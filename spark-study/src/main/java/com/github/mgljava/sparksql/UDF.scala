package com.github.mgljava.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

// UDF 用户自定义函数, 进来一条出去一条
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

    // 注册函数,多个参数就传几个 n和n2
    spark.udf.register("combination", (n:String, n2:String) => {
      "~" + n + "~" + n2
    })

    // 直接在SQL语句中使用定义的函数
    spark.sql("select name,combination(name,name) as cname, STRLEN(name) as length from students sort by length desc").show()
  }
}
