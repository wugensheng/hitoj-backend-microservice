package com.wgs.hitojjudgeservice.judge.codesandbox;

import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeRequest;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 静态代理类 实现代码沙箱接口，增强代码沙箱通用能力，例如日志信息
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public  CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        log.info("代码沙箱调用请求信息:" + excuteCodeRequest.toString());
        ExcuteCodeResponse excuteCodeResponse = codeSandbox.excuteCode(excuteCodeRequest);
        log.info("代码沙箱调用返回信息:" + excuteCodeResponse.toString());
        return excuteCodeResponse;
    }
}
