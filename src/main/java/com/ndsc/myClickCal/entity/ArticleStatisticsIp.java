package com.ndsc.myClickCal.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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
    @Basic(optional = false)
    @GeneratedValue(generator="_uuid.hex")
    @GenericGenerator(name="_uuid.hex",strategy="uuid.hex")
    @Column(name = "ID",nullable = false,length=50)
    private String id; //主键

    //以前是用外网ip来限制刷访问量现在改成用session  id来算。这样就不会出现同一个网络下ip一致的问题。
    //不过这样会出现换浏览器或重开浏览器刷页面也会加访问量的问题。这个不用管
    @Column(name = "IP")
    String ip;//ip 地址

    @Column(name = "ARTICLE_ID")
    private String articleId; //文章id

    @Column(name = "UPDATE_DATE",columnDefinition = "DATE")
    private Date updateDate; //最近修改时间

}
