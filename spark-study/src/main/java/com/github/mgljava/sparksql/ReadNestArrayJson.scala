package com.github.mgljava.sparksql

import org.apache.spark.sql.SparkSession

/**
 * 读取嵌套的JSON文件,包含数组
 */
object ReadNestArrayJson {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ReadNestArrayJson").master("local").getOrCreate()

    val dataFrame = spark.read.json("./data/spark/sql/studentNestArray.json")
    // dataFrame.show(truncate = false) // truncate = false 代表显示的时候全部显示，不省略
    import spark.implicits._
    import org.apache.spark.sql.functions._
    // explode 函数 可以将数据扁平化
    val allDataFrame = dataFrame.select($"name", $"age", explode($"computer")).toDF("name", "age", "allComputer")

    // allDataFrame.show(false)

    val result = allDataFrame.select($"name", $"age",
      $"allComputer.cpu" as "cpu",
      $"allComputer.memory" as "memory"
    )
    result.show()
  }
}
