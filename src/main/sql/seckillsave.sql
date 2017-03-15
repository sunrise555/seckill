-- ��ɱִ�д洢����
DELIMITER && -- console ; ������̨�Ļ��з�ת��Ϊ&&
-- ����洢����
-- ������in �������   out �������
-- row_count():������һ���޸�����sql(delete\insert\update)��Ӱ������
-- row_count==0:δ�޸����ݣ� >0:��ʾ�޸ĵ������� <0:sql���󡢻��sqlδִ��
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
-- �洢���̶������

DELIMITER ;
set @r_result = -3;
call execute_seckill(1003, 18292037465, now(), @r_result);
select @r_result;

-- �洢�����Ż����ǣ������������м�����ʱ��
-- ��Ҫ���������洢���̣��洢����������ҵ��ǳ����ã����ڻ�������˾��������
-- ��������ɱҵ���У��洢����ֻ���м򵥵��߼��������Դ����ܴ���Ż������Կ���ʹ��
-- �����Ż����ԴﵽQPS��һ����ɱ��6000/qps