package com.github.mgljava.mr.wordcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WorldCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

  private final IntWritable result = new IntWritable();

  // 相同的key为一组，调用一次reduce方法
  @Override
  public void reduce(Text key, Iterable<IntWritable> values,
      Context context) throws IOException, InterruptedException {
    int sum = 0;
    for (IntWritable val : values) {
      sum += val.get();
    }
    result.set(sum);
    context.write(key, result);
  }
}
