package com.wgs.hitojjudgeservice.judge.codesandbox.Impl;

import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeRequest;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeResponse;
import com.wgs.hitojmodel.codeSandbox.JudgeInfo;
import com.wgs.hitojmodel.enums.QuestionSubmitStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * 示例代码沙箱 （仅仅为了跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        System.out.println("示例代码沙箱");
        List<String> output = Arrays.asList("1, 2", "2, 3");
        String message = QuestionSubmitStatusEnum.SUCCESS.getText();
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(100L);
        judgeInfo.setMessage(QuestionSubmitStatusEnum.SUCCESS.getText());
        judgeInfo.setMemory(100L);
        return ExcuteCodeResponse.builder().output(output).message(message).judgeInfo(judgeInfo).build();
    }
}
