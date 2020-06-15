package com.github.mgljava.spark;

import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class WordCountJava {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf();
    conf.setMaster("local");
    conf.setAppName("Java-Spark-Word-Count");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> textFile = sc.textFile("hdfs://192.168.56.102:9000/data/hive/sample.txt");
    JavaPairRDD<String, Integer> counts = textFile
        .flatMap(s -> Arrays.asList(s.split("\t")).iterator())
        .mapToPair(word -> new Tuple2<>(word, 1))
        .reduceByKey((a, b) -> a + b);
    counts.saveAsTextFile("hdfs://192.168.56.102:9000/tmp/output");
    sc.stop();
  }
}
