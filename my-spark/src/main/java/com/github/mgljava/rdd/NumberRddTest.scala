package com.github.mgljava.rdd

import org.apache.spark.util.StatCounter
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 数值RDD的基本操作
 */
object NumberRddTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("NumberRddTest").setMaster("local")
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(List(-3, -9, 22, 19, 1, 4, 6, 7, 22, 4, 5))
    // StatCounter 基本操作
    val counter: StatCounter = rdd.stats()
    println("max: " + counter.max)

    // 移除rdd中的异常元素，如果元素小于0或者大于10都是一场元素
    rdd.filter(x => !(x > 10 || x < 0)).foreach(println)
  }
}
