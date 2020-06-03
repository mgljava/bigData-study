package com.github.mgljava.mr.ncdc;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 作业计数器，用户自定义的计数器
 */
public class MaxTemperatureMapperWithCounters extends Mapper<LongWritable, Text, Text, IntWritable> {

  enum Temperature {
    MISSING, // 该名称为计数器的名称
    MALFORMED
  }

  @Override
  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (true) {
      context.getCounter(Temperature.MISSING).increment(1L);
    } else {
      context.getCounter(Temperature.MALFORMED).increment(1L);
    }
  }
}
