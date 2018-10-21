package cz.vutbr.fit.meetmeal.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.viewmodel.*

class MealListFragment: Fragment() {

  companion object {
    fun newInstance() = MealListFragment()
  }

  private lateinit var viewModel: MealListViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.meal_list_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MealListViewModel::class.java)
    // TODO: Use the ViewModel
  }
}
