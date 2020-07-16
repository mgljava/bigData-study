package com.github.mgljava.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * pipeline 处理模式为一条一条数据的处理，然后落地磁盘
 */
object RDDPipelineTest {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("RDDPipelineTest")
    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(List("zhangsan", "lisi", "wangwu"))
    val rdd2 = rdd1.map(name => {
      println("map: " + name)
      name + "~"
    })
    val result = rdd2.filter(name => {
      println("filter: " + name)
      true
    })

    result.foreach(println)
    println("finish")
  }
}
