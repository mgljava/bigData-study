package com.github.mgljava.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object InterfaceView {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[4]").appName("InterfaceView").getOrCreate()
    val dataFrame = spark.read.json("/Users/monk/Desktop/Work/octopus_log/info/result2/*")
    val value: RDD[String] = dataFrame.rdd
      .map(log => log.get(log.fieldIndex("message")).toString)
      .filter(log => log.contains("PDD request params"))
      .map(log => log.substring(20))
      .map(log => log.replace("type", "methodName"))
    val frame = spark.read.json(value)
    frame.createTempView("message")
    spark.sql("select methodName,count(methodName) as total from message group by methodName").show(false)
  }
}
