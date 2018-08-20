package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.Entities.Entity

import scala.concurrent.Future

object Repositories {
  trait Repository[I, D] {
    type RepositoryEntity = Entity[I, D]

    def getById(id: I): Future[Option[RepositoryEntity]]
    def getAll: Future[Seq[RepositoryEntity]]
    def add(data: D): Future[RepositoryEntity]
    def update(entity: RepositoryEntity): Future[Unit]
    def deleteById(id: I): Future[Boolean]
  }

  type RestRepository[D] = Repository[Long, D]
}
