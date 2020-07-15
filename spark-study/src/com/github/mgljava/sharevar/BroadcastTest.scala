package com.github.mgljava.sharevar

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 使用广播变量
 * 1. 不能将RDD广播出去，可以将RDD的结果广播出去
 * 2. 广播变量只能在Driver中定义，在Executor中使用，不能再Executor中改变广播变量的值
 */
object BroadcastTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("BroadcastTest")
    val sc = new SparkContext(conf)
    val rdd1 = sc.textFile("")
    // 此后就可以使用该广播变量 signPrefixes
    val signPrefixes = sc.broadcast(loadCallSignTable())
    val value = sc.broadcast(List("zhangsan", "list", "wangwu")) // 广播变量, 不能再executor中改变
    val rdd2 = rdd1.filter(line => value.value.contains(line))
    println(rdd2)
  }

  def loadCallSignTable(): String = {
    "hello"
  }
}
