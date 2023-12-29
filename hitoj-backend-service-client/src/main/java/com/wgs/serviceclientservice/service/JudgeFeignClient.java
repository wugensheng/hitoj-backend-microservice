package com.wgs.serviceclientservice.service;

import com.wgs.hitojmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务接口
 * 1. 根据ID获取提交的code，判题用例信息
 * 2. 调用代码沙箱执行代码
 * 3. 对返回结果进行判定，修改题目提交的状态信息
 */
@FeignClient(name = "hitoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
