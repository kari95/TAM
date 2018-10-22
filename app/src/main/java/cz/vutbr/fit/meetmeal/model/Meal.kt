package cz.vutbr.fit.meetmeal.model

import android.os.*
import org.joda.time.*
import org.joda.time.format.*

class Meal (
  val id: Int = 0,
  val name: String,
  val time: DateTime,
  val gender: User.Gender = User.Gender.BOOTH,
  var user: User?,
  val peopleCount: Int = 0,
  val price: Int = 0,
  val address: Address = Address()
): Parcelable {
  val formatedTime: String
    get() {
      val formater = DateTimeFormat.forPattern("d. MMMM HH:mm")
      return time.toString(formater)
    }

  constructor(parcel: Parcel): this(
    parcel.readInt(),
    parcel.readString(),
    TODO("time"),
    TODO("gender"),
    TODO("user"),
    parcel.readInt(),
    parcel.readInt(),
    TODO("address")) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeInt(id)
    parcel.writeString(name)
    parcel.writeInt(peopleCount)
    parcel.writeInt(price)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR: Parcelable.Creator<Meal> {
    override fun createFromParcel(parcel: Parcel): Meal {
      return Meal(parcel)
    }

    override fun newArray(size: Int): Array<Meal?> {
      return arrayOfNulls(size)
    }
  }
}
