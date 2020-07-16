package com.github.mgljava.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object SparkSQLJsonTest {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "SparkSQLJsonTest")
    /*val hiveContext = new HiveContext(sc)
    val users = hiveContext.jsonFile("./data/spark/sql/user.json")
    users.registerTempTable("users")
    val result = hiveContext.sql("SELECT user.name,text FROM users")
    result.foreach(item => {
      println(item)
    })*/
    val sqlContext = new SQLContext(sc)
    val result = sqlContext.jsonFile("./data/spark/sql/user.json")
    result.foreach(row => {
      println(row.get(1))
    })
  }
}
