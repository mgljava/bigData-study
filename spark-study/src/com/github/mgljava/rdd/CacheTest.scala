package com.github.mgljava.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 持久化算子
 * cache() 默认将数据存在内存中
 * persist(StorageLevel.DISK_ONLY) 手动指定持久化级别(StorageLevel)
 * checkpoint 将数据存在磁盘中 sc.setCheckpointDir("./checkpoint") 设置checkpoint的路径，可以指定为HDFS路径
 */
object CacheTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("CacheTest")
    val sc = new SparkContext(conf)
    sc.setCheckpointDir("./data/checkpoint") // 设置数据的放置路径
    val lines = sc.textFile("./data/spark/words")

    // lines.cache()
    // lines.persist(StorageLevel.DISK_ONLY)
    // lines.persist() == cache()
    /*
        val startTime1 = System.currentTimeMillis();
        val result1 = lines.count()
        val endTime1 = System.currentTimeMillis()
        println("count = " + result1 + ", time = " + (endTime1- startTime1) + "ms")

        val startTime2 = System.currentTimeMillis();
        val result2 = lines.count()
        val endTime2 = System.currentTimeMillis();
        println("count = " + result2 + ", time = " + (endTime2- startTime2) + "ms")
    */

    // checkpoint
    lines.checkpoint()
    println(lines.count())
    sc.stop()
  }
}
