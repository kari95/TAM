package cz.vutbr.fit.meetmeal.fragment

import android.os.*
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.google.android.material.snackbar.*
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

    viewModel.registred.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        if (viewModel.registred.get()) {
          NavHostFragment.findNavController(this@RegistrationFragment).navigateUp()
        }
      }
    })

    setupListeners()
  }

  private fun setupListeners() {

    viewModel.message.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
      override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        val message = viewModel.message.get()
        if (message != null) {
          Snackbar.make(binding.registrationButton, message, Snackbar.LENGTH_SHORT)
            .show()
        }
      }
    })

    binding.registrationName.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validateName()
      }
    }

    binding.registrationEmail.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validateEmail()
      }
    }

    binding.registrationPassword.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validatePassword()
      }
    }

    binding.registrationPasswordAgain.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        viewModel.validatePasswordAgain()
      }
    }
  }
}
