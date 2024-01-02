package com.wgs.hitojquestionservice.service;

import com.wgs.hitojmodel.entity.QuestionSubmit;
import com.wgs.hitojmodel.entity.User;
import com.wgs.hitojmodel.vo.QuestionSubmitVO;

import java.util.List;
import java.util.Set;

public interface StaticService {
    /**
     * 根据用户查询对应的提交记录
     * @param loginUser
     * @return
     */
    List<QuestionSubmit> getQuestionSubmitListByUser(User loginUser);

    Set<Long> getAcceptedQuestionIdSet(List<QuestionSubmit> questionSubmitList);
}
