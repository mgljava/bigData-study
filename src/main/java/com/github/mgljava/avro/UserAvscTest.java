package com.github.mgljava.avro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

public class UserAvscTest {
  public static void main(String[] args) throws IOException {
    // 获取Schema
    Schema.Parser parser = new Schema.Parser();
    Schema schema = parser.parse(new File("/home/sweep/IdeaProjects/bigData-study/src/main/avro/user.avsc"));
    System.out.println(schema);

    // 新建一个Avro实例
    GenericRecord data = new GenericData.Record(schema);
    data.put("name", "zhangsan");

    // 序列化输出
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    GenericDatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
    Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
    datumWriter.write(data, encoder);
    encoder.flush();
    outputStream.close();

    GenericDatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
    BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(outputStream.toByteArray(), null);
    GenericRecord genericRecord = reader.read(null, decoder);
    System.out.println(genericRecord.get("name"));
  }
}
