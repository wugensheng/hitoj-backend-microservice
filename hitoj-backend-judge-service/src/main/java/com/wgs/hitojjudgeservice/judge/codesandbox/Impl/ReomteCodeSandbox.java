package com.wgs.hitojjudgeservice.judge.codesandbox.Impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wgs.hitojcommon.common.ErrorCode;
import com.wgs.hitojcommon.exception.BusinessException;
import com.wgs.hitojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeRequest;
import com.wgs.hitojmodel.codeSandbox.ExcuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱
 */
public class ReomteCodeSandbox implements CodeSandbox {
    private final String AUTH_REQUEST_HEADER = "auth";

    private final String AUTH_REQUEST_SECRET = "secretKey";
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        String url = "http://localhost:8090/executeCode";
        String responseStr = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(excuteCodeRequest))
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_EROR, "接口调用失败");
        }
        return JSONUtil.toBean(responseStr, ExcuteCodeResponse.class);
    }
}
