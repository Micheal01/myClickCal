#稿件的访问量统计
##【同一个ip 同一个稿件 一天只能加1】
##服务端配置 
###要求：jdk7

##启动步骤1
###修改配置文件
####右击用压缩文件打开myClickCal-1.0.0.jar包后进入到"BOOT-INF\class"后然后用记事本打开“application.properties”文件后修改配置

##启动步骤2
###执行包里面的startup.bat即可，里面的表会自动生成

###注意事项
####如果后台手工改数据库表数据请重启服务，因为后台数据是每一个小时更新一次缓存数据，每十秒批量保存数据

##统计表说明      
表名【TJ_ARTICLE】
字段描述
ARTICLE_ID   文章id
UPDATE_DATE  最近修改时间
LOOK_NUMBER  访问量



##客户端调用
##参考demo.zip中的index.html用法即可

##接口说明
###接口地址1
####http://ip:port/clickCal/queryById?id=xx
#### id为稿件编号，参数要求长度小于32位，且只能包含- _ 数字或字母

####返回格式JSON
{
    "code":"100",
    "data":{"articleId":"2312123112344","number":1}
    ,"msg":"成功"
}
code代码表
100成功
400参数异常
500服务器异常

data实体描述
   articleId 稿件id
   number  访问量
   
###接口地址2
####http://ip:port/clickCal/getDigit?id=xx
#### id为稿件编号，参数要求长度小于32位，且只能包含- _ 数字或字母

####返回格式数字。如果出现参数错误或系统错误统一返回0
