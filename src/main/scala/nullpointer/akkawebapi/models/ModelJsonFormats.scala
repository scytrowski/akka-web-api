package nullpointer.akkawebapi.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsonFormat}

object ModelJsonFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat: JsonFormat[User] = jsonFormat2(User)
}
