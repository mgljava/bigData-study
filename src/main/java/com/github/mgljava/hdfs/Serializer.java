package com.github.mgljava.hdfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.StringUtils;

public class Serializer {

  public static void main(String[] args) throws IOException {
    // 序列化测试
    IntWritable writable = new IntWritable(163);
    byte[] serialize = serialize(writable);
    System.out.println(serialize.length); // 输出4  因为一个整型占4个字节
    System.out.println(StringUtils.byteToHexString(serialize)); // 0000007b

    // 反序列化测试
    IntWritable intWritable = new IntWritable();
    deserialize(intWritable, serialize);
    System.out.println(intWritable.get());
  }

  // 序列化
  public static byte[] serialize(Writable writable) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
    writable.write(dataOutputStream);
    dataOutputStream.close();
    return outputStream.toByteArray();
  }

  // 反序列化
  public static byte[] deserialize(Writable writable, byte[] bytes) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

    DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
    writable.readFields(dataInputStream);
    dataInputStream.close();
    return bytes;
  }
}
