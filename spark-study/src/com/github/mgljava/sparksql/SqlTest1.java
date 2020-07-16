package com.github.mgljava.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class SqlTest1 {

  public static void main(String[] args) {
    SparkConf sparkConf = new SparkConf();
    sparkConf.setAppName("SqlTest1");
    sparkConf.setMaster("local[1]");

    JavaSparkContext sc = new JavaSparkContext(sparkConf);

    String path = "./data/spark/sql/student.json";

    SQLContext sqlContext = new SQLContext(sc);
    //  DataFrame dataFrame = sqlContext.read().format("json").load(path);
    DataFrame dataFrame = sqlContext.read().json(path);

    /**
     * DataFrame 有数据、有列的schema
     * sqlContext 读取json文件家在城DataFrame DataFrame会按照ascii码排序
     * 写SQL查询出来的DataFrame会按照指定的列显示，不会排序
     */
    dataFrame.show();
    dataFrame.printSchema();

    System.out.println("---------1-----------");

    // select name,age from tableName where age > 18;
    dataFrame.select("name", "age").where(dataFrame.col("age").gt(18)).show();

    System.out.println("---------2-----------");
    /*
     * 将DataFrame注册成临时表
     * student1 这张表不在内存中也不再磁盘中，相当于一个指针指向源文件，底层操作解析spark job 读取源文件
     */
    dataFrame.registerTempTable("student1");
    sqlContext.sql("select name,age from student1 where age > 18").show();
  }
}
