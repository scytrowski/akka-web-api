package nullpointer.akkawebapi

object Entities {
  case class Entity[I, D](id: Option[I], data: D) {
    def withId(id: I): Entity[I, D] = copy(id = Some(id))
  }

  object Entity {
    def apply[I, D](id: I, data: D): Entity[I, D] = Entity(Some(id), data)

    def apply[I, D](data: D): Entity[I, D] = Entity(None, data)
  }

  type RestEntity[D] = Entity[Long, D]

  object RestEntity {
    def apply[D](id: Option[Long], data: D): RestEntity[D] = Entity(id, data)

    def apply[D](id: Long, data: D): RestEntity[D] = Entity(Some(id), data)

    def apply[D](data: D): RestEntity[D] = Entity(None, data)
  }
}
