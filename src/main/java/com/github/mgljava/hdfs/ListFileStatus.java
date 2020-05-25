package com.github.mgljava.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class ListFileStatus {

  public static void main(String[] args) throws Exception {

    FileSystem fileSystem = FileSystem.get(new Configuration());

    FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/software"),
        path -> path.getName().endsWith("gz"));

    for (FileStatus fileStatus : fileStatuses) {
      System.out.println(fileStatus.getReplication());
      System.out.println(fileStatus.getBlockSize());
      System.out.println(fileStatus.getPath());
      System.out.println("-----------------");
    }
  }
}
