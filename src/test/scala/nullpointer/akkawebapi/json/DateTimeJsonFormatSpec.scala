package nullpointer.akkawebapi.json

import org.joda.time.DateTime
import org.scalatest.{FunSpec, MustMatchers}
import spray.json.{BasicFormats, JsNumber, JsObject}

class DateTimeJsonFormatSpec extends JsonFormatSpec {
  describe("A DateTimeJsonFormat") {
    it("must write correct year value") {
      val format = DateTimeJsonFormat()
      val expectedYear = 2015
      val dateTime = new DateTime(expectedYear, 10, 25, 19, 25, 38, 150)
      val json = format.write(dateTime).asJsObject
      val year = json.fields.get("year").map(_.convertTo[Int])
      year.isDefined mustBe true
      year.get mustBe expectedYear
    }

    it("must write correct month of year value") {
      val format = DateTimeJsonFormat()
      val expectedMonthOfYear = 10
      val dateTime = new DateTime(2015, expectedMonthOfYear, 25, 19, 25, 38, 150)
      val json = format.write(dateTime).asJsObject
      val monthOfYear = json.fields.get("monthOfYear").map(_.convertTo[Int])
      monthOfYear.isDefined mustBe true
      monthOfYear.get mustBe expectedMonthOfYear
    }

    it("must write correct day of month value") {
      val format = DateTimeJsonFormat()
      val expectedDayOfMonth = 25
      val dateTime = new DateTime(2015, 10, expectedDayOfMonth, 19, 25, 38, 150)
      val json = format.write(dateTime).asJsObject
      val dayOfMonth = json.fields.get("dayOfMonth").map(_.convertTo[Int])
      dayOfMonth.isDefined mustBe true
      dayOfMonth.get mustBe expectedDayOfMonth
    }

    it("must write correct hour of day value") {
      val format = DateTimeJsonFormat()
      val expectedHourOfDay = 19
      val dateTime = new DateTime(2015, 10, 25, expectedHourOfDay, 25, 38, 150)
      val json = format.write(dateTime).asJsObject
      val hourOfDay = json.fields.get("hourOfDay").map(_.convertTo[Int])
      hourOfDay.isDefined mustBe true
      hourOfDay.get mustBe expectedHourOfDay
    }

    it("must write correct minute of hour value") {
      val format = DateTimeJsonFormat()
      val expectedMinuteOfHour = 25
      val dateTime = new DateTime(2015, 10, 25, 19, expectedMinuteOfHour, 38, 150)
      val json = format.write(dateTime).asJsObject
      val minuteOfHour = json.fields.get("minuteOfHour").map(_.convertTo[Int])
      minuteOfHour.isDefined mustBe true
      minuteOfHour.get mustBe expectedMinuteOfHour
    }

    it("must write correct second of minute value") {
      val format = DateTimeJsonFormat()
      val expectedSecondOfMinute = 38
      val dateTime = new DateTime(2015, 10, 25, 19, 25, expectedSecondOfMinute, 150)
      val json = format.write(dateTime).asJsObject
      val secondOfMinute = json.fields.get("secondOfMinute").map(_.convertTo[Int])
      secondOfMinute.isDefined mustBe true
      secondOfMinute.get mustBe expectedSecondOfMinute
    }

    it("must write correct millis of second value") {
      val format = DateTimeJsonFormat()
      val expectedMillisOfSecond = 150
      val dateTime = new DateTime(2015, 10, 25, 19, 25, 38, expectedMillisOfSecond)
      val json = format.write(dateTime).asJsObject
      val millisOfSecond = json.fields.get("millisOfSecond").map(_.convertTo[Int])
      millisOfSecond.isDefined mustBe true
      millisOfSecond.get mustBe expectedMillisOfSecond
    }

    it("must read correct year value") {
      val format = DateTimeJsonFormat()
      val expectedYear = 2015
      val json = JsObject(
        "year" -> JsNumber(expectedYear),
        "monthOfYear" -> JsNumber(10),
        "dayOfMonth" -> JsNumber(25),
        "hourOfDay" -> JsNumber(19),
        "minuteOfHour" -> JsNumber(25),
        "secondOfMinute" -> JsNumber(38),
        "millisOfSecond" -> JsNumber(150)
      )
      val dateTime = format.read(json)
      dateTime.getYear mustBe expectedYear
    }

    it("must read correct month of year value") {
      val format = DateTimeJsonFormat()
      val expectedMonthOfYear = 10
      val json = JsObject(
        "year" -> JsNumber(2015),
        "monthOfYear" -> JsNumber(expectedMonthOfYear),
        "dayOfMonth" -> JsNumber(25),
        "hourOfDay" -> JsNumber(19),
        "minuteOfHour" -> JsNumber(25),
        "secondOfMinute" -> JsNumber(38),
        "millisOfSecond" -> JsNumber(150)
      )
      val dateTime = format.read(json)
      dateTime.getMonthOfYear mustBe expectedMonthOfYear
    }

    it("must read correct day of month value") {
      val format = DateTimeJsonFormat()
      val expectedDayOfMonth = 25
      val json = JsObject(
        "year" -> JsNumber(2015),
        "monthOfYear" -> JsNumber(10),
        "dayOfMonth" -> JsNumber(expectedDayOfMonth),
        "hourOfDay" -> JsNumber(19),
        "minuteOfHour" -> JsNumber(25),
        "secondOfMinute" -> JsNumber(38),
        "millisOfSecond" -> JsNumber(150)
      )
      val dateTime = format.read(json)
      dateTime.getDayOfMonth mustBe expectedDayOfMonth
    }

    it("must read correct hour of day value") {
      val format = DateTimeJsonFormat()
      val expectedHourOfDay = 19
      val json = JsObject(
        "year" -> JsNumber(2015),
        "monthOfYear" -> JsNumber(10),
        "dayOfMonth" -> JsNumber(25),
        "hourOfDay" -> JsNumber(expectedHourOfDay),
        "minuteOfHour" -> JsNumber(25),
        "secondOfMinute" -> JsNumber(38),
        "millisOfSecond" -> JsNumber(150)
      )
      val dateTime = format.read(json)
      dateTime.getHourOfDay mustBe expectedHourOfDay
    }

    it("must read correct minute of hour value") {
      val format = DateTimeJsonFormat()
      val expectedMinuteOfHour = 25
      val json = JsObject(
        "year" -> JsNumber(2015),
        "monthOfYear" -> JsNumber(10),
        "dayOfMonth" -> JsNumber(25),
        "hourOfDay" -> JsNumber(19),
        "minuteOfHour" -> JsNumber(expectedMinuteOfHour),
        "secondOfMinute" -> JsNumber(38),
        "millisOfSecond" -> JsNumber(150)
      )
      val dateTime = format.read(json)
      dateTime.getMinuteOfHour mustBe expectedMinuteOfHour
    }

    it("must read correct second of minute value") {
      val format = DateTimeJsonFormat()
      val expectedSecondOfMinute = 38
      val json = JsObject(
        "year" -> JsNumber(2015),
        "monthOfYear" -> JsNumber(10),
        "dayOfMonth" -> JsNumber(25),
        "hourOfDay" -> JsNumber(19),
        "minuteOfHour" -> JsNumber(25),
        "secondOfMinute" -> JsNumber(expectedSecondOfMinute),
        "millisOfSecond" -> JsNumber(150)
      )
      val dateTime = format.read(json)
      dateTime.getSecondOfMinute mustBe expectedSecondOfMinute
    }

    it("must read correct millis of second value") {
      val format = DateTimeJsonFormat()
      val expectedMillisOfSecond = 150
      val json = JsObject(
        "year" -> JsNumber(2015),
        "monthOfYear" -> JsNumber(10),
        "dayOfMonth" -> JsNumber(25),
        "hourOfDay" -> JsNumber(19),
        "minuteOfHour" -> JsNumber(25),
        "secondOfMinute" -> JsNumber(38),
        "millisOfSecond" -> JsNumber(expectedMillisOfSecond)
      )
      val dateTime = format.read(json)
      dateTime.getMillisOfSecond mustBe expectedMillisOfSecond
    }
  }
}
