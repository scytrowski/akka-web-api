package nullpointer.akkawebapi.routes

import akka.http.scaladsl.model.StatusCodes

class HelloWorldRouteSpec extends RouteSpec {
  feature("The user can request HelloWorldRoute with GET method") {
    scenario("user requests HelloWorldRoute with GET method") {
      Get("/hello") ~> HelloWorldRoute() ~> check {
        status mustBe StatusCodes.OK
        responseAs[String] mustBe "Hello, world!"
      }
    }
  }
}
