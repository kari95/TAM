package cz.vutbr.fit.meetmeal.model

import org.joda.time.*
import org.joda.time.format.*

abstract class Meal (
  val id: Int,
  open val time: DateTime,
  val gender: User.Gender,
  var user: User?
)

class NowMeal (
  id: Int = 0,
  time: DateTime = DateTime.now(),
  user: User? = null,
  gender: User.Gender = User.Gender.BOOTH,
  val place: String = ""
): Meal(id, time, gender, user) {

  val formatedTime: String
    get() {
      val formatter = PeriodFormat.wordBased()
      val period = Duration(DateTime.now(), time)
        .toPeriod()
        .withSeconds(0)
        .withMillis(0)

      return formatter.print(
        if (period.minutes > 0)
          period
        else
          period.withMinutes(1)
      )
    }
}

class PlannedMeal (
  id: Int = 0,
  time: DateTime = DateTime.now(),
  gender: User.Gender = User.Gender.BOOTH,
  user: User? = null,
  val peoplesCount: Int = 0,
  val totalPrice: Int = 0,
  val address: Address = Address()
): Meal(id, time, gender, user) {

  val formatedTime: String
    get() {
      val formater = DateTimeFormat.forPattern("EEEE d. MMMM")
      return time.toString(formater)
    }
}