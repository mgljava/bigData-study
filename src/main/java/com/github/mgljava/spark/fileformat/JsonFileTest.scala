package com.github.mgljava.spark.fileformat

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.spark.{SparkConf, SparkContext}

case class Person1(name: String, age: Int, address: String)

object JsonFileTest {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("JsonFileTest").setMaster("local")
    val sc = new SparkContext(conf)
    val jsonFiles = sc.textFile("./data/spark/files/json/jsonfile")
    jsonFiles.map(line => {
      val mapper = new ObjectMapper() with ScalaObjectMapper
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      mapper.registerModule(DefaultScalaModule)
      mapper.readValue(line, classOf[Person1])
    })
      .filter(person => person.age > 10)
      .saveAsTextFile("./data/spark/files/json/output")
  }
}
