import com.typesafe.config.{Config, ConfigFactory}

final case class ServerConfiguration(host: String, port: Int)

package object configuration {
  private lazy val config: Config = ConfigFactory.load()
  private lazy val serverHost: String = config.getConfig("webservice").getString("host")
  private lazy val serverPort: Int = config.getConfig("webservice").getInt("port")

  lazy val serverConfig: ServerConfiguration = ServerConfiguration(serverHost, serverPort)
}
