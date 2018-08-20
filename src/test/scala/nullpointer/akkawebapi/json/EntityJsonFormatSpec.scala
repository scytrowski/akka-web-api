package nullpointer.akkawebapi.json

import nullpointer.akkawebapi.Entities.Entity
import org.scalatest.{FunSpec, MustMatchers}
import spray.json._

class EntityJsonFormatSpec extends JsonFormatSpec {
  import EntityJsonFormatSpec._

  describe("An EntityJsonFormat") {
    it("must read None when id field is absent") {
      val format: EntityJsonFormat[TestIdClass, TestDataClass] = EntityJsonFormat()
      val json = JsObject(
        "data" -> TestDataClass("some data").toJson
      )
      val entity = format.read(json)
      entity.id.isEmpty mustBe true
    }

    it("must read correct id when id field is present") {
      val format: EntityJsonFormat[TestIdClass, TestDataClass] = EntityJsonFormat()
      val expectedId = TestIdClass(1337)
      val json = JsObject(
        "id" -> expectedId.toJson,
        "data" -> TestDataClass("some data").toJson
      )
      val entity = format.read(json)
      entity.id.isDefined mustBe true
      entity.id.get mustBe expectedId
    }

    it("must read correct data") {
      val format: EntityJsonFormat[TestIdClass, TestDataClass] = EntityJsonFormat()
      val expectedData = TestDataClass("some data")
      val json = JsObject(
        "id" -> TestIdClass(2048).toJson,
        "data" -> expectedData.toJson
      )
      val entity = format.read(json)
      entity.data mustBe expectedData
    }

    it("must not write id when is None") {
      val format: EntityJsonFormat[TestIdClass, TestDataClass] = EntityJsonFormat()
      val entity: Entity[TestIdClass, TestDataClass] = Entity(None, TestDataClass("some data"))
      val json = format.write(entity).asJsObject
      json.fields.contains("id") mustBe false
    }

    it("must write correct id when is defined") {
      val format: EntityJsonFormat[TestIdClass, TestDataClass] = EntityJsonFormat()
      val expectedId = TestIdClass(1111)
      val entity: Entity[TestIdClass, TestDataClass] = Entity(Some(expectedId), TestDataClass("some data"))
      val json = format.write(entity).asJsObject
      val id = json.fields.get("id").map(_.convertTo[TestIdClass])
      id.isDefined mustBe true
      id.get mustBe expectedId
    }

    it("must write correct data") {
      val format: EntityJsonFormat[TestIdClass, TestDataClass] = EntityJsonFormat()
      val expectedData = TestDataClass("some data")
      val entity: Entity[TestIdClass, TestDataClass] = Entity(Some(TestIdClass(1111)), expectedData)
      val json = format.write(entity).asJsObject
      val data = json.fields.get("data").map(_.convertTo[TestDataClass])
      data.isDefined mustBe true
      data.get mustBe expectedData
    }
  }
}

object EntityJsonFormatSpec extends DefaultJsonProtocol {
  case class TestIdClass(value: Int)

  lazy implicit val testIdClassFormat: RootJsonFormat[TestIdClass] = jsonFormat1(TestIdClass)

  case class TestDataClass(data: String)

  lazy implicit val testDataClassFormat: RootJsonFormat[TestDataClass] = jsonFormat1(TestDataClass)
}
