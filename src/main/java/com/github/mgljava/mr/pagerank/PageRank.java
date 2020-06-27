package com.github.mgljava.mr.pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Google PageRank 算法
 */
public class PageRank {

  enum MyCounter {
    my
  }

  public static void main(String[] args) {

    Configuration configuration = new Configuration(true);
    configuration.set("mapreduce.app-submision.cross-platform", "true");
    configuration.set("mapreduce.framework.name", "local");

    double d = 0.001;
    int i = 0;
    while (true) {
      i++;
      try {
        configuration.setInt("runCount", i);
        FileSystem fileSystem = FileSystem.get(configuration);
        Job job = Job.getInstance(configuration);
        job.setJarByClass(PageRank.class);
        job.setJobName("pageRank" + i);
        job.setMapperClass(PageRankMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(PageRankReduce.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        Path inputPath = new Path("/data/mr/pagerank/input/");
        String output = "/data/mr/pagerank/output/pr";
        if (i > 1) {
          inputPath = new Path(output + (i - 1));
        }
        FileInputFormat.addInputPath(job, inputPath);

        Path outputPath = new Path(output + i);
        if (fileSystem.exists(outputPath)) {
          fileSystem.delete(outputPath, true);
        }
        FileOutputFormat.setOutputPath(job, outputPath);
        boolean completion = job.waitForCompletion(true);
        if (completion) {
          System.out.println("success ");
          long sum = job.getCounters().findCounter(MyCounter.my).getValue();
          System.out.println(sum);
          double avgd = sum / 4000.0;
          if (avgd < d) {
            break;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
