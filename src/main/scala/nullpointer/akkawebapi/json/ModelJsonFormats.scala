package nullpointer.akkawebapi.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import nullpointer.akkawebapi.models.User
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object ModelJsonFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat2(User)
}
