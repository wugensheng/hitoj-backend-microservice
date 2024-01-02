package com.wgs.hitojquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wgs.hitojmodel.dto.question.QuestionQueryRequest;
import com.wgs.hitojmodel.entity.Question;
import com.wgs.hitojmodel.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author wugensheng
* @description 针对表【question(提目表)】的数据库操作Service
* @createDate 2023-12-23 16:03:44
*/
public interface QuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目 封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 题目提交成功后，submitNum+1
     * @param questionId
     * @return
     */
    public Integer updateSubmitNumById(long questionId);

    /**
     * 题目通过后，acceptedNum+1
     * @param questionId
     * @return
     */
    public Integer updateAcceptedNumById(long questionId);
}
