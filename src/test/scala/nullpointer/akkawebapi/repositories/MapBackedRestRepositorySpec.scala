package nullpointer.akkawebapi.repositories

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

import scala.concurrent.ExecutionContext.Implicits.global

class MapBackedRestRepositorySpec extends RepositorySpec {
  import MapBackedRestRepositorySpec._

  describe("A map backed repository") {
    it("must return correct items from repository") {
      val repository: MapBackedRestRepository[TestClass] = new MapBackedRestRepository
      val items = Seq(
        TestClass("first item data"),
        TestClass("second item data"),
        TestClass("third item data")
      )
      val addedItems = await(Future.sequence(items.map(repository.add)))
      val itemsFromRepository = await(repository.getAll)
      itemsFromRepository must contain theSameElementsAs addedItems
    }

    it("must have added item") {
      val repository: MapBackedRestRepository[TestClass] = new MapBackedRestRepository
      val data = TestClass("some data")
      val addedData = await(repository.add(data))
      val dataFromRepository = await(repository.getById(addedData.id.get))
      dataFromRepository.isDefined mustBe true
      dataFromRepository.get mustBe addedData
    }

    it("must have updated item") {
      val repository: MapBackedRestRepository[TestClass] = new MapBackedRestRepository
      val data = TestClass("some data")
      val addedData = await(repository.add(data))
      val updatedData = addedData.copy(data = TestClass("some another data"))
      await(repository.update(updatedData))
      val dataFromRepository = await(repository.getById(addedData.id.get))
      dataFromRepository.isDefined mustBe true
      dataFromRepository.get mustBe updatedData
    }

    it("must not have deleted item") {
      val repository: MapBackedRestRepository[TestClass] = new MapBackedRestRepository
      val data = TestClass("some data")
      val addedData = await(repository.add(data))
      val addedDataId = addedData.id.get
      await(repository.deleteById(addedDataId))
      val dataFromRepository = await(repository.getById(addedDataId))
      dataFromRepository.isEmpty mustBe true
    }
  }
}

private object MapBackedRestRepositorySpec {
  def await[T](future: Future[T]): T = Await.result(future, 5 seconds)

  case class TestClass(data: String)
}
