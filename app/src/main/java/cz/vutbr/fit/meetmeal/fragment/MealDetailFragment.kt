package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.model.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MealDetailFragment: Fragment() {

  private lateinit var binding: FragmentMealDetailBinding
  private lateinit var viewModel: MealDetailViewModel

  companion object {

    fun newInstance(): MealDetailFragment {
      return MealDetailFragment()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_detail, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val meal = MealDetailFragmentArgs.fromBundle(arguments).meal

    viewModel = ViewModelProviders.of(this).get(MealDetailViewModel::class.java)
    viewModel.meal.set(meal)
    binding.viewModel = viewModel

    setupView()
    setupListeners()
  }

  private fun setupView() {
    setTitle(viewModel.meal.get())
  }

  private fun setupListeners() {
    viewModel.meal.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        setTitle(viewModel.meal.get())
      }
    })
  }

  private fun setTitle(meal: Meal?) {
    (activity as AppCompatActivity).supportActionBar?.title = meal?.name
  }
}