package com.github.mgljava.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext

object SparkSQLJsonTest {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "SparkSQLHiveTest")
    /*val hiveContext = new HiveContext(sc)
    val users = hiveContext.jsonFile("./data/spark/sql/user.json")
    users.registerTempTable("users")
    val result = hiveContext.sql("SELECT user.name,text FROM users")
    result.foreach(item => {
      println(item)
    })*/
    val sqlContext = new SQLContext(sc)
    val result = sqlContext.read.json("./data/spark/sql/user.json")
    println(result)
  }
}
