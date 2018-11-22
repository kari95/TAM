package cz.vutbr.fit.meetmeal.fragment

import android.app.*
import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*
import java.util.*

class AddMealFragment: Fragment() {

  private val MEAL_DATE = "meal_date"

  lateinit var binding: FragmentAddMealBinding

  lateinit var viewModel: AddMealViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_meal, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(AddMealViewModel::class.java)
    binding.viewModel = viewModel

    viewModel.onScreenShowed()

    addListeners()
  }

  private fun addListeners() {

    binding.layoutNotLoggedIn.loginButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_sign_in)
    )
    binding.layoutNotLoggedIn.registrationButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_registration)
    )

    binding.addMealTimeEditText.setOnClickListener {
      showTimeDialog()
    }

    binding.addMealDateEditText.setOnClickListener {
      showDateDialog()
    }
  }

  private fun showTimeDialog() {
    val onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      viewModel.onTimeSelected(hour, minute)
    }

    val currentTime = Calendar.getInstance()
    val selectedTime = viewModel.time.get()

    val hour = selectedTime?.hourOfDay()?.get() ?: currentTime.get(Calendar.HOUR_OF_DAY)
    val minute = selectedTime?.minuteOfHour()?.get() ?: currentTime.get(Calendar.MINUTE)

    val timePicker = TimePickerDialog(context, onTimeSetListener, hour, minute, true)
    timePicker.setTitle(resources.getString(R.string.add_meal_pick_time))
    timePicker.show()

    binding.addMealTimeEditText.setTextColor(resources.getColor(R.color.colorSecondaryText))
  }

  private fun showDateDialog() {

    val currentDate = Calendar.getInstance()
    val selectedDate = viewModel.time.get()

    val year = selectedDate?.year()?.get() ?: currentDate.get(Calendar.YEAR)
    val month = selectedDate?.monthOfYear()?.get() ?: currentDate.get(Calendar.MONTH)
    val day = selectedDate?.dayOfMonth()?.get() ?: currentDate.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog.newInstance(
      { _, year, monthOfYear, dayOfMonth ->
        viewModel.onDateSelected(year, monthOfYear, dayOfMonth)
      },
      year,
      month,
      day
    )
    /*dialog.setOnDismissListener { dialogView ->
      viewModel.time = Timestamp(mealTime, 0)

      doValidateDateField(binding.addMealDateEditText, mealTime)
    }*/
    dialog.minDate = Calendar.getInstance()
    dialog.show(activity?.fragmentManager, MEAL_DATE)
    binding.addMealDateEditText.setTextColor(resources.getColor(R.color.colorSecondaryText))
  }
}
