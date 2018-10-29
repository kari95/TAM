package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.text.format.*
import android.view.*
import android.widget.*
import androidx.databinding.*
import androidx.fragment.app.*
import com.wdullaer.materialdatetimepicker.date.*
import cz.vutbr.fit.meetmeal.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*
import cz.vutbr.fit.meetmeal.viewmodel.*
import kotlinx.android.synthetic.main.fragment_add_meal.*
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

  private fun updateViews() {
    removeListeners()

    add_meal_date_edit_text.text = DateUtils.getRelativeTimeSpanString(mealTime * 1000,
      TimeZone.getDefault().getRawOffset().toLong(), DateUtils.DAY_IN_MILLIS)
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
      dialog.setOnDismissListener { dialogView ->
        doValidateDateField(add_meal_date_edit_text, mealTime)
      }
      dialog.minDate = Calendar.getInstance()
      dialog.show(activity?.fragmentManager, MEAL_DATE)
      add_meal_date_edit_text.setTextColor(resources.getColor(R.color.colorSecondaryText))
    }

    add_meal_save_button.setOnClickListener { _ ->
      viewModel.onSaveClick()
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
