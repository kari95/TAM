package cz.vutbr.fit.meetmeal.fragment

import androidx.lifecycle.*
import androidx.databinding.*
import android.os.*
import androidx.core.app.*
import android.view.*
import androidx.fragment.app.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class MyMealDetailFragment: Fragment() {

  private lateinit var binding: FragmentMyMealDetailBinding
  private lateinit var viewModel: MyMealDetailViewModel

  companion object {
    fun newInstance() = MyMealDetailFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_meal_detail, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(
      MyMealDetailViewModel::class.java)
    binding.viewModel = viewModel
    // TODO: Use the ViewModel
  }
}
