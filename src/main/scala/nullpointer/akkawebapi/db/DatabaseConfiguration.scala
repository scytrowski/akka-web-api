package nullpointer.akkawebapi.db

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseConfiguration {
  lazy val config = DatabaseConfig.forConfig[JdbcProfile]("db")
}
