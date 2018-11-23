package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import org.joda.time.*
import org.joda.time.format.*
import java.time.format.*
import java.util.regex.*

class AddMealViewModel(application: Application): AndroidViewModel(application) {

  val message: ObservableField<String> = ObservableField()

  val name: ObservableField<String> = ObservableField()
  val nameError: ObservableField<String> = ObservableField()
  val peopleCount: ObservableField<Int> = ObservableField()
  val peopleCountError: ObservableField<String> = ObservableField()
  val price: ObservableField<Int> = ObservableField()
  val priceError: ObservableField<String> = ObservableField()
  val female: ObservableBoolean = ObservableBoolean(false)
  val male: ObservableBoolean = ObservableBoolean(false)
  val booth: ObservableBoolean = ObservableBoolean(true)
  val address: ObservableField<String> = ObservableField()
  val addressError: ObservableField<String> = ObservableField()
  val time: ObservableField<DateTime> = ObservableField()
  val timeSelected: ObservableBoolean = ObservableBoolean(false)
  val dateSelected: ObservableBoolean = ObservableBoolean(false)

  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

val timeFormatter =  DateTimeFormat.forPattern("HH:mm")
val dateFormatter =  DateTimeFormat.forPattern("dd. MM. yyyy")

  private val mealEngine = MealEngine()
  private val userEngine = UserEngine()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
  }

  fun onTimeSelected(hour: Int, minute: Int) {
    val old = time.get() ?: DateTime.now().plusHours(5)
    time.set(
      old.withHourOfDay(hour).withMinuteOfHour(minute)
    )
    timeSelected.set(true)
  }

  fun onDateSelected(year: Int, month: Int, day: Int) {
    val old = time.get() ?: DateTime.now().plusHours(5)
    time.set(
      old.withYear(year).withMonthOfYear(month).withDayOfMonth(day)
    )
    dateSelected.set(true)
  }

  fun onSaveClick() {
    val nameOk = validateName()
    val priceOk = validatePrice()
    val addressOk = validateAddress()
    val peopleCountOk = validateCount()
    val restOk = validateTime() && validateGender()

    /*val meal = Meal(name, time, gender, user, peopleCount, price, address)
    mealEngine.add(meal)*/
  }

  fun validateName(): Boolean {
    val name = name.get()
    nameError.set(when {
      name == null || name.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return nameError.get() == null
  }

  fun validatePrice(): Boolean {
    val price = price.get()
    priceError.set(when {
      price == null || price <= 0 -> getString(R.string.required_field)
      else -> null
    })
    return priceError.get() == null
  }

  fun validateAddress(): Boolean {
    val name = name.get()
    addressError.set(when {
      name == null || name.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return addressError.get() == null
  }

  fun validateCount(): Boolean {
    val peopleCount = peopleCount.get()
    peopleCountError.set(when {
      peopleCount == null || peopleCount <= 0 -> getString(R.string.required_field)
      else -> null
    })
    return peopleCountError.get() == null
  }

  fun validateTime(): Boolean {
    val time = time.get()
    val timeSelected = timeSelected.get()
    val dateSelected = dateSelected.get()
    message.set(null)
    message.set(when {
      time == null || !timeSelected || !dateSelected -> getString(R.string.registration_time_required)
      time.isBeforeNow  -> getString(R.string.registration_time_before_now)
      else -> null
    })
    return message.get() == null
  }

  fun validateGender(): Boolean {
    val female = female.get()
    val male = male.get()
    val booth = booth.get()
    message.set(null)
    message.set(when {
      !female && !male && !booth -> getString(R.string.registration_gender_required)
      else -> null
    })
    return message.get() == null
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}