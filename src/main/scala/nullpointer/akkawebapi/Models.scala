package nullpointer.akkawebapi

import org.joda.time.DateTime

object Models {
  case class User(name: String, password: String)

  case class Category(parentCategoryId: Option[Long], name: String)

  case class Thread(categoryId: Long, rootPostId: Long, title: String)

  case class Post(authorId: Long, postedAt: DateTime, message: String)
}
