package com.github.mgljava.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

object JoinTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("JoinTest")
    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(List(("hadoop", "a"), ("hive", "b"), ("pig", "c"), ("spark", "d"), ("mysql", "e")))
    val rdd2 = sc.parallelize(List(("hadoop", 100), ("hive", 200), ("pig", 300), ("spark", 400), ("jdbc", 500)))

    // join 按照RDD的key去关联
    // rdd1.join(rdd2).foreach(println)

    // leftOuterJoin 以左为主
    // rdd1.leftOuterJoin(rdd2).foreach(println)

    // rightOuterJoin 以右为主
    rdd1.rightOuterJoin(rdd2).foreach(println)
  }
}
