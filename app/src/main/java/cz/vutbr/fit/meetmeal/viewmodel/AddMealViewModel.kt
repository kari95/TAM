package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.lifecycle.*
import java.util.*

class AddMealViewModel(app: Application): AndroidViewModel(app) {

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
}