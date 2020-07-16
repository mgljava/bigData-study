package com.github.mgljava.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class DataFrameRDD {

  public static void main(String[] args) {
    SparkConf sparkConf = new SparkConf();
    sparkConf.setAppName("DataFrameRDD");
    sparkConf.setMaster("local[1]");

    JavaSparkContext sc = new JavaSparkContext(sparkConf);
    String path = "./data/spark/sql/student.json";

    SQLContext sqlContext = new SQLContext(sc);
    //  DataFrame dataFrame = sqlContext.read().format("json").load(path);
    DataFrame dataFrame = sqlContext.read().json(path);
    /**
     * dataFrame RDD
     * rowJavaRDD 一行就是一个rowJavaRDD
     */
    System.out.println("---------3-----------");
    JavaRDD<Row> rowJavaRDD = dataFrame.javaRDD();
    rowJavaRDD.foreach(System.out::println);
    sc.stop();
  }
}
