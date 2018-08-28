package com.ndsc.myClickCal.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 稿件统计接口请求
 * @author Michael
 * @since 2018-8-28
 */
@Data
public class ArticleStatisticsRequest implements Serializable {
    private static final long serialVersionUID = 512227171237064974L;

    private String articleId;


}
