package nullpointer.akkawebapi.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsonFormat, RootJsonFormat}

object Entities {
  case class Entity[I, D](id: Option[I], data: D) {
    def withId(id: I): Entity[I, D] = copy(id = Some(id))
  }

  type RestEntity[D] = Entity[Long, D]

  trait EntityJsonFormatProvider[I, D] {
    implicit val idFormat: RootJsonFormat[I]
    implicit val dataFormat: RootJsonFormat[D]
  }

  trait EntityJsonSupport[I, D] extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val idFormat: JsonFormat[I]
    implicit val dataFormat: JsonFormat[D]

    implicit val entityFormat: RootJsonFormat[Entity[I, D]] = jsonFormat2(Entity[I, D])
  }

  type RestEntityJsonSupport[D] = EntityJsonSupport[Long, D]
}
