package com.github.mgljava.mr.ncdc;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

public class MaxTemperatureMapperTest {

  /**
   * 测试Mapper的输出结果
   *
   * @throws IOException
   */
  @Test
  public void processMapperValidRecord() throws IOException {
    Text value = new Text("0029227070999991901101220004+62167+" +
        "030650FM-12+010299999V0202701N002119999999N0000001N9+00061+" +
        "99999102111ADDGF100991999999999999999999");
    new MapDriver<LongWritable, Text, Text, IntWritable>()
        .withMapper(new MaxTemperatureMapper())
        .withInput(new LongWritable(1), value)
        .withOutput(new Text("1901"), new IntWritable(6))
        .runTest();
  }

  @Test
  public void processReduceValidRecord() throws IOException {
    new ReduceDriver<Text, IntWritable, Text, IntWritable>()
        .withReducer(new MaxTemperatureReduce())
        .withInput(new Text("1901"), ImmutableList.of(new IntWritable(2), new IntWritable(10),
            new IntWritable(299)))
        .withOutput(new Text("1901"), new IntWritable(299))
        .runTest();
  }
}