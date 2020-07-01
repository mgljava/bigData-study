package com.github.mgljava.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD 基本操作
 */
object RDDTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDTest").setMaster("local")
    val sc = new SparkContext(conf)
    val v1 = sc.parallelize(List(1, 2, 3))
    val v2 = sc.parallelize(List(5, 6, 7))
    val value = v1.cartesian(v2)
    value.foreach(item => {
      println(item._1 + ":" + item._2)
    })
  }
}
