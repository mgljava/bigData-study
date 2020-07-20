package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * 监控目录。，并且将结果写出到目录中
 */
object SaveAsTextFile {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SaveAsTextFile").setMaster("local")

    val ssc = new StreamingContext(conf, Durations.seconds(5))

    /**
     * 监控 copyFile 目录，如果有数据，那么就执行操作
     *
     * SparkStreaming监控一个目录数据时
     *   1. 这个目录下已经存在的文件不会被监控到，可以监控增加的文件
     *   2. 增加的文件必须是原子性产生
     */
    val lines: DStream[String] = ssc.textFileStream("./data/spark/streaming/copyFile")
    val result: DStream[(String, Int)] = lines.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
    result.saveAsTextFiles("./data/spark/streaming/resultFile", "txt")

    ssc.start()
    ssc.awaitTermination()
  }
}
