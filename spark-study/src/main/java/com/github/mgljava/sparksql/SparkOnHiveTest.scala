package com.github.mgljava.sparksql

import org.apache.spark.sql.{SaveMode, SparkSession}

// Spark 读取Hive的数据
object SparkOnHiveTest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkOnHiveTest").enableHiveSupport().getOrCreate()
    val studenInpath = ""
    val scoreInpath = ""
    spark.sql("use spark") // 使用spark数据库
    spark.sql("drop table if exists student_infos")
    spark.sql("create table student_infos(name string, age int) row format delimited fields terminated by '\t'")
    spark.sql(s"load data local inpath '$studenInpath' into table student_infos")

    spark.sql("drop table if exists student_scores")
    spark.sql("create table student_scores(name string, score int row format delimited fields terminated by '\t')")
    spark.sql(s"load data local inpath '$scoreInpath' into table student_scores")

    spark.sql("select si.name,si.age,ss.score from student_infos si, student_scores ss where si.name = ss.name")

    // 写入Hive数据库
    val dataFrame = spark.sql("drop table if exists goo_student_infos")
    dataFrame.write.mode(SaveMode.Overwrite).saveAsTable("goo_student_infos")
  }
}
