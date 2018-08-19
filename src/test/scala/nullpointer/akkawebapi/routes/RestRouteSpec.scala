package nullpointer.akkawebapi.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Route
import nullpointer.akkawebapi.models.Entities.{Entity, EntityJsonFormat, RestEntity}
import nullpointer.akkawebapi.models.ErrorResponses.{ErrorResponse, ErrorResponseJsonSupport}
import nullpointer.akkawebapi.repositories.MapBackedRestRepository
import nullpointer.akkawebapi.repositories.Repositories.RestRepository
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

class RestRouteSpec extends RouteSpec with SprayJsonSupport with DefaultJsonProtocol with BasicFormats with ErrorResponseJsonSupport {
  import RestRouteSpec._

  private implicit val testClassFormat: RootJsonFormat[TestClass] = jsonFormat1(TestClass)
  private implicit val entityFormat: RootJsonFormat[RestEntity[TestClass]] = EntityJsonFormat[Long, TestClass]

  private val items: Seq[TestClass] = Seq(
    TestClass("first item data"),
    TestClass("second item data"),
    TestClass("third item data")
  )
  private val repository: RestRepository[TestClass] = new MapBackedRestRepository
  private val route: Route = RestRoute(repository, "data")
  private var addedItems: Seq[RestEntity[TestClass]] = Seq.empty

  override def beforeEach(): Unit = {
    addedItems = await(Future.sequence(items.map(repository.add))).sortBy(_.id)
  }

  override def afterEach: Unit =
    await(Future.sequence(addedItems.map(i => repository.deleteById(i.id.get))))

  private def existingItemId: Long = addedItems(Random.nextInt(addedItems.size)).id.get

  private def nonExistingItemId: Long = addedItems.map(_.id.get).max + 1

  feature("User can request RestRoute with GET method") {
    scenario("user requests route with existing id segment") {
      val existingId = existingItemId
      Get(s"/data/$existingId") ~> route ~> check {
        Given(s"item with id '$existingId' exists in repository")
        When(s"user requests route with id '$existingId' segment")

        Then("status code must be OK")
        status mustBe StatusCodes.OK

        And("Content-Type header must be application/json")
        contentType mustBe ContentTypes.`application/json`

        And(s"response body must be data with id '$existingId'")
        val expectedItem = addedItems.filter(_.id.contains(existingId)).head
        responseAs[RestEntity[TestClass]] mustBe expectedItem
      }
    }

    scenario("user requests route with non existing id segment") {
      val nonExistingId = nonExistingItemId
      Get(s"/data/$nonExistingId") ~> route ~> check {
        Given(s"item with id '$nonExistingId' does not exist in repository")
        When(s"user requests route with id '$nonExistingId' segment")

        Then("status code must be NotFound")
        status mustBe StatusCodes.NotFound

        And("Content-Type header must be application/json")
        contentType mustBe ContentTypes.`application/json`

        And("response body must be correct error response")
        val expectedErrorResponse = ErrorResponse(404, s"TestClass with requested id '$nonExistingId' does not exist")
        responseAs[ErrorResponse] mustBe expectedErrorResponse
      }
    }

    scenario("user requests route without id segment") {
      Get("/data") ~> route ~> check {
        Given("some items exist in repository")
        When("user requests route without id segment")

        Then("status code must be OK")
        status mustBe StatusCodes.OK

        And("Content-Type header must be application/json")
        contentType mustBe ContentTypes.`application/json`

        And("response body must be items from repository")
        responseAs[Seq[RestEntity[TestClass]]] must contain theSameElementsAs addedItems
      }
    }
  }

  feature("User can request RestRoute with POST method") {
    scenario("user requests route with item") {
      val item = TestClass("new item data")
      Post("/data", item) ~> route ~> check {
        When("user requests route with item")

        Then("status code must be Created")
        status mustBe StatusCodes.Created

        And("Content-Type header must be application/json")
        contentType mustBe ContentTypes.`application/json`

        And("response body must be correct entity")
        val expectedEntity: Entity[Long, TestClass] = Entity(Some(nonExistingItemId), item)
        responseAs[RestEntity[TestClass]] mustBe expectedEntity
      }
    }

    scenario("user requests route without item") {
      Post("/data") ~> route ~> check {
        When("user requests route without item")

        Then("request is not handled")
        handled mustBe false
      }
    }
  }

  feature("User can request RestRoute with PUT method") {
    scenario("user requests route with item with present id") {
      val existingId = existingItemId
      val existingItem = addedItems.filter(_.id.contains(existingId)).head
      val modifiedItem = existingItem.copy(data = existingItem.data.copy(data = "modified item data"))
      Put("/data", modifiedItem) ~> route ~> check {
        When("user requests route with item with present id")

        Then("status code must be OK")
        status mustBe StatusCodes.OK
      }
    }

    scenario("user requests route with item with absent id") {
      val entityWithAbsentId: RestEntity[TestClass] = Entity(None, TestClass("item with absent id"))
      Put("/data", entityWithAbsentId) ~> route ~> check {
        When("user requests route with item with absent id")

        Then("status code must be BadRequest")
        status mustBe StatusCodes.BadRequest

        And("Content-Type must be application/json")
        contentType mustBe ContentTypes.`application/json`

        And("response body must be correct error response")
        val expectedErrorResponse = ErrorResponse(400, "Cannot update entity with absent id")
        responseAs[ErrorResponse] mustBe expectedErrorResponse
      }
    }

    scenario("user requests route without item") {
      Put("/data") ~> route ~> check {
        When("user requests route without item")

        Then("request is not handled")
        handled mustBe false
      }
    }
  }

  feature("User can request RestRoute with DELETE method") {
    scenario("user requests route with existing id segment") {
      val existingId = existingItemId
      Delete(s"/data/$existingId") ~> route ~> check {
        Given(s"item with id '$existingId' exists in repository")
        When("user requests route with existing id segment")

        Then("status code must be OK")
        status mustBe StatusCodes.OK
      }
    }

    scenario("user requests route with non existing id segment") {
      val nonExistingId = nonExistingItemId
      Delete(s"/data/$nonExistingId") ~> route ~> check {
        Given(s"item with id '$nonExistingId' does not exist in repository")
        When("user requests route with non existing id segment")

        Then("status code must be NotFound")
        status mustBe StatusCodes.NotFound

        And("Content-Type header must be application/json")
        contentType mustBe ContentTypes.`application/json`

        And("response body must be correct error response")
        val expectedErrorResponse = ErrorResponse(404, s"TestClass with requested id '$nonExistingId' does not exist")
        responseAs[ErrorResponse] mustBe expectedErrorResponse
      }
    }

    scenario("user requests route without id segment") {
      Delete("/data") ~> route ~> check {
        When("user requests route without id segment")

        Then("request is not handled")
        handled mustBe false
      }
    }
  }
}

object RestRouteSpec {
  def await[T](future: Future[T]): T = Await.result(future, 5 seconds)

  case class TestClass(data: String)
}
