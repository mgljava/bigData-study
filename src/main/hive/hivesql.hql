-- 创建气温记录表
CREATE TABLE records(year STRING, temperature INT, quality INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

--
select * from word_count;

-- 员工表，演示各种数据结构的使用
CREATE TABLE employees(
    name STRING,
    salary FLOAT,
    subordinates ARRAY<STRING>,
    deductions MAP<STRING, FLOAT>,
    address STRUCT<street:STRING, city:STRING, state:STRING, zip:INT>
);

