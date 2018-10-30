package cz.vutbr.fit.meetmeal.model

import cz.vutbr.fit.meetmeal.*

data class User(
  val name: String = "",
  val gender: Gender = Gender.UNKNOWN
) {
  enum class Gender(
    val value: String,
    val string: Int,
    val icon: Int
  ) {
    MALE(
      "male",
      R.string.male,
      R.drawable.ic_gender_male
    ),
    FEMALE(
      "female",
      R.string.female,
      R.drawable.ic_gender_female
    ),
    BOTH(
      "booth",
      R.string.both,
      R.drawable.ic_gender_male_female
    ),
    UNKNOWN(
      "unknown",
      R.string.unknown,
      R.drawable.ic_person
    )
  }
}