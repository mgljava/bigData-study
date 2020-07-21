package com.github.mgljava.sparksql

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * Spark 操作Hive
 * 1. 读取Hive的数据
 *   将HDFS的配置拷贝到程序运行的目录，因为Hive底层依赖HDFS（core-site.xml）
 *   将hive的配置拷贝到程序运行的目录(Spark需要知道metastore的信息)(hive-site.xml)
 * 2. 写入Hive的数据
 *   将结果调用saveAsTable写入数据
 */
object SparkOnHiveTest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("SparkOnHiveTest").enableHiveSupport().getOrCreate()
    val studenInpath = "./data/spark/hive/student_info"
    val scoreInpath = "./data/spark/hive/score_info"
    spark.sql("use hive_study") // 使用spark数据库
    spark.sql("drop table if exists student_infos")
    spark.sql("create table student_infos(name string, age int) row format delimited fields terminated by ' '")
    spark.sql(s"load data local inpath '$studenInpath' into table student_infos")

    spark.sql("drop table if exists student_scores")
    spark.sql("create table student_scores(name string, score int) row format delimited fields terminated by ' '")
    spark.sql(s"load data local inpath '$scoreInpath' into table student_scores")

    spark.sql("drop table if exists good_student_infos")

    // 写入Hive数据库
    val dataFrame = spark.sql("select si.name,si.age,ss.score from student_infos si, student_scores ss where si.name = ss.name")
    dataFrame.write.mode(SaveMode.Overwrite).saveAsTable("good_student_infos")
  }
}
