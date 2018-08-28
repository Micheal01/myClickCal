package com.ndsc.myClickCal.service;

import com.ndsc.myClickCal.dao.ArticleStatisticsDao;
import com.ndsc.myClickCal.dao.ArticleStatisticsIpDao;
import com.ndsc.myClickCal.entity.ArticleStatistics;
import com.ndsc.myClickCal.entity.ArticleStatisticsIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Michael
 * @since 2018-8-28
 */
@Service
public class ArticleStatisticsIpServiceImpl {

    @Autowired
    private ArticleStatisticsIpDao articleStatisticsDao;

    //批量保存
    public void batchSave(Collection<ArticleStatisticsIp> articleStatistics)
    {
        articleStatisticsDao.save(articleStatistics);
    }

    //获取所有数据
    public List<ArticleStatisticsIp> findAll()
    {
        return ( List<ArticleStatisticsIp>)articleStatisticsDao.findAll();
    }

}
