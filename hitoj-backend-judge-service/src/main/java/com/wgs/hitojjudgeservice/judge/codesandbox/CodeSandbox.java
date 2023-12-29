package com.wgs.hitojjudgeservice.judge.codesandbox;


import com.wgs.hitojmodel.codeSandbox.ExcuteCodeRequest;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest);
}
