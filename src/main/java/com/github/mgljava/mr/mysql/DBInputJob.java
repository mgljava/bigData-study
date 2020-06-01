package com.github.mgljava.mr.mysql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.db.DBConfiguration;
import org.apache.hadoop.mapred.lib.db.DBInputFormat;
import org.apache.hadoop.mapred.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class DBInputJob {

  public static void main(String[] args) throws Exception {

    Configuration conf = new Configuration(true);
    Job job = Job.getInstance(conf);
    // DistributedCache.addFileToClassPath(new Path("/lib/mysql-connector-java-8.0.13.jar"), conf);
    // HDFS文件路径
    job.addFileToClassPath(new Path("/lib/mysql-connector-java-8.0.13.jar"));

    // 设置输入输出类型
    job.setMapOutputKeyClass(LongWritable.class);
    job.setMapOutputValueClass(Text.class);
    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(Text.class);

    // 设置输入和输出为 DB
    job.setInputFormatClass(DBInputFormat.class);
    job.setOutputFormatClass(DBOutputFormat.class);

    // 配置MySQL的配置信息
    DBConfiguration.configureDB(conf, "com.mysql.jdbc.cj.Driver", "jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
    String[] fields = {"id", "name"};

    // 配置输入的表和读取器以及读取的字段
    DBInputFormat.setInput(job, StudentInfoRecord.class, "table1", null, "id", fields);

    // 设置输出的表和输出的字段
    DBOutputFormat.setOutput(job, "outputTable", "id", "name");

    // 设置Map和Reduce
    job.setMapperClass(DBInputMapper.class);
    job.setReducerClass(DBInputReducer.class);

    job.waitForCompletion(true);
  }
}
