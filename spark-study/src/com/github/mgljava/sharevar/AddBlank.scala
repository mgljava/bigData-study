package com.github.mgljava.sharevar

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 在 Scala 中累加空行
 * 1. 累加器在Driver端定义并初始化
 */
object AddBlank {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("AddBlank")
    val sc = new SparkContext(conf)

    val file = sc.textFile("./data/spark/files/shareva.txt")
    val blankLines = sc.accumulator(0) // 空行
    val valueLines = sc.accumulator(0) // 有值的行
    val callSigns = file.flatMap(line => {
      if (line == "") {
        blankLines += 1
        println("blankLines: " + blankLines)
        ""
      } else {
        valueLines += 1
        line.split(" ")
      }
    })
    callSigns.foreach(println)
    println("-------------")
    println("Blank lines: " + blankLines.value)
    println("valueLines lines: " + valueLines.value)
  }
}
