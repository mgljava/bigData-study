package com.github.mgljava.spark

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferBrokers
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * 集成Kafka
 * 采用版本为2.12版本
 */
object SparkStreamingKafka {

  val kafkaProperties: Map[String, Object] = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092,localhost:9093",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "use_a_separate_group_id_for_each_stream",

    /**
     * 当没有初始的offset，或者当前的offset不存在，如何处理数据
     * earliest：自动重置偏移量为最小偏移量
     * latest：自动重置偏移量为最大偏移量 [默认]
     * node: 没有找到以前的offset，抛出异常
     */
    "auto.offset.reset" -> "latest",

    // 当设置 enable.auto.commit 为false时，不会自动向Kafka中保存消费者offset，需要异步处理完数据之后手动提交
    "enable.auto.commit" -> (false: java.lang.Boolean) // 默认为true，由kafka的topic来定期提交
  )

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SparkStreamingKafka").setMaster("local")
    val ssc = new StreamingContext(conf, Durations.seconds(5))
    val topics = Array("test")
    /**
     * PreferBrokers：如果executor节点在broker节点上，那么可以采用这种模式
     * PreferConsistent：均匀的将数据分布在集群的executor上
     * PreferFixed：如果节点之间的分区有明显的分布不均，PreferFixed可以通过个map将topic分区分布到指定节点上
     */
    val messages: InputDStream[ConsumerRecord[String, String]] =
      KafkaUtils.createDirectStream(ssc, PreferBrokers, Subscribe[String, String](topics, kafkaProperties))

    val lines = messages.map(_.value)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()

    // 手动异步更新offset
    messages.foreachRDD(rdd => {
      val offsetRanges:Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      messages.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
