package com.github.mgljava.mr.hbase;

import java.io.IOException;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * 对Mapper阶段输出的结果进行统计
 */
public class HBaseReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {

  private static final byte[] CF = "CF".getBytes();
  private static final byte[] COUNT = "count".getBytes();

  @Override
  protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    int sum = 0;
    for (IntWritable val : values) {
      sum += val.get();
    }
    Put put = new Put(Bytes.toBytes(key.toString()));
    put.addColumn(CF, COUNT, Bytes.toBytes(sum));

    context.write(null, put);
  }
}
