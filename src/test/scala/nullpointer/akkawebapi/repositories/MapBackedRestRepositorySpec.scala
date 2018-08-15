package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.models.Entity
import nullpointer.akkawebapi.repositories.MapBackedRestRepositorySpec.TestEntity

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

import scala.concurrent.ExecutionContext.Implicits.global

object MapBackedRestRepositorySpec {
  def await[T](future: Future[T]): T = Await.result(future, 5 seconds)

  final case class TestEntity(override val id: Option[Long], data: String) extends Entity
}

class MapBackedRestRepositorySpec extends RepositorySpec {
  import MapBackedRestRepositorySpec._

  describe("A MapBackedRestRepository") {
    it("must have added item to map on add") {
      val repository: MapBackedRestRepository[TestEntity] = new MapBackedRestRepository()
      val item = TestEntity(None, "some test data")
      val addedItem = await(repository.add(item))
      val itemFromRepositoryOption = await(repository.getById(addedItem.id.get))
      itemFromRepositoryOption.isDefined mustBe true
      itemFromRepositoryOption.get mustBe addedItem
    }

    it("must return correct added items on getAll") {
      val repository: MapBackedRestRepository[TestEntity] = new MapBackedRestRepository()
      val items = Seq(
        TestEntity(None, "first item data"),
        TestEntity(None, "second item data"),
        TestEntity(None, "third item data")
      )
      val addedItems = await(Future.sequence(items.map(repository.add)))
      val itemsFromRepository = await(repository.getAll)
      addedItems must contain theSameElementsAs itemsFromRepository
    }

    it("must not contain deleted element") {
      val repository: MapBackedRestRepository[TestEntity] = new MapBackedRestRepository()
      val item = TestEntity(None, "some item data")
      val addedItem = await(repository.add(item))
      val addedItemId = addedItem.id.get
      await(repository.deleteById(addedItemId)) mustBe true
      val itemFromRepository = await(repository.getById(addedItemId))
      itemFromRepository.isEmpty mustBe true
    }

    it("must contain updated item") {
      val repository: MapBackedRestRepository[TestEntity] = new MapBackedRestRepository()
      val item = TestEntity(None, "some item data")
      val addedItem = await(repository.add(item))
      val updatedItem = addedItem.copy(data = "another item data")
      await(repository.update(updatedItem))
      val itemFromRepository = await(repository.getById(updatedItem.id.get))
      itemFromRepository mustBe updatedItem
    }

    it("must return  false on delete when item with given id does not exist") {
      val repository: MapBackedRestRepository[TestEntity] = new MapBackedRestRepository()
      await(repository.deleteById(1)) mustBe false
    }
  }
}
