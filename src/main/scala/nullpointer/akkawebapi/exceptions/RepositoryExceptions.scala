package nullpointer.akkawebapi.exceptions

object RepositoryExceptions {
  sealed abstract class RepositoryException(message: String, cause: Throwable) extends Exception(message, cause)

  final case class PresentIdRepositoryException(message: String = null, cause: Throwable = null) extends RepositoryException(message, cause)
  final case class AbsentIdRepositoryException(message: String = null, cause: Throwable = null) extends RepositoryException(message, cause)
}
