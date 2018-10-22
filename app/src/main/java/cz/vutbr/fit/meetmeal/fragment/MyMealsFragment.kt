package cz.vutbr.fit.meetmeal.fragment

import androidx.lifecycle.*
import androidx.databinding.*
import android.os.*
import androidx.core.app.*
import android.view.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.adapter.MealAdapter
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MyMealsFragment: Fragment() {

  private lateinit var binding: FragmentMyMealsBinding
  private lateinit var viewModel: MyMealsViewModel

  companion object {
    fun newInstance() = MyMealsFragment()
  }

  private val adapter = MealAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_meals, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MyMealsViewModel::class.java)
    binding.viewModel = viewModel
    // TODO: Use the ViewModel
    setupView()
    setupListeners()
  }

  private fun setupView() {
    val layoutManager = LinearLayoutManager(activity)
    val dividerItemDecoration = DividerItemDecoration(activity,
            layoutManager.orientation)

    binding.mealList.adapter = adapter
    binding.mealList.layoutManager = layoutManager
    binding.mealList.addItemDecoration(dividerItemDecoration)
  }

  private fun setupListeners() {

    viewModel.meals.observe(this, Observer { meals ->
      if (meals == null)
        return@Observer
      adapter.meals = meals
    })
  }
}
