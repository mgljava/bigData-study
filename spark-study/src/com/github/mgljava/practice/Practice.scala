package com.github.mgljava.practice

import org.apache.spark.{SparkConf, SparkContext}

object Practice {
  def main(args: Array[String]): Unit = {
    /**
     *
     *
     * 12班有多少人参加考试？
     * 13班有多少人参加考试？
     * 语文科目的平均成绩是多少？
     * 数学科目的平均成绩是多少？
     * 英语科目的平均成绩是多少？
     * 单个人平均成绩是多少？
     * 12班平均成绩是多少？
     * 12班男生平均总成绩是多少？
     * 12班女生平均总成绩是多少？
     * 13班平均成绩是多少？
     * 13班男生平均总成绩是多少？
     * 13班女生平均总成绩是多少？
     * 全校语文成绩最高分是多少？
     * 12班语文成绩最低分是多少？
     * 13班数学最高成绩是多少？
     * 总成绩大于150分的12班的女生有几个？
     * 总成绩大于150分，且数学大于等于70，且年龄大于等于19岁的学生的平均成绩是多少？
     */
    val conf = new SparkConf().setMaster("local").setAppName("Practice")
    val sc = new SparkContext(conf)
    val input = sc.textFile("./data/spark/practice/score.txt")
    input.cache()
    // 一共有多少个小于20岁的人参加考试？
    // 一共有多少个等于20岁的人参加考试？
    // 一共有多少个大于20岁的人参加考试？
    val count1 = input.map(line => {
      val person = line.split(" ")
      (person(1), person(2))
    }).distinct()
      .filter(item => item._2.toInt > 20)
      .count()
    println("count1: " + count1)

    // 一共有多少个女生参加考试？
    // 一共有多个男生参加考试？
    val count2 = input.map(line => {
      val person = line.split(" ")
      (person(1), person(3))
    }).distinct()
      .filter(item => item._2.equals("男"))
      .count()
    println("count2: " + count2)

  }
}
