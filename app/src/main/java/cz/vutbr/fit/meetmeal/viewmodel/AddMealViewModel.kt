package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.*
import com.google.firebase.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import org.joda.time.*
import java.util.*

class AddMealViewModel: BaseObservable() {

  private val mealEngine = MealEngine()

  var name: String = ""
    @Bindable
    get
    set(value) {
      field = value
      notifyChange()
    }
  var peopleCount: Int = 0
    @Bindable
    get
    set(value) {
      field = value
      notifyChange()
    }

  var price: Int = 0
    @Bindable
    get
    set(value) {
      field = value
      notifyChange()
    }

  var address: String = ""
    @Bindable
    get
    set(value) {
      field = value
      notifyChange()
    }

  var time: Timestamp = Timestamp.now()
  var gender: User.Gender = User.Gender.BOTH
  var user: User = User()
  var hour: Int = 0
  var minute: Int = 0
  var date: Timestamp = Timestamp.now()

  fun createDateCalendar(): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
    calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

    return calendar
  }

  fun createCalendar(year: Int, monthOfYear: Int, dayOfMonth: Int): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    calendar.set(year, monthOfYear, dayOfMonth)

    return calendar
  }

  fun isMealDateValid(mealDate: Long?): Boolean {

    if (mealDate == null) {
      return false
    }

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = (mealDate * 1000L) + 5000
    return calendar.after(Calendar.getInstance(TimeZone.getTimeZone("UTC")))
  }

  fun onSaveClick() {
    var dateTime = DateTime(date.seconds * 1000).withTimeAtStartOfDay()
    dateTime = dateTime.withHourOfDay(hour)
    dateTime = dateTime.withMinuteOfHour(minute)

    val timestampDate = Date(dateTime.millis)
    time = Timestamp(timestampDate)

    val meal = Meal(name, time, gender, user, peopleCount, price, address)
    mealEngine.add(meal)
  }

  fun onGenderButtonClick(number: Int) {
    gender = when (number) {
      1 -> {
        User.Gender.BOTH
      }
      2 -> {
        User.Gender.MALE
      }
      3 -> {
        User.Gender.FEMALE
      }
      else -> {
        User.Gender.BOTH
      }
    }
  }
}