package com.github.mgljava.spark.fileformat

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.spark.{SparkConf, SparkContext}

object SequenceFileTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("SequenceFileTest")

    val sc = new SparkContext(conf)

    val input = sc.sequenceFile(path="./data/spark/files/sequence/part-00000", classOf[Text], classOf[IntWritable])
    input.foreach(item => println(item._1.toString + ":" + item._2.get()))
    // val data = sc.parallelize(List(("Panda", 3), ("Kay", 6), ("Snail", 2)))
    // data.saveAsSequenceFile("./data/spark/files/sequence")
  }
}
