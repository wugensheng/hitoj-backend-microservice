package com.wgs.serviceclientservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wgs.hitojmodel.dto.question.QuestionQueryRequest;
import com.wgs.hitojmodel.entity.Question;
import com.wgs.hitojmodel.entity.QuestionSubmit;
import com.wgs.hitojmodel.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
* @author wugensheng
* @description 针对表【question(提目表)】的数据库操作Service
* @createDate 2023-12-23 16:03:44
*/
@FeignClient(name = "hitoj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    @PostMapping("/update_submitNum/id")
    Integer updateSubmitNumById(@RequestParam("questionId") long questionId);

    @PostMapping("/update_acceptedNum/id")
    Integer updateAcceptedNumById(@RequestParam("questionId") long questionId);

}
