package cz.vutbr.fit.meetmeal.fragment

import android.arch.lifecycle.*
import android.databinding.*
import android.os.*
import android.support.v4.app.*
import android.view.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class RegistrationFragment: Fragment() {

  private lateinit var binding: FragmentRegistrationBinding
  private lateinit var viewModel: RegistrationViewModel

  companion object {
    fun newInstance() = RegistrationFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(
      RegistrationViewModel::class.java)
    binding.viewModel = viewModel
    // TODO: Use the ViewModel
  }
}
