package com.ndsc.myClickCal.controller;

import com.ndsc.myClickCal.base.DataResponse;
import com.ndsc.myClickCal.base.ResponseEnumCode;
import com.ndsc.myClickCal.dto.ArticleStatisticsRequest;
import com.ndsc.myClickCal.dto.ArticleStatisticsResponse;
import com.ndsc.myClickCal.entity.ArticleStatistics;
import com.ndsc.myClickCal.entity.ArticleStatisticsIp;
import com.ndsc.myClickCal.service.ArticleStatisticsIpServiceImpl;
import com.ndsc.myClickCal.service.ArticleStatisticsServiceImpl;
import com.ndsc.myClickCal.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //稿件统计缓存数据集合
    private List<ArticleStatistics> DATA_CENTER=null;

    //稿件统计IP统计缓存数据集合
    private  List<ArticleStatisticsIp> IP_FILTER=null;

    //需要更新的IP数据
    private  List<ArticleStatisticsIp> needSaveData_IP=new CopyOnWriteArrayList<>();

    //需要更新的数据
    private  List<ArticleStatistics> needSaveData=new CopyOnWriteArrayList<>();

    @Autowired
    private  ArticleStatisticsServiceImpl articleStatisticsService;

    @Autowired
    private ArticleStatisticsIpServiceImpl articleStatisticsIpService;

    //定时任务正在保存中表示
    private volatile boolean isSaveIng=false;

    //是否需要保存IP数据
    private volatile boolean isNeedSaveIP=false;

    //是否需要保存数据
    private volatile boolean isNeedSave=false;

    //上次获取数据库时间
    private volatile  Date pullDate=null;

    @RequestMapping
    public String  welcome()
    {
        return "welcome from articleStatistics system !";
    }

    @RequestMapping("/queryById")
    public DataResponse findByArticleId(ArticleStatisticsRequest request,
                                        HttpServletRequest servletRequest)
    {
        DataResponse dataResponse = new DataResponse();
        try {
            ArticleStatisticsResponse statisticsResponse = new ArticleStatisticsResponse();
            if (request == null || StringUtils.isEmpty(request.getId())) {
                dataResponse.fail(ResponseEnumCode.IllegalArgumentException);
                return dataResponse;
            }
            //校验是否有不合法字符或长度不匹配
            if (needFilterLawless(request.getId())) {
                dataResponse.fail(ResponseEnumCode.IllegalArgumentException);
                return dataResponse;
            }
            //缓存没数据的话就尝试拉去库里数据
            pullData();

            //过滤同一个外网ip 同一个稿件
            if (needFilterIp(servletRequest, request.getId())) {
                //返回之前访问的数据
                ArticleStatistics as = getArticleStatistics(request.getId());
                if (as == null) {
                    statisticsResponse.setArticleId(request.getId());
                    statisticsResponse.setNumber(BigInteger.valueOf(1));
                }
                else {
                    statisticsResponse.setArticleId(as.getArticleId());
                    statisticsResponse.setNumber(as.getLookNumber());
                }
                dataResponse.success(statisticsResponse);
                return dataResponse;
            }

            //缓存中心有数据
            ArticleStatistics as = getArticleStatistics(request.getId());
            if (as == null) {
                statisticsResponse.setArticleId(request.getId());
                statisticsResponse.setNumber(BigInteger.valueOf(1));
                //加入待保存集合
                saveData(request.getId(), null,statisticsResponse.getNumber());
            }
            else {
                //统计量加一
                statisticsResponse.setArticleId(as.getArticleId());
                statisticsResponse.setNumber(as.getLookNumber().add(BigInteger.valueOf(1)));
                //加入待保存集合
                saveData(as.getArticleId(), as.getId(),statisticsResponse.getNumber());
            }
            dataResponse.success(statisticsResponse);

        }
        catch (Exception e)
        {
            dataResponse.fail(ResponseEnumCode.SERVER_EXCEPTION,logger,e);
        }
        return dataResponse;

    }

    @RequestMapping("/getDigit")
    public BigInteger getDigit(ArticleStatisticsRequest request,
                                        HttpServletRequest servletRequest)
    {
        BigInteger dataResponse;
        try {
            if (request == null || StringUtils.isEmpty(request.getId())) {
                 return BigInteger.valueOf(0);
            }
            //校验是否有不合法字符或长度不匹配
            if (needFilterLawless(request.getId())) {
                return BigInteger.valueOf(0);
            }
            //缓存没数据的话就尝试拉去库里数据
            pullData();

            //过滤同一个外网ip 同一个稿件
            if (needFilterIp(servletRequest, request.getId())) {
                //返回之前访问的数据
                ArticleStatistics as = getArticleStatistics(request.getId());
                if (as == null) {
                    dataResponse=BigInteger.valueOf(1);
                }
                else {
                    //统计量加一
                    dataResponse=as.getLookNumber().add(BigInteger.valueOf(1));
                }
                return dataResponse;
            }

            //缓存中心有数据
            ArticleStatistics as = getArticleStatistics(request.getId());
            if (as == null) {
                dataResponse=BigInteger.valueOf(1);
                //加入待保存集合
                saveData(request.getId(), null,dataResponse);
            }
            else {
                //统计量加一
                dataResponse=as.getLookNumber().add(BigInteger.valueOf(1));
                //加入待保存集合
                saveData(as.getArticleId(), as.getId(),dataResponse);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  BigInteger.valueOf(0);
        }
        return dataResponse;

    }


    //校验是否有不合法字符或长度不匹配
    private boolean needFilterLawless(String articleId) {
        if(articleId.length()>32)
        {
            return true;
        }
        String pattern = "[a-zA-Z0-9-_]*";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        Matcher matcher = r.matcher(articleId);
        boolean isMatches = matcher.matches();
        if(!isMatches)
        {
            //匹配上说明有非法字符
            return  true;
        }
        return false;
    }

    //数据
    private void saveData(String articleId, String id,BigInteger number)
    {
        ArticleStatistics  as_data=new ArticleStatistics();
        as_data.setId(id);
        as_data.setArticleId(articleId);
        as_data.setLookNumber(number);
        as_data.setUpdateDate(new Date());
        //加入待保存的数据集合
        needSaveData.add(as_data);

        if(StringUtils.isEmpty(id))
        {
            //加入数据缓存中心
            DATA_CENTER.add(as_data);
        }
        else
        {
            //修改缓存
            for (ArticleStatistics asi:DATA_CENTER) {
                if(id.equals(asi.getId()))
                {
                    //更新数据缓存中心
                    asi.setUpdateDate(new Date());
                    asi.setLookNumber(number);
                }
            }
        }
        //允许保存数据
        isNeedSave=true;
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
            if(System.currentTimeMillis()-updateDate.getTime()<60*24*60*1000) {
                //需要过滤
                return true;
            }
            return  false;
        }
        else
        {
            ArticleStatisticsIp articleStatisticsIp_new=new ArticleStatisticsIp();
            articleStatisticsIp_new.setArticleId(articleId);
            articleStatisticsIp_new.setUpdateDate(new Date());
            articleStatisticsIp_new.setIp(ip);
            //加入数据缓存中心
            IP_FILTER.add(articleStatisticsIp_new);
            //允许保存数据
            isNeedSaveIP=true;
            //加入待保存的数据集合
            needSaveData_IP.add(articleStatisticsIp_new);
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

    //通过稿件获取访问记录
    private ArticleStatistics getArticleStatistics(String articleId)
    {
        for (ArticleStatistics asi:DATA_CENTER) {
            if(articleId.equals(asi.getArticleId()))
            {
                return asi;
            }
        }
        return null;
    }

    private void pullData()
    {
        if (DATA_CENTER == null) {
            synchronized (wait) {
                if (DATA_CENTER == null) {
                    //数据中心如果没有数据就初始化下
                    List<ArticleStatistics> all = articleStatisticsService.findAll();
                    DATA_CENTER = new CopyOnWriteArrayList<>(all);
                    pullDate=new Date();
                }
            }
        }

        if (IP_FILTER == null) {
            synchronized (wait) {
                if (IP_FILTER == null) {
                    //数据中心如果没有数据就初始化下
                    List<ArticleStatisticsIp> all = articleStatisticsIpService.findAll();
                    IP_FILTER = new CopyOnWriteArrayList<>(all);
                    pullDate=new Date();
                }
            }
        }
    }

    //定时任务开启 10秒一次
    @Scheduled(cron = "0/10 * * * * *")
    public  void executeSaveDataToDataBase()
    {
        //保存数据
        try {
            if (!isSaveIng) {
                //打开正在保存标志
                isSaveIng = true;

                if (isNeedSave) {
                    //防止保存中数据变化
                    List<ArticleStatistics> saveData = needSaveData;
                    articleStatisticsService.batchSave(saveData);
                    //关闭需要保存标识
                    isNeedSave = false;
                    //删掉上次保存的数据
                    needSaveData.removeAll(saveData);
                }

                if (isNeedSaveIP) {
                    //防止保存中数据变化
                    List<ArticleStatisticsIp> saveData = needSaveData_IP;
                    articleStatisticsIpService.batchSave(saveData);
                    //关闭需要保存标识
                    isNeedSaveIP = false;
                    //删掉上次保存的数据
                    needSaveData_IP.removeAll(saveData);
                }
                //关闭正在保存标志
                isSaveIng = false;
            }
        }
        catch (Exception e)
        {
            //关闭正在保存标志
            logger.error(e.getMessage());
            e.printStackTrace();
            isSaveIng = false;
        }


        //更新本地缓存数据
        if(pullDate==null)
        {
            pullData();
        }
    }

}
