package nullpointer.akkawebapi.json

import org.joda.time.DateTime
import spray.json._

case class DateTimeJsonFormat() extends RootJsonFormat[DateTime] with BasicFormats {
  override def read(json: JsValue): DateTime = {
    val jsonObject = json.asJsObject
    val year = jsonObject.fields("year").convertTo[Int]
    val monthOfYear = jsonObject.fields("monthOfYear").convertTo[Int]
    val dayOfMonth = jsonObject.fields("dayOfMonth").convertTo[Int]
    val hourOfDay = jsonObject.fields("hourOfDay").convertTo[Int]
    val minuteOfHour = jsonObject.fields("minuteOfHour").convertTo[Int]
    val secondOfMinute = jsonObject.fields("secondOfMinute").convertTo[Int]
    val millisOfSecond = jsonObject.fields("millisOfSecond").convertTo[Int]
    new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond)
  }

  override def write(obj: DateTime): JsValue = JsObject(
    "year" -> obj.getYear.toJson,
    "monthOfYear" -> obj.getMonthOfYear.toJson,
    "dayOfMonth" -> obj.getDayOfMonth.toJson,
    "hourOfDay" -> obj.getHourOfDay.toJson,
    "minuteOfHour" -> obj.getMinuteOfHour.toJson,
    "secondOfMinute" -> obj.getSecondOfMinute.toJson,
    "millisOfSecond" -> obj.getMillisOfSecond.toJson
  )
}
