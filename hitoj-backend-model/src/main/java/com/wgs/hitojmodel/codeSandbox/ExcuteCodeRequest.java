package com.wgs.hitojmodel.codeSandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码沙箱执行请求类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuteCodeRequest {

    /**
     * 用例输入
     */
    private List<String> input;

    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;
}
