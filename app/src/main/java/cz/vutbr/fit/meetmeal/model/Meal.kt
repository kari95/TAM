package cz.vutbr.fit.meetmeal.model

import com.google.firebase.*
import com.google.firebase.firestore.*
import org.joda.time.*
import org.joda.time.format.*

data class Meal(
  var name: String = "",
  var time: Timestamp = Timestamp.now(),
  var gender: User.Gender = User.Gender.BOTH,
  var user: User = User(),
  var peopleCount: Int = 0,
  var price: Int = 0,
  var address: String = ""
) {

  @Exclude var id: String = ""

  val dateTime: DateTime
    get() = DateTime(time.toDate())

  val formatedTime: String
    get() {
      val formater = DateTimeFormat.forPattern("d. MMMM HH:mm")
      return dateTime.toString(formater)
    }
}
