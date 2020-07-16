package com.github.mgljava.wc

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    /**
     * 第一步：创建spark的配置对象sparkconf,设置spark程序的运行时的配置信息，例如说通过setMaster来设置程序
     * 链接spark集群的master的URL，如果设置为local，则代表spark程序在本地运行，
     */
    val conf = new SparkConf() //创建SparkConf对象
    conf.setMaster("local") // 此时，程序在本地执行，不需要安装spark集群
    // conf.setMaster("spark://192.168.56.102:7077") // 指定spark运行是集群模式 一般我们不在代码中指定，我们在提交的时候指定
    conf.setAppName("Spark-World-Count") // 设置应用程序的名称,在程序运行的监控界面可以看到这个名字

    /**
     * 第二步：创建SparkContext对象，
     * SparkContext是spark程序所有功能的唯一入口，无论是采用Scala，Java，Python，R等都必须有一个SparkContext
     * SparkContext核心作用：初始化spark应用程序运行时候所需要的核心组件，包括DAGScheduler，TaskScheduler,SchedulerBackend
     * 同时还会负责Spark程序往Master注册程序等
     * SparkContext是整个spark应用程序中最为至关重要的一个对象
     */
    val sc = new SparkContext(conf)

    /**
     * 第3步：根据具体的数据来源 (HDFS,HBase,Local等)通过SparkContext来创建RDD
     * RDD的创建有3种方式，外部的数据来源，根据scala集合，由其他的RDD操作
     * 数据会被RDD划分成为一系列的Partitions,分配到每个Partition的数据属于一个Task的处理范畴
     */
    // val line = sc.textFile("/emp/data/sample.txt", 1) // 读取本地的一个文件并且设置为1个partition
    // val line = sc.textFile("hdfs://192.168.56.102:9000/data/hive/sample.txt") // 指定HDFS的路径，这个也可以到时候在参数传入
    val lines = sc.textFile("./data/spark/words")
    /**
     * 第4步：对初始的RDD进行Transformation级别的处理，例如Map、filter等高阶函数等的编程来进行具体的数据计算
     * 在对每一行的字符串拆分成单个单词
     * 在单词的拆分的基础上对每个单词实例计算为1，也就是word=>(word,1)
     * 在对每个单词实例计数为1基础上统计每个单词在文件中出现的总次数
     */
    val worlds = lines.flatMap(_.split(" "))
    val pairs = worlds.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    /**
     * sortBy 排序
     */
    //  wordCounts.sortBy(word => word._2, ascending = false).foreach(wordNum => println(wordNum._1 + ":" + wordNum._2))

    /**
     * sortByKey 排序，先将tuple反转，在根据整形的key进行排序，然后在将排序的结果反转得到tuple
     */
    wordCounts.map(tuple => tuple.swap)
      .sortByKey(ascending = false)
      .map(tuple => tuple.swap)
      .foreach(tuple => {
        println(tuple._1 + ":" + tuple._2)
      })
    sc.stop()
  }
}
