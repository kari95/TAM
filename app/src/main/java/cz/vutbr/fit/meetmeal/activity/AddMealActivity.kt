package cz.vutbr.fit.meetmeal.activity

import android.arch.lifecycle.*
import android.databinding.*
import android.support.v7.app.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.viewmodel.*
import cz.vutbr.fit.meetmeal.databinding.*
import kotlinx.android.synthetic.main.add_meal.*
import android.os.Bundle
import android.text.format.*
import android.widget.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import cz.vutbr.fit.meetmeal.model.*
import java.text.*
import java.text.DateFormat
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

    add_meal_date_edit_text.text = DateUtils.getRelativeTimeSpanString(mealTime * 1000, TimeZone.getDefault().getRawOffset().toLong(), DateUtils.DAY_IN_MILLIS);
    addListeners()
  }

  private fun addListeners() {
    add_meal_date_edit_text.setOnClickListener { view ->
      val mealDate = viewModel.createDateCalendar()
      val dialog = DatePickerDialog.newInstance(
        { dialogView, year, monthOfYear, dayOfMonth ->
          val calendar = viewModel.createCalendar(year, monthOfYear, dayOfMonth)
          mealTime = (calendar.getTimeInMillis() / 1000L)

          doValidateDateField(add_meal_date_edit_text, mealTime)
          updateViews()
        },
        mealDate.get(Calendar.YEAR),
        mealDate.get(Calendar.MONTH),
        mealDate.get(Calendar.DAY_OF_MONTH)
      )
      dialog.setOnDismissListener { dialogView -> doValidateDateField(add_meal_date_edit_text, mealTime) }
      dialog.minDate = Calendar.getInstance()
      dialog.show(this.fragmentManager, MEAL_DATE)
    }
  }

  private fun removeListeners() {
    //TODO fix memory leaks
  }

  fun doValidateDateField(view: TextView, timestamp: Long?) {
    if (viewModel.isMealDateValid(timestamp)) {
      view.error = null
    } else {
      view.error = resources.getString(R.string.add_meal_date_incorrect_value)
    }
  }

}
