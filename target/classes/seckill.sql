--  数据库初始化脚本 
--  创建数据库 

-- CREATE DATABASE seckill;
--  使用数据库
use seckill;
--  创建秒杀库存表
CREATE TABLE seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
name varchar(120) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '库存数量',
start_time timestamp NOT NULL COMMENT '秒杀开启时间',
end_time timestamp NOT NULL COMMENT '秒杀结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀创建时间',
PRIMARY KEY(seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--  初始化数据
insert into seckill(name, number, start_time,end_time)
values
	('1000元秒杀iPhone 6s', 1000, '2016-12-28 00:00:00', '2016-12-28 00:00:01'),
	('100元秒杀小米note', 200, '2016-12-28 00:00:00', '2016-12-28 00:00:01'),
	('500元秒杀iPad mini', 100, '2016-12-28 00:00:00', '2016-12-28 00:00:01'),
	('300元秒杀魅族MX4', 100, '2016-12-28 00:00:00', '2016-12-28 00:00:01');
	
--  秒杀成功明细表
--  需要用户登录认证的信息
create table success_killed(
seckill_id bigint NOT NULL COMMENT '秒杀商品ID',
user_phone bigint NOT NULL COMMENT '用户手机号',
state tinyint NOT NULL COMMENT '秒杀状态:-1：无效 0：成功 1：已付款 2：已发货',
create_time timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id, user_phone), /*联合主键：seckill*/
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';