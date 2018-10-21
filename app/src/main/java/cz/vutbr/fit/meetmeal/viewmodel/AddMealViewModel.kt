package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.arch.lifecycle.*
import android.app.DatePickerDialog
import android.widget.*
import java.util.*
import java.text.*
import android.R.attr.startYear
import cz.vutbr.fit.meetmeal.activity.MainActivity



class AddMealViewModel(app: Application): AndroidViewModel(app) {


  fun createDateCalendar(): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    calendar.set(Calendar.YEAR, 0, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar
  }

  fun createCalendar(year: Int, monthOfYear: Int, dayOfMonth: Int): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    calendar.set(year, monthOfYear, dayOfMonth)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar
  }

  fun isMealDateValid(mealDate: Long?): Boolean {

    if (mealDate == null) {
      return false
    }

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = mealDate * 1000L

    return calendar.before(Calendar.getInstance(TimeZone.getTimeZone("UTC")))
  }
}