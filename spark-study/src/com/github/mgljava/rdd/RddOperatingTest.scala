package com.github.mgljava.rdd

import org.apache.spark
import org.apache.spark.{SparkConf, SparkContext}

object RddOperatingTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("RddOperatingTest")
    val sc = new SparkContext(conf)

    /*sc.parallelize(List(("a", 2), ("b", 1), ("c", 2), ("a", 4))).groupByKey().foreach(item => {
      println(item._1 + ": " + item._2.sum)
    })*/

    val rdd1 = sc.parallelize(List(("a", 2), ("b", 1), ("c", 2), ("a", 4)))
    val rdd2 = sc.parallelize(List(("a", 20), ("b", 911), ("c", 2), ("d", 4)))
   // rdd1.join(rdd2).foreach(println)
   //  rdd1.countByKey().foreach(item=>println(item))
   val rdd3 = rdd1.partitionBy(new spark.HashPartitioner(2))
    println(rdd3.partitioner)
  }
}
