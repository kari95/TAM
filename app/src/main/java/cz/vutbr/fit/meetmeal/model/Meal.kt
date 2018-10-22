package cz.vutbr.fit.meetmeal.model

import com.google.firebase.*
import org.joda.time.*
import org.joda.time.format.*

class Meal (
  var name: String = "",
  var time: Timestamp = Timestamp.now(),
  var gender: User.Gender = User.Gender.BOOTH,
  var user: User = User(),
  var peopleCount: Int = 0,
  var price: Int = 0,
  var address: Address = Address()
) {
  val formatedTime: String
    get() {
      val formater = DateTimeFormat.forPattern("d. MMMM HH:mm")
      val dateTime = DateTime(time.toDate())
      return dateTime.toString(formater)
    }
}
