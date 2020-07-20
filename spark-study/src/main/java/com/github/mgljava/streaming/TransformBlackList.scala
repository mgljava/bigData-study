package com.github.mgljava.streaming

import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * 当输入的数据中包含广播变量广播的名字时进行过滤不显示
 */
object TransformBlackList {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("TransformBlackList")
    val ssc = new StreamingContext(conf, Durations.seconds(5))

    // 广播黑名单
    val blackList: Broadcast[List[String]] = ssc.sparkContext.broadcast(List("zhangsan", "lisi"))

    val lines = ssc.socketTextStream("localhost", 9999)

    // 从实时数据中发现第二位是黑名单人员进行过滤掉
    val pairLines: DStream[(String, String)] = lines.map(line => {
      (line.split(" ")(1), line)
    })

    /**
     * transform 算子可以拿到 DStream中的RDD，对RDD使用RDD的算子操作，但是最后要返回RDD，返回的RDD又被封装到了一个DStream
     * transform中拿到的RDD的算子外，代码是在Driver端执行的，可以做到改变广播变量
     */
    val result: DStream[String] = pairLines.transform((pairLine) => {
      println("++++++ Driver Code +++++")
      val filterRDD: RDD[(String, String)] = pairLine.filter(tp => {
        val nameList = blackList.value
        !nameList.contains(tp._1)
      })

      val returnRDD: RDD[String] = filterRDD.map(tp => tp._2)
      returnRDD
    })

    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
