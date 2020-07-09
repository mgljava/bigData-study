package com.github.mgljava.pvuv

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Unique Visitor 统计,根据用户id去重
 */
object UniqueViewTest {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("UniqueViewTest")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./data/pvuvdata")
    lines.cache()
    lines.map(line => {
      val strings = line.split("\t")
      (strings(5), strings(4))
    }).distinct().map(line => {
      (line._1, 1)
    }).reduceByKey((a, b) => a+b).foreach(println)
  }
}
