package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * http://localhost:4040/streaming/ WebUI 界面
 *
 * 监听Socket连接的单词统计程序
 */
object NetworkWordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[2]")

    // val scc = new StreamingContext(conf, Seconds(1))
    val scc = new StreamingContext(conf, Durations.seconds(5))
    scc.sparkContext.setLogLevel("Error")
    val lines = scc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_ONLY)
    println(lines)
    val wordCounts = lines.flatMap(_.split(" "))
      .map(word => {
        println("word: " + word)
        (word, 1)
      })
      .reduceByKey(_ + _)
    wordCounts.print()
    scc.start()
    scc.awaitTermination()
  }
}
