/*

原始数据 dynamic_partition.txt
编号 姓名 年龄 性别 住址
1   张三  20  boy 北京
2   李四  30  boy 天津
3   王五  20  man 重庆
4   小明  30  man 北京
5   小红  20  boy 南京
6   小玲  30  man 乐山
7   小张  20  man 成都
8   小爱  10  boy 河南
9   小啊  10  man 眉山
10  小王  20  boy 芜湖

*/

// 原始表，存放原始数据
create table dynamic_partition_source
(
    id      int,
    name    string,
    age     int,
    sex     string,
    address string
) row format delimited fields terminated by ' '
    lines terminated by '\n';

LOAD DATA LOCAL INPATH '../dynamic_partition.txt' into table dynamic_partition_source;

// 分区表，通过动态分区计算后存储数据
create table dynamic_partition_target
(
    id      int,
    name    string,
    address string
) partitioned by (age int, sex string)
    row format delimited fields terminated by ' '
        lines terminated by '\n';

// 加载数据到分区表
from dynamic_partition_source
insert overwrite table dynamic_partition_target partition(age,sex)
select id, name, address,age,sex distribute by age, sex;