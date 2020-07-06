package com.github.mgljava.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

object PartitionTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(("a", 2), ("b", 1), ("a", 3), ("c", 6), ("c", 10)))
    val rdd2 = rdd1.mapValues(item => {
      item * item
    })
    rdd2.foreach(println)
  }
}
