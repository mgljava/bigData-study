package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object NetworkWordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("NetworkWordCount").setMaster("local")

    val scc = new StreamingContext(conf, Seconds(1))
    val lines = scc.socketTextStream("localhost", 9999, StorageLevel.DISK_ONLY)
    println(lines)
    val wordCounts = lines.flatMap(_.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
    wordCounts.print()
    scc.start()
    scc.awaitTermination()
  }
}
