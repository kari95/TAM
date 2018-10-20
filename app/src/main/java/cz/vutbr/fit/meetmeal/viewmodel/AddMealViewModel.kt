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


  fun onDateClick() {
    val calendar = Calendar.getInstance()


    val datePickerDialog = DatePicker(this.getApplication())

    val datePicker = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
      // TODO Auto-generated method stub
      calendar.set(Calendar.YEAR, year)
      calendar.set(Calendar.MONTH, monthOfYear)
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
      updateDate( calendar)
    }
  }

  private fun updateDate(calendar: Calendar) {
    val myFormat = "MM/dd/yy" //In which you need put here
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    //add_meal_date_edit_text.setText(sdf.format(calendar.time))
  }
}