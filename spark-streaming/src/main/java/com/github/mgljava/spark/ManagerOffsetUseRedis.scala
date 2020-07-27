package com.github.mgljava.spark

import java.util

import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferBrokers
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable

/**
 * 通过redis来管理offset
 */
object ManagerOffsetUseRedis {

  // 从redis中获取offset
  def getOffsetFromRedis(dbIndex: Int, topic: String): mutable.Map[String, String] = {
    val jedis = RedisClient.pool.getResource
    jedis.select(dbIndex)
    val result: util.Map[String, String] = jedis.hgetAll(topic)
    RedisClient.pool.returnResource(jedis)
    if (result.size() == 0) {
      // 设置partition
      result.put("0", "0")
      result.put("1", "0")
      result.put("2", "0")
    }

    import scala.collection.JavaConverters.mapAsScalaMap
    val offset: scala.collection.mutable.Map[String, String] = mapAsScalaMap(result)
    offset
  }

  /**
   * 将消费者的offset保存到redis中
   */
  def saveOffsetToRedis(dbIndex: Int, offsetRanges: Array[OffsetRange]): Unit = {
    val jedis = RedisClient.pool.getResource
    jedis.select(dbIndex)
    offsetRanges.foreach(item => {
      jedis.hset(item.topic, item.partition.toString, item.untilOffset.toString)
    })
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("ManagerOffsetUseRedis")
    // 设置每个分区每秒读取多少数据
    conf.set("spark.streaming.kafka.maxRatePerPartition", "10")
    val ssc = new StreamingContext(conf, Durations.seconds(5))
    ssc.sparkContext.setLogLevel("Error")

    val topic = "testTopic"

    // 从Redis中获取消费者offset
    val dbIndex = 3
    val currentTopicOffset: mutable.Map[String, String] = getOffsetFromRedis(dbIndex, topic)
    currentTopicOffset.foreach(x => {
      println(s"初始读取到的offset: $x")
    })

    // 转换成需要的类型
    val fromOffset: Map[TopicPartition, Long] = currentTopicOffset.map { resultSet =>
      new TopicPartition(topic, resultSet._1.toInt) -> resultSet._2.toLong
    }.toMap

    val kafkaProperties: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092,localhost:9093",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream2",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean) // 默认为true，由kafka的topic来定期提交
    )
    // 获取到消费者的offset，传递给SparkStreaming
    val stream = KafkaUtils.createDirectStream(ssc, PreferBrokers,
      ConsumerStrategies.Assign(fromOffset.keys, kafkaProperties, fromOffset))

    stream.foreachRDD(rdd => {
      println("业务处理完成===")
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd.foreachPartition(iter => {
        val o = offsetRanges(TaskContext.getPartitionId())
        println(s"topic:${o.topic} partition:${o.partition} fromOffset:${o.fromOffset} untilOffset:${o.untilOffset}")
      })
      // 将当前批次最后的分区offset 保存到redis中
      saveOffsetToRedis(dbIndex, offsetRanges)
    })

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }
}
