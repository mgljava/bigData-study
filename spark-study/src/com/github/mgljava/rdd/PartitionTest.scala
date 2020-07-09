package com.github.mgljava.rdd

import org.apache.spark.{SparkConf, SparkContext}

object PartitionTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(("a", 2), ("b", 1), ("a", 3), ("c", 6), ("c", 10)))
    val rdd2 = rdd1.mapValues(item => {
      item * item
    })
    // rdd2.foreach(println)
    val rdd = sc.parallelize(List(
      "hello1", "hello2", "hello3", "hello4",
      "hello5", "hello6", "hello7", "hello8",
      "hello9", "hello10", "hello11", "hello12",
      "hello13", "hello14", "hello15", "hello16"
    ), 4)

    // index代表分区的下标，默认以0开始
    val result = rdd.mapPartitionsWithIndex((index, lines) => {
      // println("index：" + index)
      // lines.foreach(println(_))
      lines
    }, preservesPartitioning = true)
    result.count()

    // 重新分区
    val value = result.repartition(6)
    val repartition = value.mapPartitionsWithIndex((index, line) => {
      println("index：" + index)
      line.foreach(println(_))
      line
    })
    // repartition.count()
    val value1 = result.coalesce(30, shuffle = true)
    val coalesce = value1.mapPartitionsWithIndex((index, line) => {
      println("index：" + index)
      line.foreach(println(_))
      line
    })
    coalesce.count()
  }
}
