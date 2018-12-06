package cz.vutbr.fit.meetmeal.model

import com.google.firebase.firestore.*

class Group(
  val name: String = ""
) {
  @Exclude @set:Exclude @get:Exclude
  var id: String = ""
}