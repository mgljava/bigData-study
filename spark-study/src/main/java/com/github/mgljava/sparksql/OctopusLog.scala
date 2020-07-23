package com.github.mgljava.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object OctopusLog {
  def main(args: Array[String]): Unit = {
    logParse()
  }

  def logParse(): Unit = {
    val logPath = "/Users/monk/Desktop/Work/octopus_log/*"

    val spark: SparkSession = SparkSession.builder().appName("OctopusLog").master("local").getOrCreate()

    val logs: DataFrame = spark.read.json(logPath)
    logs.show(3)
    logs.rdd
      .map(log => log.get(log.fieldIndex("message")).toString)
      .map(log => {
        if (log.contains("YouZan target sku")) {
          val strings = log.split("itemNo")
          if (strings.size > 1) {
            (strings(0), 1)
          } else {
            (log, 1)
          }
        }else if (log.contains("ailed. storageNo")) {
          (log.split("storageNo")(0), 1)
        }else if(log.contains("Not found HiSHOP SKU information")) {
          (log.split(", outerSkuId")(0), 1)
        } else {
          (log, 1)
        }
      }
      )
      .reduceByKey((_: Int) + (_: Int))
      .sortBy(_._2, ascending = false)
      .foreach(println)
    println("logs.count(): " + logs.count())
  }
}
