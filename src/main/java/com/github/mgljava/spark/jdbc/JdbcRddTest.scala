package com.github.mgljava.spark.jdbc

import java.sql.{DriverManager, ResultSet}

import org.apache.spark.SparkContext

/**
 * spark操作数据库
 *
 * CREATE TABLE `my_test` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `name` varchar(255) DEFAULT NULL,
 * `email` varchar(255) DEFAULT NULL,
 * `age` int(11) DEFAULT NULL,
 * `address` varchar(155) DEFAULT NULL,
 * PRIMARY KEY (`id`),
 * KEY `idx_name_email` (`name`,`email`),
 * KEY `idx_add` (`address`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
 */
object JdbcRddTest {

  def main(args: Array[String]): Unit = {
    def createConnection() = {
      println("createConnection---------")
      Class.forName("com.mysql.cj.jdbc.Driver")
      DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql_study", "root", "123456")
    }

    def extractValues(r: ResultSet): (Int, String, String, Int, String) = {
      // id name email age address
      println("extractValues=========")
      Tuple5(r.getInt(1), r.getString(2), r.getString(3), r.getInt(4), r.getString(5))
    }
    val sc = new SparkContext("local", "JdbcRddTest")
    val result = new org.apache.spark.rdd.JdbcRDD(
      sc = sc, createConnection, sql = "SELECT id,name,email,age,address FROM my_test where ? <=id and id <=?", lowerBound = 1, upperBound = 2, numPartitions = 1, mapRow = extractValues)
    println(result.count())
  }
}
