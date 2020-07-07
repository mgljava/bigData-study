package com.github.mgljava.spark.sql

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkContext, sql}

/**
 * Spark SQL 查询Hive的数据
 */
object SparkSQLHiveTest {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "SparkSQLHiveTest")
    val hiveContext = new HiveContext(sc)
    val rows: sql.DataFrame = hiveContext.sql("SELECT name, age FROM users")
    val row = rows.first()
    println(row.getString(0))
  }
}
