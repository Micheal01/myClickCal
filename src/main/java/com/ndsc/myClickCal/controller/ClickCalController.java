package com.ndsc.myClickCal.controller;

import com.ndsc.myClickCal.entity.ArticleStatistics;
import com.ndsc.myClickCal.service.ArticleStatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取稿件的点击量并自动加1
 *  同一个ip 同一个稿件 一天只能加1
 * @author Michael
 * @since 2018-8-27
 */
@RestController
public class ClickCalController {

    @Autowired
    private ArticleStatisticsServiceImpl articleStatisticsService;

    @RequestMapping("/findAll")
    public List<ArticleStatistics> findAll()
    {
        return articleStatisticsService.findAll();
    }

    @RequestMapping("/findByArticleId")
    public ArticleStatistics findByArticleId(String articleId)
    {
        return articleStatisticsService.findByArticleId(articleId);
    }


}
