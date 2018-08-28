package com.ndsc.myClickCal.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author Michael
 * @since 2018-8-27
 */
@Entity
@Table(name="TJ_ARTICLE")
@Data
public class ArticleStatistics {

    @Id
    @GeneratedValue
    private String id; //主键

    @Column(name = "ARTICLE_ID")
    private String articleId; //文章id

    @Column(name = "UPDATE_DATE")
    private Date updateDate; //最近修改时间

    @Column(name = "LOOK_NUMBER")
    private BigInteger lookNumber;//访问量


}
