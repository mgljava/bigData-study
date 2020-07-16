package com.github.mgljava.sparksql

import org.apache.spark.sql.SparkSession

/**
 * 读取嵌套的JSON文件
 */
object ReadNestJson {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ReadNestJson").master("local").getOrCreate()

    // val dataFrame = spark.read.format("json").load("./data/spark/sql/studentNest.json")
    val dataFrame = spark.read.json("./data/spark/sql/studentNest.json")
    // 注册为临时表或视图
    dataFrame.createOrReplaceTempView("person")
    val result = spark.sql("select name,address.home,age,address.company from person")
    result.show()
  }
}
