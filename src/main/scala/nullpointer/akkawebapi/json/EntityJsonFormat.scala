package nullpointer.akkawebapi.json

import nullpointer.akkawebapi.Entities.Entity
import spray.json._

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

object EntityJsonFormat {
  type RestEntityJsonFormat[D] = EntityJsonFormat[Long, D]

  object RestEntityJsonFormat extends BasicFormats {
    def apply[D](implicit dataFormat: RootJsonFormat[D]): RestEntityJsonFormat[D] = EntityJsonFormat()
  }
}