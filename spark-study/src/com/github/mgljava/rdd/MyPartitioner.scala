package com.github.mgljava.rdd

import java.net.URL

import org.apache.spark.Partitioner

/**
 * 基于域名的分区器
 */
class MyPartitioner(number: Int) extends Partitioner {
  override def numPartitions: Int = number

  override def getPartition(key: Any): Int = {
    val domain = new URL(key.toString).getHost
    val code = domain.hashCode % numPartitions
    if (code < 0) {
      code + numPartitions // 使其非负
    }
    else {
      code
    }
  }

  /*override def equals(obj: Any): Boolean = {
    case my: MyPartitioner => my.numPartitions == numPartitions
    case _ => false
  }*/
}
