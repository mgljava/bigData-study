package com.github.mgljava.mr.friends;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FriendsReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  IntWritable rValue = new IntWritable();

  @Override
  protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

    /*
     * 模拟数据
     * hello:hadoop 0
     * hello:hadoop 1
     * hello:hadoop 0
     */
    int flag = 0;
    int sum = 0;
    for (IntWritable value : values) {
      if (value.get() == 0) {
        flag = 1;
      }
      sum += value.get();
    }
    // 全是间接关系
    if (flag == 0) {
      rValue.set(sum);
      context.write(key, rValue);
    }
  }
}
