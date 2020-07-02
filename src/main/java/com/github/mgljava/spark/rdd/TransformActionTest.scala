package com.github.mgljava.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

object TransformActionTest {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("TransformActionTest")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("./data/spark/words")

    /**
     * sample 随机抽样
     * true 代表有无放回抽样
     * 0.1抽样的比例
     *
     * seed:Long类型的种子，针对的同一批数据，只要种子相同，每次抽样的数据结果一样。
     */
    lines.sample(withReplacement = true, fraction = 0.1, seed = 1000)
      .foreach(println)

    println("-------count----------")
    println(lines.count()) // count

    println("-------take----------")
    lines.take(3).foreach(println(_)) // take

    println("-------filter----------")
    lines.filter(line => line.endsWith("hive")).foreach(println(_)) // filter
    val value = sc.parallelize(List(2, 3)).first()
    println(value)
    sc.stop()
  }
}
