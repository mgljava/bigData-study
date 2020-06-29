/*
Hive 实现word count
原始数据
hello world hadoop
hive spark hello sql
hadoop spark hello
hadoop
sql
java hello
*/
-- word_count 表存储原始数据
create table word_count(
    line string
);

-- word_count_result 存放结果集
create table word_count_result(
    word string,
    times int
);

-- 执行MR程序
from (select explode(split(wc.line, ' ')) word from word_count wc) t1
insert into word_count_result
select t1.word,count(t1.word) group by t1.word;

-- 查看结果集
select * from word_count_result;