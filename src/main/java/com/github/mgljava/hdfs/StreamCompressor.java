package com.github.mgljava.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * 该程序压缩从标准输入读取的数据,然后将其写到标准输出
 */
public class StreamCompressor {

  public static void main(String[] args) throws Exception {

    String codeClassName = "org.apache.hadoop.io.compress.GzipCodec";

    Class<?> codeClass = Class.forName(codeClassName);

    Configuration configuration = new Configuration();
    CompressionCodec compressionCodec = (CompressionCodec) ReflectionUtils.newInstance(codeClass, configuration);

    CompressionOutputStream outputStream = compressionCodec.createOutputStream(System.out);
    IOUtils.copyBytes(System.in, outputStream, 4096, false);
    outputStream.finish();
  }
}
