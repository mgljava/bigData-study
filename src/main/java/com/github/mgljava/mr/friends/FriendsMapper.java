package com.github.mgljava.mr.friends;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FriendsMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  Text mKey = new Text();
  IntWritable mValue = new IntWritable();

  @Override
  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    // tom hello hadoop cat
    String[] lines = StringUtils.split(value.toString(), " ");

    for (int i = 1; i < lines.length; i++) {
      mKey.set(getSeq(lines[0], lines[i]));
      mValue.set(0); // 0 为直接关系
      context.write(mKey, mValue);
      // 内层循环，生成间接关系
      for (int j = i + 1; j < lines.length; j++) {
        mKey.set(getSeq(lines[i], lines[j]));
        mValue.set(1); // 1 为间接关系
        context.write(mKey, mValue);
      }
    }
  }

  // 将两个字符排序，如果是AB，返回AB，如果是BA，返回AB
  public static String getSeq(String s1, String s2) {
    if (s1.compareTo(s2) < 0) {
      return s1 + ":" + s2;
    }
    return s2 + ":" + s1;
  }
}
