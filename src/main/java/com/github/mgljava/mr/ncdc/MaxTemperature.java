package com.github.mgljava.mr.ncdc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 统计 NCDC 每年的最大气温
 */
public class MaxTemperature {

  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      System.err.println("Usage: MaxTemperature<input path> <output path>");
      System.exit(-1);
    }

    Job job = Job.getInstance(new Configuration(true));

    job.setJarByClass(MaxTemperature.class);
    job.setJobName("MaxTemperature");
    job.setMapperClass(MaxTemperatureMapper.class);
    job.setReducerClass(MaxTemperatureReduce.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
  }
}
