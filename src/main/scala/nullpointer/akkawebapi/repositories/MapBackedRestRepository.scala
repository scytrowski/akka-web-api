package nullpointer.akkawebapi.repositories

import java.util.concurrent.atomic.AtomicLong

import nullpointer.akkawebapi.repositories.Repositories.RestRepository

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

class MapBackedRestRepository[D] extends RestRepository[D] {
  private val items: collection.concurrent.Map[Long, D] = TrieMap()
  private val idCounter: AtomicLong = new AtomicLong(0)

  override def getById(id: Long): Future[Option[RepositoryEntity]] = ???

  override def getAll: Future[Seq[RepositoryEntity]] = ???

  override def add(data: D): Future[RepositoryEntity] = ???

  override def update(entity: RepositoryEntity): Future[Unit] = ???

  override def deleteById(id: Long): Future[Boolean] = ???
}
