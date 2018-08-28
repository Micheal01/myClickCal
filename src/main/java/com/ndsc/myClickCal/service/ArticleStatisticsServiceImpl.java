package com.ndsc.myClickCal.service;

import com.ndsc.myClickCal.dao.ArticleStatisticsDao;
import com.ndsc.myClickCal.entity.ArticleStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Michael
 * @since 2018-8-28
 */
@Service
public class ArticleStatisticsServiceImpl {

    @Autowired
    private ArticleStatisticsDao articleStatisticsDao;

    //批量保存
    public void batchSave(Collection<ArticleStatistics> articleStatistics)
    {
        articleStatisticsDao.save(articleStatistics);
    }

    //获取所有数据
    public List<ArticleStatistics> findAll()
    {
        return ( List<ArticleStatistics>)articleStatisticsDao.findAll();
    }

    /**
     * 根据稿件id获取数据
     * @param articleId 稿件id
     */
    public ArticleStatistics findByArticleId(String articleId)
    {
        return articleStatisticsDao.findByArticleId(articleId);
    }
}
