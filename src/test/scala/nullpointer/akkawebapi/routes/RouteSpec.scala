package nullpointer.akkawebapi.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FeatureSpec, Matchers, MustMatchers}

trait RouteSpec extends FeatureSpec with ScalatestRouteTest with MustMatchers
