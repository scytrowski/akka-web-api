package nullpointer.akkawebapi

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object ErrorResponses {
  case class ErrorResponse(statusCode: Int, message: String)

  trait ErrorResponseJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val errorResponseFormat: RootJsonFormat[ErrorResponse] = jsonFormat2(ErrorResponse)
  }
}
