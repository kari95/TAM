package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import com.google.android.material.tabs.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.activity.*
import cz.vutbr.fit.meetmeal.adapter.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MealListFragment: Fragment(), MenuItem.OnMenuItemClickListener {

  companion object {
    fun newInstance() = MealListFragment()
  }

  private val adapter = MealAdapter { meal ->
    NavHostFragment.findNavController(this).navigate(
      MealListFragmentDirections.actionMealDetail(meal).setMeal(meal))
  }

  private lateinit var binding: FragmentMealListBinding
  private lateinit var viewModel: MealListViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_list, container, false)
    setHasOptionsMenu(true)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MealListViewModel::class.java)
    binding.viewModel = viewModel

    setupView()
    setupListeners()
  }

  override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.actions_menu, menu)
    menu.getItem(0).setOnMenuItemClickListener(this)
  }

  override fun onMenuItemClick(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.action_groups -> {
        NavHostFragment.findNavController(this).navigate(R.id.action_group_add)
        return true
      }
    }
    return false
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

    binding.fab.setOnClickListener(
      { NavHostFragment.findNavController(this).navigate(R.id.action_add_meal) }
    )



    (activity as MainActivity).binding.daytimeTabs.addOnTabSelectedListener(
      object: TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) { }

        override fun onTabUnselected(tab: TabLayout.Tab?) { }

        override fun onTabSelected(tab: TabLayout.Tab?) {
          if (tab == null) {
            return
          }
          viewModel.onDaytimeChanged(when(tab.position) {
            1 -> MealListViewModel.DayTime.BREAKFAST
            2 -> MealListViewModel.DayTime.LUNCH
            3 -> MealListViewModel.DayTime.DINNER
            else -> null
          })
        }
      }
    )
  }
}
