package com.github.mgljava.mr.temperature;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TemperatureReduce extends Reducer<TemperatureModel, IntWritable, Text, IntWritable> {

  Text rKey = new Text();
  IntWritable rValue = new IntWritable();

  @Override
  protected void reduce(TemperatureModel key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    /*
     * 相同的key为一组，map输出的数据
     * 1949 01 01 89
     * 1949 01 11 72
     * 1949 01 12 12
     * 1949 02 01 99
     * 1949 02 21 80
     */
    int flag = 0;
    int day = 0;
    for (IntWritable ignored : values) {
      if (flag == 0) {
        // key: 1949-01-01:89
        rKey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay() + ":");
        rValue.set(key.getTemperature());
        day = key.getDay();
        flag++;
        context.write(rKey, rValue);
      }
      if (flag != 0 && day != key.getDay()) {
        rKey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay() + ":");
        rValue.set(key.getTemperature());
        context.write(rKey, rValue);
        break;
      }
    }
  }
}
