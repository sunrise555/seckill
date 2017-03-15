-- 秒杀执行存储过程
DELIMITER && -- console ; 将控制台的换行符转换为&&
-- 定义存储过程
-- 参数：in 输入参数   out 输出参数
-- row_count():返回上一条修改类型sql(delete\insert\update)的影响行数
-- row_count==0:未修改数据； >0:表示修改的行数； <0:sql错误、或该sql未执行
CREATE PROCEDURE `seckill`.`execute_seckill`
    (in v_seckill_id bigint, in v_phone bigint,
     in v_kill_time timestamp, out r_result int)
    BEGIN
        DECLARE insert_count int DEFAULT 0;
        START TRANSACTION;
        insert ignore into success_killed
            (seckill_id, user_phone, create_time)
            values (v_seckill_id, v_phone, v_kill_time);
        select row_count() into insert_count;
        IF (insert_count = 0) THEN
            ROLLBACK;
            set r_result = -1;
        ELSEIF(insert_count < 0) THEN
            ROLLBACK;
            set r_result = -2;
        ELSE
            update seckill
            set number = number - 1
            where seckill_id = v_seckill_id
                and end_time > v_kill_time
                and start_time < v_kill_time
                and number > 0;
            select row_count() into insert_count;
            IF (insert_count = 0) THEN
                ROLLBACK;
                set r_result = 0;
            ELSEIF (insert_count < 0) THEN
                ROLLBACK;
                set r_result = -2;
            ELSE
                COMMIT;
                set r_result = 1;
            END IF;
        END IF;
    END;
&&
-- 存储过程定义结束

DELIMITER ;
set @r_result = -3;
call execute_seckill(1003, 18292037465, now(), @r_result);
select @r_result;

-- 存储过程优化的是：事务所持有行级锁的时间
-- 不要过度依赖存储过程：存储过程在银行业务非常常用，但在互联网公司并不多用
-- 但是在秒杀业务中，存储过程只具有简单的逻辑，但可以带来很大的优化，所以可以使用
-- 这种优化可以达到QPS：一个秒杀单6000/qps