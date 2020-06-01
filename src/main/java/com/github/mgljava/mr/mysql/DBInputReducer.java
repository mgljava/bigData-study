package com.github.mgljava.mr.mysql;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DBInputReducer extends Reducer<LongWritable, Text, StudentInfoRecord, Text> {

  @Override
  protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    String[] splits = values.iterator().next().toString().split(" ");
    StudentInfoRecord r = new StudentInfoRecord();
    r.id = Integer.parseInt(splits[0]);
    r.name = splits[1];
    context.write(r, new Text(r.name));
  }
}
