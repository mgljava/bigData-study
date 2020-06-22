-- 创建气温记录表
CREATE TABLE records
(
    year        STRING,
    temperature INT,
    quality     INT
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

--
select *
from word_count;

-- 员工表，演示各种数据结构的使用
CREATE TABLE employees
(
    name         STRING,
    salary       FLOAT,
    subordinates ARRAY<STRING>,
    deductions   MAP<STRING, FLOAT>,
    address      STRUCT<street:STRING, city:STRING, state:STRING, zip:INT>
) comment '员工表'
    -- 指明表的元信息
    tblproperties ('create-at' = 'me', 'create-date' = '2020-06-20')
    -- 指明存储路径
    location '/usr/hive/warehouse/hive_study.db/employees';

-- 分区表，先按照country分区，再按照state分区
CREATE TABLE employees_partition
(
    name         STRING,
    salary       FLOAT,
    subordinates ARRAY<STRING>,
    deductions   MAP<STRING, FLOAT>,
    address      STRUCT<street:STRING, city:STRING, state:STRING, zip:INT>
) comment '员工分区表'
PARTITIONED BY (country STRING, state STRING)

-- 宝尊股票表
create table baozun_stock(
  stock_date STRING,
  stock_open float,
  stock_high float,
  stock_low float,
  stock_close float,
  stock_ajd_close float,
  stock_volume int
) comment '宝尊股票表' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n';
