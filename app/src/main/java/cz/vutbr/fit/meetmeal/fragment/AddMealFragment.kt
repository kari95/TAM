package cz.vutbr.fit.meetmeal.fragment

import android.app.*
import android.os.*
import android.text.format.*
import android.view.*
import android.widget.*
import androidx.databinding.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.*
import com.google.firebase.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*
import cz.vutbr.fit.meetmeal.viewmodel.*
import java.util.*

class AddMealFragment: Fragment() {

  private val MEAL_DATE = "meal_date"

  lateinit var binding: FragmentAddMealBinding

  var viewModel = AddMealViewModel()

  val newMeal: Meal? = null
  var mealTime = 0L

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_meal, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    binding.viewModel = viewModel

    addListeners()
  }

  private fun addListeners() {

    binding.addMealSaveButton.setOnClickListener { _ ->
      viewModel.onSaveClick()
      NavHostFragment.findNavController(this).navigateUp()
    }

    val onTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hour: Int, minute: Int ->
      viewModel.hour = hour
      viewModel.minute = minute
      binding.addMealTimeEditText.text = String.format(resources.getString(R.string.time), hour,
        minute)
    }

    binding.addMealTimeEditText.setOnClickListener { view ->
      val currentTime = Calendar.getInstance()
      val hour = currentTime.get(Calendar.HOUR_OF_DAY)
      val minute = currentTime.get(Calendar.MINUTE)
      val timePicker: TimePickerDialog

      timePicker = TimePickerDialog(context, onTimeSetListener, hour, minute, true)
      timePicker.setTitle(resources.getString(R.string.add_meal_pick_time))
      timePicker.show()
      timePicker.setOnDismissListener { view ->
      }

      binding.addMealTimeEditText.setTextColor(resources.getColor(R.color.colorSecondaryText))
    }

    binding.addMealDateEditText.setOnClickListener { view ->
      val mealDate = viewModel.createDateCalendar()
      val dialog = DatePickerDialog.newInstance(
        { dialogView, year, monthOfYear, dayOfMonth ->
          val calendar = viewModel.createCalendar(year, monthOfYear, dayOfMonth)
          mealTime = (calendar.timeInMillis / 1000L)

          doValidateDateField(binding.addMealDateEditText, mealTime)

          binding.addMealDateEditText.text = DateUtils.getRelativeTimeSpanString(mealTime * 1000,
            TimeZone.getDefault().getRawOffset().toLong(), DateUtils.DAY_IN_MILLIS)
        },
        mealDate.get(Calendar.YEAR),
        mealDate.get(Calendar.MONTH),
        mealDate.get(Calendar.DAY_OF_MONTH)
      )
      dialog.setOnDismissListener { dialogView ->
        viewModel.date = Timestamp(mealTime, 0)

        doValidateDateField(binding.addMealDateEditText, mealTime)
      }
      dialog.minDate = Calendar.getInstance()
      dialog.show(activity?.fragmentManager, MEAL_DATE)
      binding.addMealDateEditText.setTextColor(resources.getColor(R.color.colorSecondaryText))
    }
  }

  private fun doValidateDateField(view: TextView, timestamp: Long?) {
    if (viewModel.isMealDateValid(timestamp)) {
      view.error = null
    } else {
      view.error = resources.getString(R.string.add_meal_date_incorrect_value)
    }
  }
}
