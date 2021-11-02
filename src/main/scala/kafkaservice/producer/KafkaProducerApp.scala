package kafkaservice.producer

import kafkaservice.consumer.KRow
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.{Date, Properties}
import scala.util.Random
import io.circe.syntax._
import io.circe.generic.auto._

object KafkaProducerApp {
  def main(args: Array[String]): Unit = {
    val rnd = new Random()
    val topicName = "test"
    val host = "localhost:9092"
    val producerId = "producer-application"
    val events = 10
    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, producerId)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val t = System.currentTimeMillis()
    for (nEvents <- Range(0, events)) {
      val runtime = new Date().getTime
      val ip = "192.168.2." + rnd.nextInt(255)
      val msg = KRow(runtime, nEvents, "www.example.com", ip).asJson
      val data = new ProducerRecord[String, String](topicName, ip, msg.noSpaces)

      //async
      //producer.send(data, (m,e) => {})
      //sync
      producer.send(data)
    }

    producer.flush()
    System.out.println("sent per second: " + events * 1000 / (System.currentTimeMillis() - t))
    producer.close()

  }
}
