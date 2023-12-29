package com.wgs.hitojjudgeservice.judge.codesandbox;

import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wgs.hitojjudgeservice.judge.codesandbox.Impl.ExampleCodeSandbox;
import com.wgs.hitojjudgeservice.judge.codesandbox.Impl.ReomteCodeSandbox;
import com.wgs.hitojjudgeservice.judge.codesandbox.Impl.ThirdPartyCodeSandbox;

public class CodeSandboxFactory {

    public static CodeSandbox getCodeSandbox(String name) {
        switch (name) {
            case "example": return new ExampleCodeSandbox();
            case "remote": return new ReomteCodeSandbox();
            case "thirdparty": return new ThirdPartyCodeSandbox();
            default: return new ExampleCodeSandbox();
        }
    }
}
