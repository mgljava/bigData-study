package com.github.mgljava.spark

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("Spark-World-Count")
    val sc = new SparkContext(conf)
    val line = sc.textFile("hdfs://192.168.56.102:9000/data/hive/sample.txt")
    val worlds = line.flatMap(_.split("\t"))
    val pairs = worlds.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)
    wordCounts.collect().foreach(wordNum => println(wordNum._1 + ":" + wordNum._2))
    sc.stop()
  }
}