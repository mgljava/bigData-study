package com.github.mgljava.mr.ncdc;

import com.github.mgljava.mr.wordcount.WorldCountMapper;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class MultipleInputsTest {
  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
    if (args.length != 3) {
      System.err.println("Usage: MaxTemperature<input path1><input path2> <output path>");
      System.exit(-1);
    }

    Job job = Job.getInstance(new Configuration(true));

    job.setJarByClass(MaxTemperature.class);
    job.setJobName("MaxTemperature");

    /**
     * 如果有多种输入格式的文件采用一个mapper实现,那么可以采用如下的方式
     * @see(org.apache.hadoop.mapreduce.lib.input.MultipleInputs)
     * MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class);
     * job.setMapperClass(MaxTemperatureMapper.class);
     */

    MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, MaxTemperatureMapper.class);
    MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, WorldCountMapper.class); // 这里只是举例,可以采用多个mapper来实现

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    job.waitForCompletion(true);
  }
}
