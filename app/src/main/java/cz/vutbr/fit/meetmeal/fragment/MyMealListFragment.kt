package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.adapter.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MyMealListFragment: Fragment() {

  private lateinit var binding: FragmentMyMealListBinding
  private lateinit var viewModel: MyMealListViewModel

  companion object {
    fun newInstance() = MyMealListFragment()
  }

  private val adapter = MealAdapter { meal ->
    NavHostFragment.findNavController(this).navigate(
      MyMealListFragmentDirections.actionMyMealDetail(meal).setMeal(meal))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_meal_list, container, false)
    binding.layoutNotLoggedIn.loginButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_sign_in)
    )
    binding.layoutNotLoggedIn.registrationButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_registration)
    )
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MyMealListViewModel::class.java)
    binding.viewModel = viewModel

    viewModel.onScreenShowed()

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
