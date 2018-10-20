package cz.vutbr.fit.meetmeal.activity

import android.arch.lifecycle.*
import android.databinding.*
import android.support.v7.app.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.viewmodel.*
import cz.vutbr.fit.meetmeal.databinding.*
import kotlinx.android.synthetic.main.add_meal.*
import android.os.Bundle
import android.widget.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import cz.vutbr.fit.meetmeal.model.*
import java.util.*

class AddMealActivity : AppCompatActivity() {

  private val MEAL_DATE = "meal_date"

  lateinit var binding: AddMealBinding

  lateinit var viewModel: AddMealViewModel

  val newMeal: Meal? = null
  var mealTime = 0L

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.add_meal)
    viewModel = ViewModelProviders.of(this).get(AddMealViewModel::class.java)

    addListeners()
  }

  private fun updateViews() {
    removeListeners()

    add_meal_date_edit_text.text = Date(mealTime).toString()

    addListeners()
  }

  private fun addListeners() {
    add_meal_date_edit_text.setOnClickListener { view ->
      val mealDate = createDateCalendar()
      val dialog = DatePickerDialog.newInstance(
        { dialogView, year, monthOfYear, dayOfMonth ->
          val calendar = createCalendar(year, monthOfYear, dayOfMonth)
          mealTime = (calendar.getTimeInMillis() / 1000L)

          doValidateDateField(add_meal_date_edit_text, mealTime)
          updateViews()
        },
        mealDate.get(Calendar.YEAR),
        mealDate.get(Calendar.MONTH),
        mealDate.get(Calendar.DAY_OF_MONTH)
      )
      dialog.setOnDismissListener(
        { dialogView -> doValidateDateField(add_meal_date_edit_text, mealTime) }
      )
      dialog.setMaxDate(Calendar.getInstance()) // Sets maximal supported date of birth (today).
      dialog.show(this.fragmentManager, MEAL_DATE)
    }
  }

  private fun removeListeners() {
    //TODO fix memory leaks
  }


  private fun createDateCalendar(): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

      calendar.set(Calendar.YEAR, 0, 1)
      calendar.set(Calendar.HOUR_OF_DAY, 0)
      calendar.set(Calendar.MINUTE, 0)
      calendar.set(Calendar.SECOND, 0)
      calendar.set(Calendar.MILLISECOND, 0)

    return calendar
  }

  private fun createCalendar(year: Int, monthOfYear: Int, dayOfMonth: Int): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    calendar.set(year, monthOfYear, dayOfMonth)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar
  }

  private fun doValidateDateField(view: TextView, timestamp: Long?) {
    if (isMealDateValid(timestamp)) {
      view.error = null
    } else {
      view.error = resources.getString(R.string.add_meal_date_incorrect_value)
    }
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
