/*

原始数据 bucket.txt
1,tom,11
2,cat,22
3,dog,33
4,hive,44
5,hbase,55
6,mr,66
7,alice,77
8,scala,88

*/

// 原始数据存放表
CREATE TABLE bucket_source
(
    id   INT,
    name STRING,
    age  INT
)
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

LOAD DATA LOCAL INPATH '../bucket.txt' into table bucket_source;

// 分桶表
CREATE TABLE bucket_target
(
    id   INT,
    name STRING,
    age  INT
)
    CLUSTERED BY (age) INTO 4 BUCKETS
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

// 向分桶表插入数据
insert into table bucket_target select id, name, age from bucket_source;

// 抽样, 从第二个表开始抽，抽取（桶的个数除以4）个桶的数据
select * from bucket_target tablesample ( bucket 2 out of 4 on age);

// 如果有4个桶 那么bucket 2 out of 8 on age 表示的是 从第二个桶开始抽，抽取的数据为 4/8 的数据。