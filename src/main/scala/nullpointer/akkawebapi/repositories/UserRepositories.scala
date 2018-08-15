package nullpointer.akkawebapi.repositories

import java.util.concurrent.atomic.AtomicLong

import nullpointer.akkawebapi.exceptions.RepositoryExceptions.{AbsentIdRepositoryException, PresentIdRepositoryException}
import nullpointer.akkawebapi.models.Users.User

import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}

object UserRepositories {
  type UserRepository = Repository[User]

  class MapBackedUserRepository(implicit ec: ExecutionContext) extends UserRepository {
    private val keyCounter: AtomicLong = new AtomicLong(0)
    private val userMap: collection.concurrent.Map[Long, User] = TrieMap()

    override def getById(id: Long): Future[Option[User]] = Future(userMap.get(id))

    override def getAll: Future[Seq[User]] = Future(userMap.values.toSeq)

    override def add(entity: User): Future[User] = Future {
      entity.id match {
        case Some(_) => throw PresentIdRepositoryException("Cannot add user with present id")
        case None =>
          val entityKey = keyCounter.incrementAndGet()
          val entityWithKey = entity.copy(id = Some(entityKey))
          userMap.put(entityKey, entityWithKey)
          entityWithKey
      }
    }

    override def update(entity: User): Future[Unit] = Future {
      entity.id match {
        case Some(entityKey) =>
          userMap.put(entityKey, entity)
        case None => throw AbsentIdRepositoryException("Cannot update user with absent id")
      }
    }

    override def deleteById(id: Long): Future[Boolean] = Future(userMap.remove(id).isDefined)
  }
}
