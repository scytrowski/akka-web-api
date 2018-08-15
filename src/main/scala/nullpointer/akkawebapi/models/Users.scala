package nullpointer.akkawebapi.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object Users {
  case class User(override val id: Option[Long], name: String, password: String) extends Entity

  trait UserJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val userFormat: RootJsonFormat[User] = jsonFormat3(User)
  }
}


