package cz.vutbr.fit.meetmeal.fragment

import android.app.*
import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.*
import com.google.android.material.snackbar.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*
import org.joda.time.*
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

    viewModel.message.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        val message = viewModel.message.get()
        if (message != null) {
          Snackbar.make(binding.addMealSaveButton, message, Snackbar.LENGTH_SHORT)
            .show()
        }
      }
    })

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

    binding.addMealNameEditText.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validateName()
      }
    }

    binding.addMealCostEditText.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validatePrice()
      }
    }

    binding.addMealAddressEditText.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validateAddress()
      }
    }

    binding.addMealPeopleInvitedEditText.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validateCount()
      }
    }
  }

  private fun showTimeDialog() {
    val onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      viewModel.onTimeSelected(hour, minute)
    }

    val selectedTime = viewModel.time.get() ?: DateTime.now().plusHours(5)

    val hour = selectedTime.hourOfDay().get()
    val minute = selectedTime.minuteOfHour().get()

    val timePicker = TimePickerDialog(context, onTimeSetListener, hour, minute, true)
    timePicker.show()
  }

  private fun showDateDialog() {
    val selectedDate = viewModel.time.get() ?: DateTime.now().plusHours(5)

    val year = selectedDate.year().get()
    val month = selectedDate.monthOfYear().get()
    val day = selectedDate.dayOfMonth().get()

    val dialog = DatePickerDialog.newInstance(
      { _, year, monthOfYear, dayOfMonth ->
        viewModel.onDateSelected(year, monthOfYear + 1, dayOfMonth)
      },
      year,
      month - 1,
      day
    )
    dialog.minDate = Calendar.getInstance()
    dialog.show(activity?.fragmentManager, MEAL_DATE)
  }
}
