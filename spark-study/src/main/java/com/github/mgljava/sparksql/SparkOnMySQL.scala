package com.github.mgljava.sparksql

import java.util.Properties

import org.apache.spark.sql.SparkSession

/**
 * SparkSQL操作MySQL
 *
 *
 */
object SparkOnMySQL {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local").appName("SparkOnMySQL").getOrCreate()

    // 方式1
    /*val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/fund")
      .option("dbtable", "funds")
      .option("user", "root")
      .option("password", "123456")
      .load()*/
    // 方式2
    val connectionProperties = new Properties()
    connectionProperties.put("user", "root")
    connectionProperties.put("password", "123456")
    val jdbcDF = spark.read
      .jdbc("jdbc:mysql://localhost:3306/fund", "funds", connectionProperties)
    jdbcDF.show()
    jdbcDF.explain(true) // 打印执行计划
    println("-------")
    val l = jdbcDF.rdd
      .map(item => item.get(4))
      .collect()
    var sum = 0
    for (i <- l) {
      sum += Integer.parseInt(i.toString)
    }
    println(sum)
  }
}
