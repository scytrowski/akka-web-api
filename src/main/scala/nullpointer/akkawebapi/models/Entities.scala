package nullpointer.akkawebapi.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

object Entities {
  case class Entity[I, D](id: Option[I], data: D) {
    def withId(id: I): Entity[I, D] = copy(id = Some(id))
  }

  type RestEntity[D] = Entity[Long, D]

  trait EntityJsonSupport[I, D] extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val idFormat: JsonFormat[I]
    implicit val dataFormat: RootJsonFormat[D]

    implicit val entityFormat: RootJsonFormat[Entity[I, D]] = jsonFormat2(Entity[I, D])
  }

  case class EntityJsonFormat[I, D](implicit idFormat: JsonFormat[I], dataFormat: JsonFormat[D]) extends RootJsonFormat[Entity[I, D]] {
    override def read(json: JsValue): Entity[I, D] = {
      val jsonObject = json.asJsObject
      val id = jsonObject.fields.get("id").map(_.convertTo[I])
      val data = jsonObject.fields("data").convertTo[D]
      Entity(id, data)
    }

    override def write(obj: Entity[I, D]): JsValue = {
      val dataJson = obj.data.toJson
      obj.id match {
        case Some(id) => JsObject(
          "id" -> id.toJson,
          "data" -> dataJson
        )
        case None => JsObject(
          "data" -> dataJson
        )
      }
    }
  }

  type RestEntityJsonSupport[D] = EntityJsonSupport[Long, D]
}
