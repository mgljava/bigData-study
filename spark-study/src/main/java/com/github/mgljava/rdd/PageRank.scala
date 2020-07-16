package com.github.mgljava.rdd

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object PageRank {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("").setMaster("PageRank")
    val sc = new SparkContext(conf)
    val links = sc.objectFile[(String, Seq[String])]("links")
      .partitionBy(new HashPartitioner(100))
      .persist()

    var ranks = links.mapValues(_ => 1.0)
    for (i <- 0 until 10) {
      val contributions = links.join(ranks).flatMap {
        case (pageId, (links, rank)) =>
          links.map(dest => (dest, rank / links.size))
      }
      ranks = contributions.reduceByKey((x, y) => x + y).mapValues(v => 0.15 + 0.85 * v)
    }
    // 写出最终排名
    ranks.saveAsTextFile("ranks")
  }
}
