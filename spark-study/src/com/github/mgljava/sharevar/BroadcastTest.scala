package com.github.mgljava.sharevar

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 使用广播变量查询国家
 */
object BroadcastTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("BroadcastTest")
    val sc = new SparkContext(conf)
    val signPrefixes = sc.broadcast(loadCallSignTable())
    // 此后就可以使用该广播变量 signPrefixes
  }

  def loadCallSignTable(): String = {
    "hello"
  }
}
