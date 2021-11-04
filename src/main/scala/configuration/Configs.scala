package configuration

import pureconfig._
import pureconfig.generic.auto._

case class ConsumerAppConf(broker: String, topic: String, consumerId: String, keyDeserializer: String, valueDeserializer: String)

case class ProducerAppConf(broker: String, topic: String, producerId: String, keySerializer: String, valueSerializer: String)

object Configs {
  lazy val consumerAppConf: ConsumerAppConf =
    ConfigSource.default.at("kafka-consumer").loadOrThrow[ConsumerAppConf]
  lazy val producerAppConf: ProducerAppConf =
    ConfigSource.default.at("kafka-producer").loadOrThrow[ProducerAppConf]
}
