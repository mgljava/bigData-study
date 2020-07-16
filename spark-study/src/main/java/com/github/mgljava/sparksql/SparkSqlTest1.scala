package com.github.mgljava.sparksql

import org.apache.spark.sql.SparkSession

/**
 * DataFrame 有数据、有列的schema
 * sqlContext 读取json文件加载成DataFrame，DataFrame会按照 ascii码排序
 * 写SQL查询出来的DataFrame会按照指定的列显示，不会排序
 */
object SparkSqlTest1 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master(master = "local").appName(name = "SparkSqlTest1").getOrCreate()

    val dataFrame = spark.read.json("./data/spark/sql/student.json")

    // dataFrame.printSchema()

    println("--------- dataFrame.show() ---------- ")
    dataFrame.show()
    println("--------- dataFrame.show() end---------- ")

    // dataFrame.select("name").show()

    // dataFrame.filter("age>18").show()

    // dataFrame.groupBy("age").count().show()

    /*
     * 将DataFrame注册成临时表
     * people 这张表不在内存中也不再磁盘中，相当于一个指针指向源文件，底层操作解析spark job 读取源文件
     */
    dataFrame.createOrReplaceTempView("people")
    val sqlDf = spark.sql("select * from people where age>18")
    sqlDf.show()
    import spark.implicits._
    dataFrame.select($"name", $"age" + 1).show()
  }
}
