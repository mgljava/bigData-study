package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 通过传递状态来统计多个批次的数据进行WordCount
 */
object UpdateStateByKey {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("UpdateStateByKey")
    val ssc = new StreamingContext(conf, Seconds(5))
    // 设置日志级别
    ssc.sparkContext.setLogLevel("Error")

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
    val words: DStream[String] = lines.flatMap(_.split(" "))
    val pairWords: DStream[(String, Int)] = words.map((_, 1))

    // 需要设置checkpoint
    /**
     * 根据Key更新状态，需要设置checkpoint来保存状态
     * 默认key的状态在内存中有一份，在checkpoint目录中也有一份
     *
     * 多久会将内存中的数据（每个key对应的状态）持久化到磁盘呢？
     *
     * 如果你的batchInterval小于10s，那么10s会将内存中的数据写入到磁盘一份
     * 如果batchInterval大于10s，那么就以batchInterval为准
     *
     * 这样做的目的是频繁的写HDFS
     */
    ssc.checkpoint("./data/spark/streaming/UpdateStateByKey")

    /**
     * currentValue: 当前批次某个key所对应的状态值
     * preValue:
     */
    val result: DStream[(String, Int)] = pairWords.updateStateByKey((currentValue: Seq[Int], preValue: Option[Int]) => {
      var total = 0
      if (preValue.isDefined) {
        total += preValue.get
      }
      for (value <- currentValue) {
        total += value
      }
      Option(total)
    })
    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
