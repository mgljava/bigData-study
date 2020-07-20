package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext}

/**
 * SparkStreaming窗口操作
 *
 * reduceByKeyAndWindow
 *
 * 每隔窗口滑动间隔时间 计算 窗口长度内的数据，按照指定的方式处理
 */
object WindowOperator {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WindowOperator").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))

    // 设置日志级别
    ssc.sparkContext.setLogLevel("Error")

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    val words: DStream[String] = lines.flatMap(_.split(" "))
    val pairWords: DStream[(String, Int)] = words.map((_, 1))


    // 代表每隔5秒计算过去15秒的数据，数据操作的逻辑封装在 (v1: Int, v2: Int) => v1 + v2
    /**
     * 窗口操作
     * 窗口长度：window.length  -- Wl
     * 窗口滑动间隔：Window sliding interval -- si
     *
     * 滑动间隔和窗口长度必须是 batchInterval的整数倍
     */
    val windowResult: DStream[(String, Int)] =
      pairWords.reduceByKeyAndWindow((v1: Int, v2: Int) => v1 + v2, Durations.seconds(15), Durations.seconds(5))

    /**
     * 窗口操作优化机制,必须设置checkpoint
     * 1. 加上新进来的批次
     * 2. 减去被移除的批次
     */
    ssc.checkpoint("./data/spark/streaming/WindowOperator")
    val windowResult2 = pairWords.reduceByKeyAndWindow(
      // 加上新进来的批次
      (v1: Int, v2: Int) => {
        v1 + v2
      },
      // 减去被移除的批次
      (v1: Int, v2: Int) => {
        v1 - v2
      },
      Durations.seconds(15),
      Durations.seconds(5)
    )

    // 自定义处理逻辑, 每隔5秒处理前15秒的数据，返回DStream
    val value: DStream[(String, Int)] = pairWords.window(Durations.seconds(15), Durations.seconds(5))

    windowResult.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
