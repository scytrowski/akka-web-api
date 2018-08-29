package nullpointer.akkawebapi.db

import java.sql.Timestamp

import nullpointer.akkawebapi.Entities.RestEntity
import nullpointer.akkawebapi.Models.{Category, Model, Post, User, Thread, ThreadPost}
import org.joda.time.DateTime
import slick.ast.{ScalaBaseType, TypedType}
import slick.collection.heterogeneous.HNil
import slick.lifted.ProvenShape

import scala.reflect.ClassTag

object ModelTables extends DatabaseConfiguration {
  import config.profile.api._

  abstract class RestEntityTable[D](tag: Tag, tableName: String)(implicit dataType: TypedType[D]) extends Table[RestEntity[D]](tag, tableName) {
    val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * : ProvenShape[RestEntity[D]] =
      (id :: dataShape :: HNil) <>[RestEntity[D]]
        (
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

  class CategoryTable(tag: Tag) extends RestEntityTable[Category](tag, "categories") {
    val parentCategoryId = column[Option[Long]]("parentCategoryId")
    val name = column[String]("name")

    override protected def dataShape: ProvenShape[Category] = (parentCategoryId, name) <> (Category.tupled, Category.unapply)
  }

  class PostTable(tag: Tag) extends RestEntityTable[Post](tag, "posts") {
    val authorId = column[Long]("authorId")
    val postedAt = column[Timestamp]("postedAt")
    val message = column[String]("message")

    override protected def dataShape: ProvenShape[Post] = (authorId, postedAt, message) <>[Post] (
      p => Post(p._1, p._2, p._3),
      (p: Post) => Some((p.authorId, dateTime2timestamp(p.postedAt), p.message))
    )
  }

  class ThreadTable(tag: Tag) extends RestEntityTable[Thread](tag, "threads") {
    val categoryId = column[Long]("categoryId")
    val rootPostId = column[Long]("rootPostId")
    val title = column[String]("title")

    override protected def dataShape: ProvenShape[Thread] = (categoryId, rootPostId, title) <> (Thread.tupled, Thread.unapply)
  }

  class ThreadPostTable(tag: Tag) extends RestEntityTable[ThreadPost](tag, "threadPosts") {
    val threadId = column[Long]("threadId")
    val postId = column[Long]("postId")

    override protected def dataShape: ProvenShape[ThreadPost] = (threadId, postId) <> (ThreadPost.tupled, ThreadPost.unapply)
  }

  private implicit def timestamp2dateTime(timestamp: Timestamp): DateTime = new DateTime(timestamp.toInstant.toEpochMilli)
  private implicit def dateTime2timestamp(dateTime: DateTime): Timestamp = new Timestamp(dateTime.getMillis)
  private implicit def dataType[D <: Model](implicit classTag: ClassTag[D]): TypedType[D] = ScalaBaseType[D]

  implicit val users: TableQuery[UserTable] = TableQuery[UserTable]
  implicit val categories: TableQuery[CategoryTable] = TableQuery[CategoryTable]
  implicit val posts: TableQuery[PostTable] = TableQuery[PostTable]
  implicit val threads: TableQuery[ThreadTable] = TableQuery[ThreadTable]
  implicit val threadPosts: TableQuery[ThreadPostTable] = TableQuery[ThreadPostTable]
}
