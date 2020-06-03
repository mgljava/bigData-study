package com.github.mgljava.mr.ncdc;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

/**
 * 多路径输出
 */
public class MultipleOutputFormatReduce extends Reducer<Text, Text, NullWritable, Text> {

  private MultipleOutputs<NullWritable, Text> multipleOutputs;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    multipleOutputs = new MultipleOutputs<>(context);
  }

  @Override
  protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    for (Text text : values) {
      multipleOutputs.write(NullWritable.get(), text, key.toString());
    }
  }
}
