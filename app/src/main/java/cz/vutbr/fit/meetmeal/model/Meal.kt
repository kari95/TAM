package cz.vutbr.fit.meetmeal.model

import com.google.firebase.*
import com.google.firebase.firestore.*
import org.joda.time.*
import org.joda.time.format.*

data class Meal(
  var name: String = "",
  var time: Timestamp = Timestamp.now(),
  var gender: User.Gender = User.Gender.BOTH,
  var userId: String = "",
  var groupId: String = "",
  var peopleCount: Int = 0,
  var price: Int = 0,
  var address: String = ""
) {

  val joinedUsers = ArrayList<String>()

  @Exclude @set:Exclude @get:Exclude
  var id: String = ""

  val dateTime: DateTime
    @Exclude get() = DateTime(time.toDate())

  val formattedTime: String
    @Exclude get() {
      val formatter = DateTimeFormat.forPattern("d. MMMM HH:mm")
      return dateTime.toString(formatter)
    }

  val freeSpaces: Int
    @Exclude get() = peopleCount - joinedUsers.size
}
