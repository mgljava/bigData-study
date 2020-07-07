package com.github.mgljava.spark.fileformat

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat
import org.apache.spark.SparkContext

/**
 * 读取hadoop文件
 */
object HadoopFileTest {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "HadoopFileTest")
    val result = sc.newAPIHadoopFile("", classOf[KeyValueTextInputFormat], classOf[Text], classOf[Text])
  }
}
