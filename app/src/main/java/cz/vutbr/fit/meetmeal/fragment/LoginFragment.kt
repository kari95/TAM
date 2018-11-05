package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.fragment.*
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

    viewModel.loggedIn.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        if (viewModel.loggedIn.get()) {
          NavHostFragment.findNavController(this@LoginFragment).navigateUp()
        }
      }
    })
  }
}
