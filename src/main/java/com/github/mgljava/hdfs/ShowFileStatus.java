package com.github.mgljava.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

// 查看文件状态
public class ShowFileStatus {

  public static void main(String[] args) throws Exception {
    FileSystem fileSystem = FileSystem.get(new Configuration());

    FileStatus fileStatus = fileSystem.getFileStatus(new Path("/software/hadoop-2.10.0.tar.gz"));

    System.out.println(fileStatus);
    System.out.println(fileStatus.isDirectory());
    System.out.println(fileStatus.getBlockSize());
    System.out.println(fileStatus.getReplication());
  }
}
