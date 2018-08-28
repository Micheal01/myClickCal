package com.ndsc.myClickCal.controller;

import com.ndsc.myClickCal.base.MyResponse;
import com.ndsc.myClickCal.base.ResponseEnumCode;
import com.ndsc.myClickCal.dto.ArticleStatisticsRequest;
import com.ndsc.myClickCal.entity.ArticleStatistics;
import com.ndsc.myClickCal.entity.ArticleStatisticsIp;
import com.ndsc.myClickCal.service.ArticleStatisticsIpServiceImpl;
import com.ndsc.myClickCal.service.ArticleStatisticsServiceImpl;
import com.ndsc.myClickCal.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 获取稿件的点击量并自动加1
 *  同一个ip 同一个稿件 一天只能加1
 * @author Michael
 * @since 2018-8-27
 */
@RestController
@RequestMapping
public class ClickCalController {

    private  Object wait=new Object();

    private Logger logger=LoggerFactory.getLogger(ClickCalController.class);

    //数据集合
    public static List<ArticleStatistics> DATA_CENTER=null;

    //每次访问IP集
    public static List<ArticleStatisticsIp> IP_FILTER=null;

    @Autowired
    private  ArticleStatisticsServiceImpl articleStatisticsService;

    @Autowired
    private ArticleStatisticsIpServiceImpl articleStatisticsIpService;

    @RequestMapping
    public String  welcome()
    {
        return "welcome from articleStatistics system !";
    }

    @RequestMapping("/queryById")
    public MyResponse findByArticleId(ArticleStatisticsRequest request,
                                             HttpServletRequest servletRequest)
    {
        MyResponse myResponse=new MyResponse();
        if(request==null|| StringUtils.isEmpty(request.getArticleId()))
        {
            myResponse.fail(ResponseEnumCode.IllegalArgumentException);
            return myResponse;
        }
        //过滤同一个外网ip 同一个稿件
        if(needFilterIp(servletRequest,request.getArticleId()))
        {
            myResponse.success("");
            return myResponse;
        }

        if(DATA_CENTER==null)
        {
            synchronized (wait)
            {
                if(DATA_CENTER==null)
                {
                    //数据中心如果没有数据就初始化下
                    List<ArticleStatistics> all = articleStatisticsService.findAll();
                    DATA_CENTER=new CopyOnWriteArrayList<>(all);
                }
            }
        }

        if(IP_FILTER==null)
        {
            synchronized (wait)
            {
                if(IP_FILTER==null)
                {
                    //数据中心如果没有数据就初始化下
                    List<ArticleStatisticsIp> all = articleStatisticsIpService.findAll();
                    IP_FILTER=new CopyOnWriteArrayList<>(all);
                }
            }
        }

        return myResponse;

    }

    //过滤同一个外网ip 同一个稿件
    private boolean needFilterIp(HttpServletRequest request,String articleId)
    {
        String ip=HttpUtil.getIp(request);
        ArticleStatisticsIp articleStatisticsIp = getArticleStatisticsIp(ip,articleId);
        if(articleStatisticsIp!=null)
        {
            Date updateDate = articleStatisticsIp.getUpdateDate();
            //上次更新时间和当前时间比较小于一天得话就过滤
            if(updateDate.getTime()-(System.currentTimeMillis())<60*24*60*1000) {
                //需要过滤
                return true;
            }
            return  false;
        }
        else
        {
            ArticleStatisticsIp articleStatisticsIp_new=new ArticleStatisticsIp();
            articleStatisticsIp_new.setArticleId(articleId);
            articleStatisticsIp_new.setUpdateDate((java.sql.Date) new Date());
            articleStatisticsIp_new.setIp(ip);
            IP_FILTER.add(articleStatisticsIp_new);
            return false;
        }

    }

    //通过ip地址和稿件获取访问记录
    private ArticleStatisticsIp getArticleStatisticsIp(String ip,String articleId)
    {
        for (ArticleStatisticsIp asi:IP_FILTER) {
            if(ip.equals(asi.getIp())&&articleId.equals(asi.getArticleId()))
            {
                return asi;
            }
        }
        return null;
    }

}
