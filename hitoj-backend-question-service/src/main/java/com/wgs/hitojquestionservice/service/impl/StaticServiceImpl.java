package com.wgs.hitojquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wgs.hitojmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wgs.hitojmodel.entity.QuestionSubmit;
import com.wgs.hitojmodel.entity.User;
import com.wgs.hitojmodel.enums.JudgeInfoMessageEnum;
import com.wgs.hitojmodel.vo.QuestionSubmitVO;
import com.wgs.hitojquestionservice.service.QuestionService;
import com.wgs.hitojquestionservice.service.QuestionSubmitService;
import com.wgs.hitojquestionservice.service.StaticService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StaticServiceImpl implements StaticService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Override
    public List<QuestionSubmit> getQuestionSubmitListByUser(User loginUser) {
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq(ObjectUtils.isNotEmpty(loginUser.getId()), "userId", loginUser.getId());
        return questionSubmitService.list(questionSubmitQueryWrapper);
    }

    @Override
    public Set<Long> getAcceptedQuestionIdSet(List<QuestionSubmit> questionSubmitList) {
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(QuestionSubmitVO::objToVo).collect(Collectors.toList());
        return questionSubmitVOList.stream().filter(questionSubmitVO -> {
            return questionSubmitVO.getJudgeInfo().getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue());
        }).map(QuestionSubmitVO::getQuestionId).collect(Collectors.toSet());
    }
}
