package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * 由于Streaming的应用需要 7*24 小时执行，所以需要保证Driver HA
 *
 * 主要是通过 checkpoint 来存储和恢复
 */
object DriverHA {
  val ckDir = "./data/spark/streaming/driverHA"
  def main(args: Array[String]): Unit = {
    // 先去checkpoint中恢复StreamingContext，如果不能恢复就去创建新的 StreamingContext
    val ssc: StreamingContext = StreamingContext.getOrCreate(ckDir, createStreamingContext)
    ssc.start()
    ssc.awaitTermination()

    ssc.stop()
  }

  def createStreamingContext():StreamingContext = {
    println("========Create new StreamingContext=====")
    val conf = new SparkConf().setMaster("local").setAppName("DriverHA")
    val ssc: StreamingContext = new StreamingContext(conf, Durations.seconds(5))
    ssc.sparkContext.setLogLevel("Error")

    /**
     * 默认 checkpoint 存储下列信息
     * 1. 配置信息
     * 2. DStream操作逻辑
     * 3. job的执行速度
     * 4. offset
     */
    ssc.checkpoint(ckDir)
    val lines: DStream[String] = ssc.textFileStream("./data/spark/streaming/copyFile")
    val result: DStream[(String, Int)] = lines.flatMap(_.trim.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)

    result.print()

    ssc
  }
}
