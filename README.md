# 秒杀系统<br>
## 项目来源
项目基于<a href="http://www.imooc.com">慕课网</a>上《Java高并发秒杀API》四个系列的视频讲解内容完成<br>
* [Java高并发秒杀API之业务分析与DAO层](http://www.imooc.com/learn/587)
* [Java高并发秒杀API之Service层](http://www.imooc.com/learn/631)
* [Java高并发秒杀API之web层](http://www.imooc.com/learn/630)
* [Java高并发秒杀API之高并发优化](http://www.imooc.com/learn/632)<br>

## 项目信息
### 开发工具
* MyEclipse + Maven + SSM框架
* 前端 Bootstrap
* 数据库 MySql
* 缓存优化 Redis
### 测试
* 访问地址：`http://localhost:8080/seckill/list`
* 启动：使用jetty:run/Tomcat8.0
### 文档结构
* src/main/resources
	* /spring　　 ————spring配置文件
	* /mappers　　————mybatis映射配置文件
* src/main/sql　　    ————sql文件　　　
* src/main/java
	* /dao、/web、/service、/entity　　————dao层、web层、service层、实体层
    * /dto　　————数据封装
    * /enum　　————枚举类
    * /exception　————异常处理
## 并发优化
* 前端控制：暴露接口，防止按钮重复点击
* 动静态数据分离：CDN缓存，Redis后端缓存
* 存储过程：事务SQL在MySql端执行
* 事务竞争优化：减少事务锁的时间
## 结果展示
* 秒杀列表展示<br>
![秒杀列表](https://raw.githubusercontent.com/sunrise555/seckill/master/image/%E7%A7%92%E6%9D%80%E5%88%97%E8%A1%A8%E9%A1%B5.png)
* 秒杀详情页
![秒杀开始](https://raw.githubusercontent.com/sunrise555/seckill/master/image/%E7%A7%92%E6%9D%80%E5%BC%80%E5%A7%8B.png)
![秒杀倒计时](https://raw.githubusercontent.com/sunrise555/seckill/master/image/%E7%A7%92%E6%9D%80%E5%80%92%E8%AE%A1%E6%97%B6.png)
![秒杀资格验证](https://raw.githubusercontent.com/sunrise555/seckill/master/image/%E7%A7%92%E6%9D%80%E8%B5%84%E6%A0%BC%E9%AA%8C%E8%AF%81.png)
![秒杀结束](https://raw.githubusercontent.com/sunrise555/seckill/master/image/%E7%A7%92%E6%9D%80%E7%BB%93%E6%9D%9F.png)

## 待更新2017/04/10
