package com.wgs.hitojjudgeservice.judge;

import com.wgs.hitojmodel.entity.QuestionSubmit;

/**
 * 判题服务接口
 * 1. 根据ID获取提交的code，判题用例信息
 * 2. 调用代码沙箱执行代码
 * 3. 对返回结果进行判定，修改题目提交的状态信息
 */
public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);
}
