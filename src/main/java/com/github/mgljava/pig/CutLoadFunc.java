package com.github.mgljava.pig;

import java.io.IOException;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.pig.LoadFunc;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * 自定义加载函数，该函数通过指定纯文本的列的区域定义字段
 */
public class CutLoadFunc extends LoadFunc {

  private final TupleFactory tupleFactory = TupleFactory.getInstance();
  private RecordReader recordReader;

  @Override
  public void setLocation(String location, Job job) throws IOException {
    FileInputFormat.setInputPaths(job, location);
  }

  @Override
  public TextInputFormat getInputFormat() throws IOException {
    return new TextInputFormat();
  }

  @Override
  public void prepareToRead(RecordReader reader, PigSplit split) throws IOException {
    this.recordReader = reader;
  }

  @Override
  public Tuple getNext() throws IOException {
    // TODO
    return null;
  }
}






















