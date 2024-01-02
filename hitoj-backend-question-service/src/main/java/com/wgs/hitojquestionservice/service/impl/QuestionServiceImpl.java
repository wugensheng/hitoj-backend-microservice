package com.wgs.hitojquestionservice.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.wgs.hitojcommon.common.ErrorCode;
import com.wgs.hitojcommon.constant.CommonConstant;
import com.wgs.hitojcommon.exception.BusinessException;
import com.wgs.hitojcommon.exception.ThrowUtils;
import com.wgs.hitojcommon.utils.SqlUtils;
import com.wgs.hitojmodel.codeSandbox.JudgeInfo;
import com.wgs.hitojmodel.dto.question.QuestionQueryRequest;
import com.wgs.hitojmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wgs.hitojmodel.entity.Question;
import com.wgs.hitojmodel.entity.QuestionSubmit;
import com.wgs.hitojmodel.entity.User;
import com.wgs.hitojmodel.enums.JudgeInfoMessageEnum;
import com.wgs.hitojmodel.vo.QuestionSubmitVO;
import com.wgs.hitojmodel.vo.QuestionVO;
import com.wgs.hitojmodel.vo.UserVO;
import com.wgs.hitojquestionservice.service.QuestionService;
import com.wgs.hitojquestionservice.mapper.QuestionMapper;
import com.wgs.hitojquestionservice.service.QuestionSubmitService;
import com.wgs.hitojquestionservice.service.StaticService;
import com.wgs.serviceclientservice.service.QuestionFeignClient;
import com.wgs.serviceclientservice.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author wugensheng
* @description 针对表【question(提目表)】的数据库操作Service实现
* @createDate 2023-12-23 16:03:44
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {
    private final static Gson GSON = new Gson();

    @Resource
    private UserFeignClient userService;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    @Lazy
    private StaticService staticService;
//    @Resource
//    private QuestionThumbMapper questionThumbMapper;
//
//    @Resource
//    private QuestionFavourMapper questionFavourMapper;
//
//    @Resource
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 校验题目是否合法
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取查询包装类(用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的查询QueryWapper类)
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 获取当前用户的所有提交结果
        User loginUser = userService.getLoginUser(request);
        List<QuestionSubmit> questionSubmitList = staticService.getQuestionSubmitListByUser(loginUser);
        Set<Long> acceptedQuestionIdSet = staticService.getAcceptedQuestionIdSet(questionSubmitList);

        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            if (acceptedQuestionIdSet.contains(questionVO.getId())) {
                questionVO.setStatus(1);
            } else {
                questionVO.setStatus(0);
            }
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public Integer updateSubmitNumById(long questionId) {
        return questionMapper.update(null, new UpdateWrapper<Question>().setSql("submitNum = submitNum + 1").eq("id", questionId));
    }

    @Override
    public Integer updateAcceptedNumById(long questionId) {
        return questionMapper.update(null, new UpdateWrapper<Question>().setSql("acceptedNum = acceptedNum + 1").eq("id", questionId));
    }
}




