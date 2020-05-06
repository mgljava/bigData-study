package com.github.mgljava.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// 单词统计
public class WorldCount {

  public static void main(String[] args) throws Exception {

    Configuration configuration = new Configuration(true);

    Job job = Job.getInstance(configuration);
    job.setJarByClass(WorldCount.class);

    // Specify various job-specific parameters
    job.setJobName("world-count-mgl");

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.setMapperClass(WorldCountMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setReducerClass(WorldCountReduce.class);

    job.waitForCompletion(true);
  }
}
