package nullpointer.akkawebapi

import com.typesafe.config.Config

object Configurations {
  case class ServerConfiguration(host: String, port: Int, basePath: String)

  object ServerConfiguration {
    def ofConfig(config: Config): ServerConfiguration = {
      val apiConfig = config.getConfig("rest")
      val host = apiConfig.getString("host")
      val port = apiConfig.getInt("port")
      val basePath = apiConfig.getString("basePath")
      ServerConfiguration(host, port, basePath)
    }
  }
}
