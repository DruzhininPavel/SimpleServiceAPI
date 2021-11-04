package kafkaservice.consumer

import io.circe.jawn._
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util.Properties
import java.util.regex.Pattern
import scala.jdk.CollectionConverters._
import io.circe.generic.auto._

import scala.language.postfixOps

import configuration.Configs.consumerAppConf

case class KRow(time: Long, id: Long, event: String, userIP: String)

object KafkaConsumerApp {
  def main(args: Array[String]): Unit = {
    val topicName = consumerAppConf.topic

    val  props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerAppConf.broker)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerAppConf.consumerId)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerAppConf.keyDeserializer)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerAppConf.valueDeserializer)


    val consumer = new KafkaConsumer[String, String](props)

    consumer.subscribe(Pattern.compile(topicName))

    while(true){
      val records = consumer.poll(Duration.ofNanos(100))

      for (record <- records.asScala){
        val printable = parse(record.value()).getOrElse(throw new Exception).as[KRow] match {
          case Right(value) => value
          case Left(e) => throw new Exception(e.message)
        }
        println(printable)
      }
    }
  }
}