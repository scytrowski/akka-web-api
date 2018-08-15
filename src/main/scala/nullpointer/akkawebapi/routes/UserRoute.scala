package nullpointer.akkawebapi.routes
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import nullpointer.akkawebapi.models.ErrorResponses.ErrorResponse
import nullpointer.akkawebapi.models.Users.{User, UserJsonSupport}
import nullpointer.akkawebapi.repositories.UserRepositories.UserRepository

import scala.util.{Failure, Success}

case class UserRoute(repository: UserRepository) extends CustomRoute with UserJsonSupport {
  override def definition: Route =
    pathPrefix("user") {
      pathEnd {
        get {
          complete(repository.getAll)
        } ~
        post {
          entity(as[User]) { user =>
            onComplete(repository.add(user)) {
              case Success(addedUser) => complete(StatusCodes.Created, addedUser)
              case Failure(ex) => completeWithError(ErrorResponse(400, ex.getMessage))
            }
          }
        } ~
        put {
          entity(as[User]) { user =>
            onComplete(repository.update(user)) {
              case Success(_) => complete(StatusCodes.OK)
              case Failure(ex) => completeWithError(ErrorResponse(400, ex.getMessage))
            }
          }
        }
      } ~
      pathSuffix(LongNumber) { id =>
        get {
          onSuccess(repository.getById(id)) {
            case Some(user) => complete(user)
            case None => completeWithError(ErrorResponse(404, s"User with id '$id' does not exist"))
          }
        } ~
        delete {
          onSuccess(repository.deleteById(id)) {
            case true => complete(StatusCodes.OK)
            case false => completeWithError(ErrorResponse(404, s"User with id '$id' does not exist"))
          }
        }
      }
    }
}
