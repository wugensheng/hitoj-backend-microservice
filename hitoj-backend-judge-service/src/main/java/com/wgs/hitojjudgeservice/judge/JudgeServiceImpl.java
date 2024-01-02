package com.wgs.hitojjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.wgs.hitojcommon.common.ErrorCode;
import com.wgs.hitojcommon.exception.BusinessException;
import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.wgs.hitojjudgeservice.judge.strategy.JudgeContext;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeRequest;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeResponse;
import com.wgs.hitojmodel.codeSandbox.JudgeInfo;
import com.wgs.hitojmodel.dto.question.JudgeCase;
import com.wgs.hitojmodel.dto.question.JudgeConfig;
import com.wgs.hitojmodel.entity.Question;
import com.wgs.hitojmodel.entity.QuestionSubmit;
import com.wgs.hitojmodel.enums.JudgeInfoMessageEnum;
import com.wgs.hitojmodel.enums.QuestionSubmitStatusEnum;
import com.wgs.serviceclientservice.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务接口
 * 1. 根据ID获取提交的code，判题用例信息
 * 2. 调用代码沙箱执行代码
 * 3. 对返回结果进行判定，修改题目提交的状态信息
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    QuestionFeignClient questionService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = questionService.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交不存在");
        }
        Question question = questionService.getQuestionById(questionSubmit.getQuestionId());
        String judgeCaseStr = question.getJudgeCase();
        String judgeConfigStr = question.getJudgeConfig();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);

        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交正在判题中");
        }
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean result = questionService.updateQuestionSubmitById(questionSubmitUpdate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新提交状态失败");
        }

        // 调用沙箱执行
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        CodeSandbox codeSandbox = new CodeSandboxProxy(CodeSandboxFactory.getCodeSandbox(type));
        ExcuteCodeRequest request = ExcuteCodeRequest.builder()
                .code(code)
                .language(language)
                .input(inputList)
                .build();
        ExcuteCodeResponse response = codeSandbox.excuteCode(request);
        JudgeInfo judgeInfo = response.getJudgeInfo();
        List<String> outputList = response.getOutput();
        // 3. 对结果和运行信息进行判定
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeConfig(judgeConfig);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestionSubmit(questionSubmit);
        judgeContext.setInputList(inputList);

        JudgeManager judgeManager = new JudgeManager();
        judgeInfo  = judgeManager.doJudge(judgeContext);

        // 更新数据库中提交题目的状态
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue()); // 提交状态更新为成功，即表示代码运行成功
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        result = questionService.updateQuestionSubmitById(questionSubmitUpdate);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交题目判题信息更新失败");
        }
        QuestionSubmit questionSubmitResult = questionService.getQuestionSubmitById(questionSubmitId);
        // todo 处理并发事务（乐观锁+重试机制）
        questionService.updateSubmitNumById(question.getId());
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            questionService.updateAcceptedNumById(question.getId());
        }
        return questionSubmitResult;
    }
}
