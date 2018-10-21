package cz.vutbr.fit.meetmeal.fragment

import android.arch.lifecycle.*
import android.databinding.*
import android.os.*
import android.support.v4.app.*
import android.view.*
import cz.vutbr.fit.meetmeal.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MealDetailFragment: Fragment() {

  private lateinit var binding: FragmentMealDetailBinding
  private lateinit var viewModel: MealDetailFragmentViewModel

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
    viewModel = ViewModelProviders.of(this).get(MealDetailFragmentViewModel::class.java)
    binding.viewModel = viewModel
    // TODO: Use the ViewModel

  }

}