package nullpointer.akkawebapi.repositories

import nullpointer.akkawebapi.Entities.{Entity, RestEntity}
import nullpointer.akkawebapi.db.DatabaseConfiguration
import nullpointer.akkawebapi.db.Tables.RestEntityTable
import nullpointer.akkawebapi.exceptions.RepositoryExceptions.AbsentIdRepositoryException
import nullpointer.akkawebapi.repositories.SlickBackedRestRepository.SlickBackedRestRepository
import slick.ast.{ScalaBaseType, TypedType}
import slick.lifted.ProvenShape

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class SlickBackedRestRepositorySpec extends RepositorySpec {
  import SlickBackedRestRepositorySpec._

  override def beforeAll(): Unit = initializeDatabase()

  describe("A SlickBackedRestRepository") {
    it("must return correct items from repository") {
      val repository: SlickBackedRestRepository[TestClass, TestClassTable] = new SlickBackedRestRepository[TestClass, TestClassTable](database)
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
      val repository: SlickBackedRestRepository[TestClass, TestClassTable] = new SlickBackedRestRepository[TestClass, TestClassTable](database)
      val data = TestClass("some data")
      val addedData = await(repository.add(data))
      val dataFromRepository = await(repository.getById(addedData.id.get))
      dataFromRepository.isDefined mustBe true
      dataFromRepository.get mustBe addedData
    }

    it("must have updated item") {
      val repository: SlickBackedRestRepository[TestClass, TestClassTable] = new SlickBackedRestRepository[TestClass, TestClassTable](database)
      val data = TestClass("some data")
      val addedData = await(repository.add(data))
      val updatedData = addedData.copy(data = TestClass("some another data"))
      await(repository.update(updatedData))
      val dataFromRepository = await(repository.getById(addedData.id.get))
      dataFromRepository.isDefined mustBe true
      dataFromRepository.get mustBe updatedData
    }

    it("must not have deleted item") {
      val repository: SlickBackedRestRepository[TestClass, TestClassTable] = new SlickBackedRestRepository[TestClass, TestClassTable](database)
      val data = TestClass("some data")
      val addedData = await(repository.add(data))
      val addedDataId = addedData.id.get
      await(repository.deleteById(addedDataId))
      val dataFromRepository = await(repository.getById(addedDataId))
      dataFromRepository.isEmpty mustBe true
    }

    it("must throw AbsentIdRepositoryException when id is absent on update") {
      val repository: SlickBackedRestRepository[TestClass, TestClassTable] = new SlickBackedRestRepository[TestClass, TestClassTable](database)
      val entityWithAbsentKey: RestEntity[TestClass] = Entity(None, TestClass("some test data"))
      an[AbsentIdRepositoryException] mustBe thrownBy (await(repository.update(entityWithAbsentKey)))
    }
  }
}

object SlickBackedRestRepositorySpec extends DatabaseConfiguration {
  import config.profile.api._

  def await[T](future: Future[T]): T = Await.result(future, 5 seconds)

  case class TestClass(data: String)

  class TestClassTable(tag: Tag) extends RestEntityTable[TestClass](tag, "test") {
    val data = column[String]("data")

    override protected def dataShape: ProvenShape[TestClass] = data.mapTo[TestClass]
  }

  implicit lazy val testClassType: TypedType[TestClass] = ScalaBaseType[TestClass]
  implicit lazy val testClassQuery: TableQuery[TestClassTable] = TableQuery[TestClassTable]

  lazy val database: Database = Database.forConfig("db")

  def initializeDatabase(): Unit = await(database.run(testClassQuery.schema.create))
}
