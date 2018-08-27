package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.Entities.RestEntity
import nullpointer.akkawebapi.db.DatabaseConfiguration
import nullpointer.akkawebapi.db.Tables.RestEntityTable
import nullpointer.akkawebapi.exceptions.RepositoryExceptions.AbsentIdRepositoryException
import nullpointer.akkawebapi.repositories.Repositories.RestRepository

import scala.concurrent.{ExecutionContext, Future}

object SlickBackedRestRepository extends DatabaseConfiguration {
  import config.profile.api._

  class SlickBackedRestRepository[D](database: Database)(implicit ec: ExecutionContext, query: TableQuery[_ <: RestEntityTable[D]]) extends RestRepository[D] {

    override def getById(id: Long): Future[Option[RepositoryEntity]] = database.run(query.filter(_.id === id).result.headOption)

    override def getAll: Future[Seq[RepositoryEntity]] = database.run(query.result)

    override def add(data: D): Future[RepositoryEntity] = database.run((query returning query.map(_.id)) += RestEntity(0, data)).map(id => RestEntity(id, data))

    override def update(entity: RepositoryEntity): Future[Unit] = entity.id match {
      case Some(_) => database.run(query.insertOrUpdate(entity)).map(_ => Unit)
      case None => Future.failed(AbsentIdRepositoryException("Cannot update entity with absent id"))
    }

    override def deleteById(id: Long): Future[Boolean] = database.run(query.filter(_.id === id).delete).map(_ > 0)
  }
}


