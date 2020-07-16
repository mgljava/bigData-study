package com.github.mgljava.sparksql

import org.apache.spark.sql.SparkSession

object SparkSqlTest1 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master(master = "local").appName(name = "SparkSqlTest1").getOrCreate()

    val dataFrame = spark.read.json("./data/spark/sql/student.json")

    // dataFrame.printSchema()

    println("--------- dataFrame.show() ---------- ")
    dataFrame.show()
    println("--------- dataFrame.show() end---------- ")

    // dataFrame.select("name").show()

    // dataFrame.filter("age>18").show()

    // dataFrame.groupBy("age").count().show()
    dataFrame.createOrReplaceTempView("people")
    val sqlDf = spark.sql("select * from people where age>18")
    sqlDf.show()
  }
}
