package com.ndsc.myClickCal.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 稿件统计接口响应类
 * @author Michael
 * @since 2018-8-28
 */
@Data
public class ArticleStatisticsResponse implements Serializable {

    private static final long serialVersionUID = -6851011668887357743L;

    private String articleId;

    private BigInteger number;


}
