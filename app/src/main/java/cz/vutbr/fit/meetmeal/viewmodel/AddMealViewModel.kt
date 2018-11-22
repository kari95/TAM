package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import org.joda.time.*
import java.util.regex.*

class AddMealViewModel(application: Application): AndroidViewModel(application) {

  val name: ObservableField<String> = ObservableField()
  val nameError: ObservableField<String> = ObservableField()
  val peopleCount: ObservableField<Int> = ObservableField()
  val price: ObservableField<Int> = ObservableField()
  val female: ObservableBoolean = ObservableBoolean(false)
  val male: ObservableBoolean = ObservableBoolean(false)
  val booth: ObservableBoolean = ObservableBoolean(false)
  val address: ObservableField<String> = ObservableField()
  val addressError: ObservableField<String> = ObservableField()
  val time: ObservableField<DateTime> = ObservableField()

  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  private val mealEngine = MealEngine()
  private val userEngine = UserEngine()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
  }

  fun onTimeSelected(hour: Int, minute: Int) {
    val old = time.get() ?: DateTime.now()
    time.set(
      old.withHourOfDay(hour).withMinuteOfHour(minute)
    )
  }

  fun onDateSelected(year: Int, month: Int, day: Int) {
    val old = time.get() ?: DateTime.now()
    time.set(
      old.withYear(year).withMonthOfYear(month).withDayOfMonth(day)
    )
  }

  fun onSaveClick() {
    val nameOk = validateName()

    /*val meal = Meal(name, time, gender, user, peopleCount, price, address)
    mealEngine.add(meal)*/
  }

  fun validateName(): Boolean {
    val name = name.get()
    nameError.set(when {
      name == null || name.isEmpty() -> getString(R.string.add_meal_event_name_required)
      else -> null
    })
    return nameError.get() == null
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}