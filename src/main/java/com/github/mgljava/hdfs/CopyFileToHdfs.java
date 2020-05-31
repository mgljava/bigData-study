package com.github.mgljava.hdfs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

// 将本地文件复制到Hadoop系统
public class CopyFileToHdfs {

  public static void main(String[] args) throws Exception {
    String localSrc = "/home/sweep/Download/remarkable_1.87_all.deb";
    String dst = "/software/remarkable_1.87_all.deb";

    InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

    Configuration configuration = new Configuration();
    FileSystem fs = FileSystem.get(URI.create(dst), configuration);
    OutputStream outputStream = fs.create(new Path(dst), () -> System.out.println("."));

    IOUtils.copyBytes(in, outputStream, 4096, true);
  }
}
