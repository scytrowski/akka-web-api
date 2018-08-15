package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.models.Entity

import scala.concurrent.Future

trait Repository[K, E <: Entity] {
  def getById(id: K): Future[Option[E]]
  def getAll: Future[Seq[E]]
  def add(entity: E): Future[E]
  def update(entity: E): Future[Unit]
  def deleteById(id: K): Future[Boolean]
}
