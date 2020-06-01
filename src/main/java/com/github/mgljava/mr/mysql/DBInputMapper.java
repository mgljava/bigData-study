package com.github.mgljava.mr.mysql;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DBInputMapper extends Mapper<LongWritable, StudentInfoRecord, LongWritable, Text> {

  @Override
  protected void map(LongWritable key, StudentInfoRecord value, Context context) throws IOException, InterruptedException {
    context.write(new LongWritable(value.id), new Text(value.toString()));
  }
}
