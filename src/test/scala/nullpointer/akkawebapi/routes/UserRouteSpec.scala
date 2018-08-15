package nullpointer.akkawebapi.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import nullpointer.akkawebapi.models.ErrorResponses.ErrorResponse
import nullpointer.akkawebapi.models.Users.{User, UserJsonSupport}
import nullpointer.akkawebapi.repositories.UserRepositories.{MapBackedUserRepository, UserRepository}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

class UserRouteSpec extends RouteSpec with UserJsonSupport {
  private val userModels: Seq[User] = Seq(
    User(None, "first_user", "password123"),
    User(None, "someOtherUser", "abcdefg"),
    User(None, "admin", "123456")
  )
  private val repository: UserRepository = new MapBackedUserRepository()
  private val route: Route = UserRoute(repository)
  private var users: Seq[User] = Seq.empty

  override def beforeEach(): Unit = {
    users = Await.result(Future.sequence(userModels.map(repository.add)), 5 seconds).sortBy(_.id)
  }

  override def afterEach(): Unit =
    Await.ready(Future.sequence(users.map(u => repository.deleteById(u.id.get))), 5 seconds)

  private def getExistingId: Long = users(Random.nextInt(users.size)).id.get

  private def getNonExistingId: Long = users.map(_.id.get).max + 1

  feature("User can request UserRoute with GET method") {
    scenario("user requests route with existing id segment") {
      val existingId = getExistingId
      Get(s"/user/$existingId") ~> route ~> check {
        Given(s"user with id '$existingId' is present in repository")
        When(s"user requests route with id '$existingId' segment")

        Then("status code must be OK")
        status mustBe StatusCodes.OK

        And(s"response body must be user with id '$existingId'")
        responseAs[User] mustBe users.filter(_.id.contains(existingId)).head
      }
    }

    scenario("user requests route with non existing id segment") {
      val nonExistingId = getNonExistingId
      Get(s"/user/$nonExistingId") ~> route ~> check {
        Given(s"user with id '$nonExistingId' is not present in repository")
        When("user requests route with non existing id segment")

        Then("status code must be NotFound")
        status mustBe StatusCodes.NotFound

        And("response body must be error with correct status code and message")
        responseAs[ErrorResponse] mustBe ErrorResponse(404, s"User with id '$nonExistingId' does not exist")
      }
    }

    scenario("user requests route without id segment") {
      Get("/user") ~> route ~> check {
        Given("users are present in the repository")
        When("user requests route without id segment")

        Then("status code must be OK")
        status mustBe StatusCodes.OK

        And("response body must be users added to the repository")
        responseAs[Seq[User]].sortBy(_.id) mustBe users
      }
    }
  }

  feature("User can request UserRoute with POST method") {
    scenario("user requests route with user with absent id") {
      val newUserId = getNonExistingId
      val userToAdd = User(None, "new_user", "a1b2c3")
      Post("/user", userToAdd) ~> route ~> check {
        When("user requests route with user with absent id")

        Then("status code must be Created")
        status mustBe StatusCodes.Created

        And("response body must be user to add with correct id")
        responseAs[User] mustBe userToAdd.copy(id = Some(newUserId))
      }
    }

    scenario("user requests route with user with present id") {
      val existingId = getExistingId
      Post("/user", User(Some(existingId), "new_user", "a1b2c3")) ~> route ~> check {
        When("user requests route with user with present id")

        Then("status code must be BadRequest")
        status mustBe StatusCodes.BadRequest

        And("response body must be error with correct status code and message")
        responseAs[ErrorResponse] mustBe ErrorResponse(400, "Cannot add user with present id")
      }
    }

    scenario("user requests route without user") {
      Post("/user") ~> route ~> check {
        When("user requests route without user")

        Then("request is not handled")
        handled mustBe false
      }
    }
  }

  feature("User can request UserRoute with PUT method") {
    scenario("user requests route with user with absent id") {
      Put("/user", User(None, "user_to_update", "a1b2c3")) ~> route ~> check {
        When("user requests route with user with absent id")

        Then("status code must be BadRequest")
        status mustBe StatusCodes.BadRequest

        And("response body must be error with correct status code and message")
        responseAs[ErrorResponse] mustBe ErrorResponse(400, "Cannot update user with absent id")
      }
    }

    scenario("user requests route with user with present id") {
      val existingId = getExistingId
      Put("/user", User(Some(existingId), "user_to_update", "a1b2c3")) ~> route ~> check {
        When("user requests route with user with present id")

        Then("status code must be OK")
        status mustBe StatusCodes.OK
      }
    }

    scenario("user requests route without user") {
      Put("/user") ~> route ~> check {
        When("user requests route without user")

        Then("request is not handled")
        handled mustBe false
      }
    }
  }

  feature("User can request UserRoute with DELETE method") {
    scenario("user requests route with existing id segment") {
      val existingId = getExistingId
      Delete(s"/user/$existingId") ~> route ~> check {
        Given(s"user with id '$existingId' is present in repository")
        When("user requests route with existing id segment")

        Then("status code must be OK")
        status mustBe StatusCodes.OK
      }
    }

    scenario("user requests route with non existing id segment") {
      val nonExistingId = getNonExistingId
      Delete(s"/user/$nonExistingId") ~> route ~> check {
        Given(s"user with id '$nonExistingId' is not present in repository")
        When("user requests route with non existing id segment")

        Then("status code must be NotFound")
        status mustBe StatusCodes.NotFound

        And("response body must be error with correct status code and message")
        responseAs[ErrorResponse] mustBe ErrorResponse(404, s"User with id '$nonExistingId' does not exist")
      }
    }

    scenario("user requests route without id segment") {
      Delete(s"/user") ~> route ~> check {
        When("user requests route without id segment")

        Then("request is not handled")
        handled mustBe false
      }
    }
  }
}
