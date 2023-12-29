package com.wgs.hitojmodel.dto.questionsubmit;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 提交题目
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;


    /**
     * 题目ID
     */
    private Long questionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}