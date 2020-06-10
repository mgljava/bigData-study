package com.github.mgljava.mr.temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// 从数据中提取每月气温最高的两天
public class TemperatureJob {

  public static void main(String[] args) throws Exception {

    Configuration configuration = new Configuration(true);
    Job job = Job.getInstance(configuration);
    job.setJarByClass(TemperatureJob.class);

    // mapper 阶段
    job.setMapperClass(TemperatureMapper.class);
    job.setMapOutputKeyClass(TemperatureModel.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setPartitionerClass(TemperaturePartition.class);
    job.setSortComparatorClass(TemperatureComparator.class);

    // reduce 阶段
    // 分组比较器
    job.setGroupingComparatorClass(TemperatureGroupingComparator.class);
    job.setReducerClass(TemperatureReduce.class);

    // 输入输出配置
    Path input = new Path("/data/temperature/input");
    FileInputFormat.addInputPath(job, input);
    Path output = new Path("/data/temperature/output");
    if (output.getFileSystem(configuration).exists(output)) {
      output.getFileSystem(configuration).delete(output, true);
    }
    FileOutputFormat.setOutputPath(job, output);

    job.setNumReduceTasks(2);

    job.waitForCompletion(true);
  }
}
