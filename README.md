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
### 文档结构
* src/main/resources
	* /spring　　————　spring配置文件
	* /mappers　　————　mybatis映射配置文件
* src/main/sql　　————　sql文件　　　
* src/main/javａ
	* /dao、/web、/service、/entity　　————　dao层、web层、service层、实体层
    * /dto　　————　数据封装
    * /enum　　————　枚举类
    * /exception　　————　异常处理
## 并发优化
* 前端控制：暴露接口，防止按钮重复点击
* 动静态数据分离：CDN缓存，Redis后端缓存
* 存储过程：事务SQL在MySql端执行
* 事务竞争优化：减少事务锁的时间
## 待更新2017/04/10
