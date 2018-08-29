#稿件的访问量统计
##【同一个ip 同一个稿件 一天只能加1】
##服务端配置 
###要求：jdk7

###项目启动方法，内置tomcat，且会自动创建表结构
java -jar myClickCal-1.0.0.jar 

###以下可以自定义配置
###端口号
--server.port=8999 
###驱动
--spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
#### 数据库连接
--spring.datasource.url=jdbc:oracle:thin:@218.92.162.226:1521:orcl
#### 数据库账号
--spring.datasource.username=yd_jcms
####数据库密码
--spring.datasource.password=1

####参考例子
java -jar myClickCal-1.0.0.jar --server.port=8999 --spring.datasource.url=jdbc:oracle:thin:@218.92.162.226:1521:orcl --spring.datasource.username=yd_jcms --spring.datasource.password=1

####注意事项
#####如果后台手工改数据库表数据请重启服务，因为后台数据是每一个小时更新一次缓存数据，每十秒批量保存数据

##客户端调用

###接口地址
####http://ip:port/queryById?id=xx
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