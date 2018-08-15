package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.models.Entity

trait RestRepository[E <: Entity] extends Repository[Long, E]
