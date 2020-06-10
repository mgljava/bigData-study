package com.github.mgljava.mr.temperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

// 分区器
public class TemperaturePartition extends Partitioner<TemperatureModel, IntWritable> {

  @Override
  public int getPartition(TemperatureModel temperatureModel, IntWritable intWritable, int numPartitions) {
    return temperatureModel.getYear() % numPartitions;
  }
}
