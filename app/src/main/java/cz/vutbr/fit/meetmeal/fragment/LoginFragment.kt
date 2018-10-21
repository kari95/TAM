package cz.vutbr.fit.meetmeal.fragment

import android.arch.lifecycle.ViewModelProviders
import android.databinding.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.*

import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*

class LoginFragment: Fragment() {

  private lateinit var binding: FragmentLoginBinding
  private lateinit var viewModel: LoginViewModel

  companion object {
    fun newInstance() = LoginFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
    binding.registrationButton.setOnClickListener(
      Navigation.createNavigateOnClickListener(R.id.action_registration)
    )
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    binding.viewModel = viewModel
    // TODO: Use the ViewModel
  }
}
