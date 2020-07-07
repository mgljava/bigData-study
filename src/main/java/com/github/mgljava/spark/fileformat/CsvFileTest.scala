package com.github.mgljava.spark.fileformat

import java.io.StringReader

import au.com.bytecode.opencsv.CSVReader
import org.apache.spark.{SparkConf, SparkContext}

object CsvFileTest {

  case class CsvPerson(name: String, favoriteAnimal: String)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("CsvFileTest")

    val sc = new SparkContext(conf)

    // 一行一行的读取csv文件
    val rdd = sc.textFile("*.csv")
    rdd.map(line => {
      val reader = new CSVReader(new StringReader(line))
      reader.readNext();
    }).collect()

    // 读取整个csv文件
    val input = sc.wholeTextFiles("*.csv")
    input.map(line => {
      val reader = new CSVReader(new StringReader(line._2))
      reader.readAll()
    })
  }
}
