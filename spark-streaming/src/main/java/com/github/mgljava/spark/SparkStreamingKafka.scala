package com.github.mgljava.spark

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
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
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SparkStreamingKafka").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Durations.seconds(5))
    val topics = Array("test")
    val messages = KafkaUtils.createDirectStream(ssc, PreferConsistent, Subscribe[String, String](topics, kafkaProperties))

    val lines = messages.map(_.value)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
