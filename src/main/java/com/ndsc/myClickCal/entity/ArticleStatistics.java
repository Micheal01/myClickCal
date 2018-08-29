package com.ndsc.myClickCal.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * 稿件统计量
 * @author Michael
 * @since 2018-8-27
 */
@Entity
@Table(name="TJ_ARTICLE")
@Data
public class ArticleStatistics {

    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="_uuid.hex")
    @GenericGenerator(name="_uuid.hex",strategy="uuid.hex")
    @Column(name = "ID",nullable = false,length=50)
    private String id; //主键

    @Column(name = "ARTICLE_ID")
    private String articleId; //文章id

    @Column(name = "UPDATE_DATE",columnDefinition = "DATE")
    private Date updateDate; //最近修改时间

    @Column(name = "LOOK_NUMBER",columnDefinition = "NUMBER(19)")
    private BigInteger lookNumber;//访问量


}
