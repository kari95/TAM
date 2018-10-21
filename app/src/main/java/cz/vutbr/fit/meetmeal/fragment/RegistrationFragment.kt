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
