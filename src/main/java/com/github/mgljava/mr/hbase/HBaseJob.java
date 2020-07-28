package com.github.mgljava.mr.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;

public class HBaseJob {

  public static void main(String[] args) throws Exception {
    String sourceTable = "原始表";
    String targetTable = "目标表";

    Configuration configuration = HBaseConfiguration.create();
    Job job = Job.getInstance(configuration);

    Scan scan = new Scan();
    scan.setCaching(500);
    scan.setCacheBlocks(false);

    TableMapReduceUtil.initTableMapperJob(sourceTable.getBytes(), scan, HBaseMapper.class, null, null, job);
    TableMapReduceUtil.initTableReducerJob(targetTable, HBaseReducer.class, job);
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(Put.class);

    job.waitForCompletion(true);
  }
}
