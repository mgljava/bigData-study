package com.github.mgljava.fileformat

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext

case class Log(thread: String, level: String, loggerName: String, message: String, errorMsg: String)

object OctopusLog {
  def main(args: Array[String]): Unit = {
    val input = "/Users/monk/Desktop/log/input/error*"
    val sc = new SparkContext(master = "local", appName = "OctopusLog")
    val logs = sc.wholeTextFiles(input)
    val output = "/Users/monk/Desktop/log/output"
    val fileSystem = FileSystem.get(sc.hadoopConfiguration)
    if (fileSystem.exists(new Path(output))) {
      fileSystem.delete(new Path(output), true)
    }
    logs.map(item => {
      item._2.split("\n")
    }).flatMap(line => line).map(log => {
      val mapper = new ObjectMapper()
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      mapper.registerModule(DefaultScalaModule)
      mapper.readValue(log, classOf[Log])
    }).map(line => {
      val strings = line.message.split(":")
      Tuple2(strings(0), 1)
    }).reduceByKey((a, b) => a + b)
      .sortBy(item => item._2, ascending = false)
      .saveAsTextFile(output)
  }
}
