package nullpointer.akkawebapi

import org.joda.time.DateTime

object Models {
  sealed trait Model

  case class User(name: String, password: String) extends Model

  case class Category(parentCategoryId: Option[Long], name: String) extends Model

  case class Post(authorId: Long, postedAt: DateTime, message: String) extends Model

  case class Thread(categoryId: Long, rootPostId: Long, title: String) extends Model

  case class ThreadPost(threadId: Long, postId: Long) extends Model
}
