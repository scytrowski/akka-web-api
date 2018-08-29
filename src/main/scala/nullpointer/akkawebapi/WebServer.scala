package nullpointer.akkawebapi

import akka.http.scaladsl.server.{HttpApp, Route}
import com.typesafe.config.Config
import nullpointer.akkawebapi.Configurations.ServerConfiguration
import nullpointer.akkawebapi.Models.{Category, Post, Thread, ThreadPost, User}
import nullpointer.akkawebapi.WebServer.WebApp
import nullpointer.akkawebapi.db.DatabaseConfiguration
import nullpointer.akkawebapi.db.ModelTables.RestEntityTable
import nullpointer.akkawebapi.repositories.MapBackedRestRepository
import nullpointer.akkawebapi.repositories.Repositories.RestRepository
import nullpointer.akkawebapi.repositories.SlickBackedRestRepository.SlickBackedRestRepository
import nullpointer.akkawebapi.routes.RestRoute

import scala.concurrent.ExecutionContext

class WebServer(configuration: ServerConfiguration)(implicit ec: ExecutionContext) {
  private val app = WebApp(configuration)

  def start(): Unit = {
    app.startServer(configuration.host, configuration.port)
  }
}

object WebServer {
  private case class WebApp private(configuration: ServerConfiguration)(implicit ec: ExecutionContext) extends HttpApp with DatabaseConfiguration {
    import nullpointer.akkawebapi.json.ModelJsonFormats._
    import nullpointer.akkawebapi.db.ModelTables._
    import config.profile.api._

    private val database: Database = config.db

    private val userRepository: RestRepository[User] = new SlickBackedRestRepository(database)
    private val categoryRepository: RestRepository[Category] = new SlickBackedRestRepository(database)
    private val threadRepository: RestRepository[Thread] = new SlickBackedRestRepository(database)
    private val postRepository: RestRepository[Post] = new SlickBackedRestRepository(database)
    private val threadPostRepository: RestRepository[ThreadPost] = new SlickBackedRestRepository(database)

    override protected def routes: Route =
      path(configuration.basePath) {
        RestRoute[User](userRepository, "user") ~
        RestRoute[Category](categoryRepository, "category") ~
        RestRoute[Thread](threadRepository, "thread") ~
        RestRoute[Post](postRepository, "post") ~
        RestRoute[ThreadPost](threadPostRepository, "threadPost")
      }
  }
}
