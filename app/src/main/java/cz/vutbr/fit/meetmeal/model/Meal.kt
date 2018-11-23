package cz.vutbr.fit.meetmeal.model

import com.google.firebase.*
import com.google.firebase.firestore.*
import org.joda.time.*
import org.joda.time.format.*

data class Meal(
  var name: String = "",
  var time: Timestamp = Timestamp.now(),
  var gender: User.Gender = User.Gender.BOTH,
  @Exclude @set:Exclude @get:Exclude
  var user: User = User(),
  var peopleCount: Int = 0,
  var price: Int = 0,
  var address: String = ""
) {

  @Exclude @set:Exclude @get:Exclude
  var id: String = ""

  val userId: String
    get() = "user/" + user.id

  val dateTime: DateTime
    @Exclude get() = DateTime(time.toDate())

  val formatedTime: String
    @Exclude get() {
      val formater = DateTimeFormat.forPattern("d. MMMM HH:mm")
      return dateTime.toString(formater)
    }
}
