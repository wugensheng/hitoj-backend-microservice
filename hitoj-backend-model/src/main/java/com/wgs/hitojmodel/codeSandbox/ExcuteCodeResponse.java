package com.wgs.hitojmodel.codeSandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码沙箱执行消息返回类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuteCodeResponse {

    /**
     * 用例输出
     */
    private List<String> output;

    /**
     * 其他信息
     */
    private String message;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 执行状态
     */
    private Integer status;
}
