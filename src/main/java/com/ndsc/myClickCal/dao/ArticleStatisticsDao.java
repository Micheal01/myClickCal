package com.ndsc.myClickCal.dao;

import com.ndsc.myClickCal.entity.ArticleStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Michael
 * @since 2018-8-28
 */
@Repository
public interface ArticleStatisticsDao extends CrudRepository<ArticleStatistics,String>{

        ArticleStatistics findByArticleId(String articleId);

//    //其他sql用法
//    @Query("select name from ArticleStatistics t where t.userName=:name")
//    public ArticleStatistics findUserByName(@Param("name") String name);

}
