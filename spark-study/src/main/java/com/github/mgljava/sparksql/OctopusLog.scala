package com.github.mgljava.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object OctopusLog {
  def main(args: Array[String]): Unit = {
    val logPath = "/Users/monk/Desktop/Work/octopus_log/*"

    val spark: SparkSession = SparkSession.builder().appName("OctopusLog").master("local").getOrCreate()

    val logs: DataFrame = spark.read.json(logPath)
    logs.show(3)
    logs.rdd
      .map(log => log.get(log.fieldIndex("message")).toString)
      .map((log: String) => (log, 1))
      .reduceByKey((_: Int) + (_: Int))
      .sortBy(_._2, ascending = false)
      .foreach(println)
  }
}
