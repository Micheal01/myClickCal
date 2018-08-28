package com.ndsc.myClickCal.dao;

import com.ndsc.myClickCal.entity.ArticleStatistics;
import com.ndsc.myClickCal.entity.ArticleStatisticsIp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Michael
 * @since 2018-8-28
 */
@Repository
public interface ArticleStatisticsIpDao extends CrudRepository<ArticleStatisticsIp,String>{


}
