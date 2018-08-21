package nullpointer.akkawebapi.json

import org.joda.time.DateTime
import spray.json._

class DateTimeJsonFormatSpec extends JsonFormatSpec {
  import DateTimeJsonFormatSpec._

  describe("A DateTimeJsonFormat") {
    it("must write correct year value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("year") mustBe Some(Year)
    }

    it("must write correct month of year value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("monthOfYear") mustBe Some(MonthOfYear)
    }

    it("must write correct day of month value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("dayOfMonth") mustBe Some(DayOfMonth)
    }

    it("must write correct hour of day value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("hourOfDay") mustBe Some(HourOfDay)
    }

    it("must write correct minute of hour value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("minuteOfHour") mustBe Some(MinuteOfHour)
    }

    it("must write correct second of minute value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("secondOfMinute") mustBe Some(SecondOfMinute)
    }

    it("must write correct millis of second value") {
      val json = dateTimeToJson(TestDateTime)
      json.intField("millisOfSecond") mustBe Some(MillisOfSecond)
    }

    it("must read correct year value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getYear mustBe Year
    }

    it("must read correct month of year value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getMonthOfYear mustBe MonthOfYear
    }

    it("must read correct day of month value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getDayOfMonth mustBe DayOfMonth
    }

    it("must read correct hour of day value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getHourOfDay mustBe HourOfDay
    }

    it("must read correct minute of hour value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getMinuteOfHour mustBe MinuteOfHour
    }

    it("must read correct second of minute value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getSecondOfMinute mustBe SecondOfMinute
    }

    it("must read correct millis of second value") {
      val dateTime = jsonToDateTime(TestDateTimeJsonObject)
      dateTime.getMillisOfSecond mustBe MillisOfSecond
    }
  }
}

private object DateTimeJsonFormatSpec extends BasicFormats {
  lazy val Year: Int = 2015
  lazy val MonthOfYear: Int = 10
  lazy val DayOfMonth: Int = 25
  lazy val HourOfDay: Int = 19
  lazy val MinuteOfHour: Int = 25
  lazy val SecondOfMinute: Int = 38
  lazy val MillisOfSecond: Int = 150
  lazy val TestDateTime: DateTime = new DateTime(
    Year,
    MonthOfYear,
    DayOfMonth,
    HourOfDay,
    MinuteOfHour,
    SecondOfMinute,
    MillisOfSecond
  )
  lazy val TestDateTimeJsonObject: JsObject = JsObject(
    "year" -> Year.toJson,
    "monthOfYear" -> MonthOfYear.toJson,
    "dayOfMonth" -> DayOfMonth.toJson,
    "hourOfDay" -> HourOfDay.toJson,
    "minuteOfHour" -> MinuteOfHour.toJson,
    "secondOfMinute" -> SecondOfMinute.toJson,
    "millisOfSecond" -> MillisOfSecond.toJson
  )

  def dateTimeToJson(dateTime: DateTime): JsObject =
    DateTimeJsonFormat().write(dateTime).asJsObject

  def jsonToDateTime(json: JsValue): DateTime =
    DateTimeJsonFormat().read(json)

  implicit class JsObjectWithIntField(jsonObject: JsObject) extends BasicFormats {
    def intField(fieldName: String): Option[Int] =
      jsonObject.fields.get(fieldName).map(_.convertTo[Int])
  }
}
