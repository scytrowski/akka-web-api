package nullpointer.akkawebapi.repositories

import java.util.concurrent.atomic.AtomicLong

import nullpointer.akkawebapi.models.Entity

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

class MapBackedRestRepository[E <: Entity] extends RestRepository[E] {
  private val items: collection.concurrent.Map[Long, E] = TrieMap()
  private val idCounter: AtomicLong = new AtomicLong(0)

  override def getById(id: Long): Future[Option[E]] = ???

  override def getAll: Future[Seq[E]] = ???

  override def add(entity: E): Future[E] = ???

  override def update(entity: E): Future[Unit] = ???

  override def deleteById(id: Long): Future[Boolean] = ???
}
