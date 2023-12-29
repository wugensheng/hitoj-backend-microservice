package com.wgs.hitojjudgeservice.judge;

import com.wgs.hitojjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.wgs.hitojjudgeservice.judge.strategy.JavaJudgeStrategy;
import com.wgs.hitojjudgeservice.judge.strategy.JudgeContext;
import com.wgs.hitojjudgeservice.judge.strategy.JudgeStrategy;
import com.wgs.hitojmodel.codeSandbox.JudgeInfo;
import com.wgs.hitojmodel.entity.QuestionSubmit;
import com.wgs.hitojmodel.enums.QuestionSubmitLanguageEnum;

public class JudgeManager {

    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (questionSubmit.getLanguage().equals(QuestionSubmitLanguageEnum.JAVA.getValue())) {
            judgeStrategy = new JavaJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
