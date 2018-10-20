package cz.vutbr.fit.meetmeal.model

import org.joda.time.*
import org.joda.time.format.*

class Meal (
  val id: Int = 0,
  val time: DateTime,
  val gender: User.Gender = User.Gender.BOOTH,
  var user: User?,
  val peoplesCount: Int = 0,
  val totalPrice: Int = 0,
  val address: Address = Address()
) {
  val formatedTime: String
    get() {
      val formater = DateTimeFormat.forPattern("d. MMMM HH:mm")
      return time.toString(formater)
    }
}
