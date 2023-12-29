package com.wgs.hitojjudgeservice.judge.codesandbox.Impl;

import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeRequest;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeResponse;

/**
 * 第三方代码沙箱
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
