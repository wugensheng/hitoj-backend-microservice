package com.wgs.hitojjudgeservice.judge.strategy;

import com.wgs.hitojmodel.codeSandbox.JudgeInfo;
import com.wgs.hitojmodel.dto.question.JudgeCase;
import com.wgs.hitojmodel.dto.question.JudgeConfig;
import com.wgs.hitojmodel.entity.Question;
import com.wgs.hitojmodel.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文 用于定义在策略中传递的参数
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> outputList;

    private List<String> inputList;

    private List<JudgeCase> judgeCaseList;

    private JudgeConfig judgeConfig;

    private Question question;

    private QuestionSubmit questionSubmit;
}
