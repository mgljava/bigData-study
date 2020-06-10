package com.github.mgljava.mr.temperature;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

// 将每行数据进行组装到对象中
public class TemperatureMapper extends Mapper<LongWritable, Text, TemperatureModel, IntWritable> {

  TemperatureModel tKey = new TemperatureModel();
  IntWritable tValue = new IntWritable();

  @Override
  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    // example: 1949-10-01 14:21:02 34c
    String[] strings = StringUtils.split(value.toString(), ' ');
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.parse(strings[0], dateTimeFormatter);
    this.tKey.setYear(localDate.getYear());
    this.tKey.setMonth(localDate.getMonthValue());
    this.tKey.setDay(localDate.getDayOfMonth());

    int temperature = Integer.parseInt(strings[2].substring(0, strings[2].length() - 1));
    this.tKey.setTemperature(temperature);

    tValue.set(temperature);

    context.write(tKey, tValue);
  }
}
