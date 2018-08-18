package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.models.User
import nullpointer.akkawebapi.repositories.Repositories.RestRepository

object UserRepositories {
  type UserRepository = RestRepository[User]
}
