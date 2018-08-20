package nullpointer.akkawebapi.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import nullpointer.akkawebapi.ErrorResponses.ErrorResponseJsonSupport
import org.scalatest._

trait RouteSpec extends FeatureSpec with GivenWhenThen with ScalatestRouteTest
  with MustMatchers with BeforeAndAfterEach with ErrorResponseJsonSupport
