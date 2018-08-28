package com.ndsc.myClickCal.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * 稿件ip访问明细
 * @author Michael
 * @since 2018-8-28
 */
@Entity
@Table(name="TJ_ARTICLE_IP")
@Data
public class ArticleStatisticsIp {

    @Id
    @GeneratedValue
    private String id; //主键

    @Column(name = "IP")
    String ip;//ip 地址

    @Column(name = "ARTICLE_ID")
    private String articleId; //文章id

    @Column(name = "UPDATE_DATE")
    private Date updateDate; //最近修改时间

}
