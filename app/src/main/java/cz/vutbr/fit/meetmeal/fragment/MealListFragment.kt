package cz.vutbr.fit.meetmeal.fragment

import androidx.lifecycle.*
import androidx.databinding.*
import android.os.*
import androidx.core.app.*
import android.view.*
import androidx.fragment.app.*
import androidx.navigation.fragment.*
import androidx.recyclerview.widget.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.adapter.*
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MealListFragment: Fragment(), MenuItem.OnMenuItemClickListener {

  companion object {
    fun newInstance() = MealListFragment()
  }

  private val adapter = MealAdapter()

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
    // TODO: Use the ViewModel
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
  }
}
