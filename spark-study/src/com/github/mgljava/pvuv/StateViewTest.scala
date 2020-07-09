package com.github.mgljava.pvuv

import org.apache.spark.{SparkConf, SparkContext}

// 每个网址每个地区的访问量是多少
object StateViewTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("StateViewTest")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./data/pvuvdata")
    lines.cache()
    lines.map(line => {
      val strings = line.split("\t")
      ((strings(5), strings(1)), 1)
    }).groupByKey().map(item => {
      (item._1, item._2.size)
    }).sortByKey().foreach(println)
  }
}
