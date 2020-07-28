package com.github.mgljava.mr.hbase;

import java.io.IOException;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * 从表里抽取列族为CF的，列名为attr1的字段值进行WC
 */
public class HBaseMapper extends TableMapper<Text, IntWritable> {

  private static final byte[] CF = "CF".getBytes();
  private static final byte[] ATTR1 = "attr1".getBytes();

  private final IntWritable one = new IntWritable(1);
  private final Text text = new Text();

  @Override
  protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
    String val = new String(value.getValue(CF, ATTR1));
    text.set(val);
    context.write(text, one);
  }
}
