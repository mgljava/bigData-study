package com.github.mgljava.pvuv

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 统计各个网站的访问次数
 */
object PvUvTest {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("PvUvTest")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./data/*/pvuvdata")
    lines.cache()
    val webSites = lines.map(line => {
      val webSite = line.split("\t")(5)
      webSite
    })
    val value = webSites.map(webSite => {
      (webSite, 1)
    })
    val result = value.countByKey()
    result.foreach(line => {
      println("website: " + line._1 + ", number: " + line._2)
    })
    println("count: " + lines.count())
    val sum = result.values.sum
    println("sum: " + sum)
  }
}
