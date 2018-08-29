package nullpointer.akkawebapi.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import nullpointer.akkawebapi.Models.{Category, Post, Thread, ThreadPost, User}
import org.joda.time.DateTime
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object ModelJsonFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val dateTimeFormat: RootJsonFormat[DateTime] = DateTimeJsonFormat()

  implicit val userFormat: RootJsonFormat[User] = jsonFormat2(User)

  implicit val categoryFormat: RootJsonFormat[Category] = jsonFormat2(Category)

  implicit val threadFormat: RootJsonFormat[Thread] = jsonFormat3(Thread)

  implicit val postFormat: RootJsonFormat[Post] = jsonFormat3(Post)

  implicit val threadPostFormat: RootJsonFormat[ThreadPost] = jsonFormat2(ThreadPost)
}
