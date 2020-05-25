package com.github.mgljava.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class RegexPathFilter {

  public static void main(String[] args) throws Exception {
    FileSystem fileSystem = FileSystem.get(new Configuration(true));
    FileStatus[] fileStatuses = fileSystem.globStatus(new Path("/software/*"),
        new MyRegexExcludePathFilter("[^hadoop]"));
    for (FileStatus fileStatus : fileStatuses) {
      System.out.println(fileStatus.getPath().getName());
    }
  }
}


class MyRegexExcludePathFilter implements PathFilter {

  private final String regex;

  public MyRegexExcludePathFilter(String regex) {
    this.regex = regex;
  }

  @Override
  public boolean accept(Path path) {
    System.out.println("path.toString(): " + path.toString());
    return !path.toString().matches(regex);
  }
}