package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.*
import com.google.firebase.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
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
  var gender: User.Gender = User.Gender.BOOTH
  var user: User = User()

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
    val meal = Meal(name, time, gender, user, peopleCount, price, address)
    mealEngine.add(meal)
    //MealListFragmentDirections.actionAddMeal()
    /*getTestingData().subscribe { it ->
      mealEngine.add(it)
    }*/
  }
}