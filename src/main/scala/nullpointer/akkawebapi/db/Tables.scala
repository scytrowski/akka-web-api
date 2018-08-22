package nullpointer.akkawebapi.db

import nullpointer.akkawebapi.Entities.RestEntity
import nullpointer.akkawebapi.Models.User
import slick.ast.{ScalaBaseType, TypedType}
import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape

object Tables extends DatabaseConfiguration {
  import config.profile.api._

  abstract class RestEntityTable[D](tag: Tag, tableName: String)(implicit dataType: TypedType[D]) extends Table[RestEntity[D]](tag, tableName) {
    val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * : ProvenShape[RestEntity[D]] = (id :: dataShape :: HNil) <>[RestEntity[D]] (
      x => RestEntity[D](x.head, x.tail.head),
      (x: RestEntity[D]) => x.id.map(id => id :: x.data :: HNil)
    )

    protected def dataShape: ProvenShape[D]
  }

  class UserTable(tag: Tag) extends RestEntityTable[User](tag, "users") {
    val name = column[String]("name")
    val password = column[String]("password")

    override protected def dataShape: ProvenShape[User] = (name, password) <> (User.tupled, User.unapply)
  }

  implicit val userType: TypedType[User] = ScalaBaseType[User]
  val users = TableQuery[UserTable]
}
