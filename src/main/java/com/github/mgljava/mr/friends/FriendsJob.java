package com.github.mgljava.mr.friends;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 好友推荐
 */
public class FriendsJob {

  public static void main(String[] args) throws Exception {
    Configuration configuration = new Configuration(true);
    Job job = Job.getInstance(configuration);
    // conf
    job.setJarByClass(FriendsJob.class);
    // mapper
    job.setMapperClass(FriendsMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);

    //reducer
    job.setReducerClass(FriendsReducer.class);

    // input output
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.waitForCompletion(true);
  }
}
