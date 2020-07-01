package com.github.mgljava.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

object LogFileRdd {

  def main(args: Array[String]): Unit = {
    val filePath = "./data/spark/logFile.log"
    val conf = new SparkConf().setMaster("local").setAppName("LogFileRdd")
    val sc = new SparkContext(conf)

    val lines = sc.textFile(filePath)
    val errorMsg = lines.filter(line => line.contains("error"))
    val warnMsg = lines.filter(line => line.contains("warn"))
    val result = errorMsg.union(warnMsg)

    println(result.count())
    for (elem <- result.take(4)) {
      println(elem)
    }

    sc.stop()
  }
}
