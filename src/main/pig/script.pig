/* 从本地文件中加载记录，并将记录赋值为year、temperature、quality字段的值 */
records = LOAD '/data/ncdc/input/sample.txt' AS (year:chararray, temperature:int, quality:int);
/* 过滤掉无用的数据 */
filtered_records = FILTER records BY temperature != 9999 AND (quality == 0 OR qualitu == 1 OR quality == 4 OR quality == 5 OR quality == 9);
/* 按year进行分组 */
grouped_records = GROUP filtered_records BY year;
/* 取分组中最大的记录 */
max_temp = FOREACH grouped_records GENERATE group, MAX(filtered_records.temperature);
/* 显示数据 */
DUMP max_temp;