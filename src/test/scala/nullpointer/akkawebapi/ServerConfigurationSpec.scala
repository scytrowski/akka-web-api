package nullpointer.akkawebapi

import com.typesafe.config.ConfigFactory
import nullpointer.akkawebapi.Configurations.ServerConfiguration

class ServerConfigurationSpec extends CommonSpec {
  import collection.JavaConverters._

  describe("A ServerConfiguration") {
    it("must have host value from config") {
      val expectedHost = "example.host"
      val config = ConfigFactory.parseMap(Map[String, Any](
        "rest.host" -> expectedHost,
        "rest.port" -> 8080,
        "rest.basePath" -> "api"
      ).asJava)
      val serverConfiguration = ServerConfiguration.ofConfig(config)
      serverConfiguration.host mustBe expectedHost
    }

    it("must have port value from config") {
      val expectedPort = 8080
      val config = ConfigFactory.parseMap(Map[String, Any](
        "rest.host" -> "example.host",
        "rest.port" -> expectedPort,
        "rest.basePath" -> "api"
      ).asJava)
      val serverConfiguration = ServerConfiguration.ofConfig(config)
      serverConfiguration.port mustBe expectedPort
    }

    it("must have base path value from config") {
      val expectedBasePath = "api"
      val config = ConfigFactory.parseMap(Map[String, Any](
        "rest.host" -> "example.host",
        "rest.port" -> 8080,
        "rest.basePath" -> expectedBasePath
      ).asJava)
      val serverConfiguration = ServerConfiguration.ofConfig(config)
      serverConfiguration.basePath mustBe expectedBasePath
    }
  }
}
