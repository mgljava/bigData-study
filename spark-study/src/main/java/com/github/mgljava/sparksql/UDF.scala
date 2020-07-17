package com.github.mgljava.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

// UDF 用户自定义函数
object UDF {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("UDF").getOrCreate()

    val nameList = List[String]("zhangsan", "list", "wangwu", "zhaoliu", "tianqi")

    import spark.implicits._
    val nameDF: DataFrame = nameList.toDF("name")
    nameDF.show()
    nameDF.createOrReplaceTempView("students")

    spark.udf.register("STRLEN", (n:String) => {
      n.length
    })

    spark.sql("select name, STRLEN(name) as length from students sort by length desc").show()
  }
}
