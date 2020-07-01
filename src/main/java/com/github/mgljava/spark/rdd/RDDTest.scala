package com.github.mgljava.spark.rdd

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD 基本操作
 */
object RDDTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDTest").setMaster("local")
    val sc = new SparkContext(conf)
    val v1 = sc.parallelize(List(1, 2, 3, 2, 3, 1, 8, 19))
    val v2 = sc.parallelize(List(5, 6, 7))
    val value = v1.cartesian(v2)
    // value.foreach(item => {
    // println(item._1 + ":" + item._2)
    // })
    val i = v1.reduce((a: Int, b: Int) => a + b)
    println(i)
    val i1 = v2.fold(5)((a, b) => a + b)
    println(i1)

    /**
     * aggregate
     */
    val tuple = v1.aggregate((0, 0))(
      (acc, value) => (acc._1 + value, acc._2 + 1),
      (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2))
    val i2 = tuple._1 / tuple._2
    println(i2)

    v1.top(2).foreach(println)
    println("-----take------")
    v1.takeSample(withReplacement = true, num = 2).foreach(println)
    v1.foreach(println)

    println("-----cache------")
    val value1 = sc.parallelize(List(2, 4, 6))
    value1.persist(StorageLevel.DISK_ONLY)
    println(value1.count())
    println(value1.collect().mkString(";"))
  }
}
