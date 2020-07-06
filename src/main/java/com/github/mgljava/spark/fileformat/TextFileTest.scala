package com.github.mgljava.spark.fileformat

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 文本文件格式
 */
object TextFileTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("TextFileTest")

    val sc = new SparkContext(conf)

    /**
     * 传递目录作为参数，这样它会把各部分都读取到 RDD 中
     */
    /*val words = sc.textFile("./data/spark/files")
    words.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey((a, b) => a + b)
      .foreach(println)*/

    /**
     * wholeTextFiles
     *  1. 键是输入文件的文件名。
     *  2. 值是文件中的值
     *  3. 可以使用通配符：(如 part-*.txt)
     */
    val wholeWords = sc.wholeTextFiles("./data/spark/files/input/file*")
    // val wholeWords = sc.wholeTextFiles("./data/spark/files/input/*1")
    val result = wholeWords.mapValues(line => {
      val nums = line.split(" ").flatten.map(x => x.toDouble)
      // val nums = line.split(" ").map(x => x.toDouble)
      val result = nums.sum / nums.length.toDouble
      result
    })
    result.saveAsTextFile("./data/spark/files/output")
    sc.stop()
  }
}
