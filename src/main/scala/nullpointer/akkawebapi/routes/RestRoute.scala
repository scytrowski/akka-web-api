package nullpointer.akkawebapi.routes

import akka.http.scaladsl.server.Route
import nullpointer.akkawebapi.models.Entities.RestEntityJsonSupport
import nullpointer.akkawebapi.repositories.Repositories.RestRepository
import spray.json.{BasicFormats, JsonFormat}

import scala.reflect.ClassTag

case class RestRoute[D](repository: RestRepository[D], pathPrefix: String)(implicit classTag: ClassTag[D], override val dataFormat: JsonFormat[D]) extends CustomRoute with RestEntityJsonSupport[D] with BasicFormats {
  override implicit val idFormat: JsonFormat[Long] = LongJsonFormat

  override def definition: Route = path(pathPrefix) {
    get {
      complete(classTag.runtimeClass.getSimpleName)
    }
  }
}
