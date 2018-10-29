package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.model.*

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

  val isFemale: ObservableBoolean = ObservableBoolean(false)
  val isMale: ObservableBoolean = ObservableBoolean(false)
  val name: ObservableField<String> = ObservableField()
  val nameError: ObservableField<String> = ObservableField()

  val email: ObservableField<String> = ObservableField()
  val emailError: ObservableField<String> = ObservableField()

  val password: ObservableField<String> = ObservableField()
  val passwordError: ObservableField<String> = ObservableField()

  val passwordAgain: ObservableField<String> = ObservableField()
  val passwordAgainError: ObservableField<String> = ObservableField()
  // TODO: Implement the ViewModel

  fun onRegister() {
    validateName()
    validateEmail()
    validatePassword()
    val name = name.get()
    val gender = when {
      isFemale.get() && !isMale.get() -> User.Gender.FEMALE
      isMale.get() && !isFemale.get() -> User.Gender.MALE
      else -> User.Gender.UNKNOWN
    }
    if (name != null) {

      val user = User(name, gender)
    }
  }

  private fun validateName() {
    val name = name.get()
    nameError.set(when {
      name == null || name.isEmpty() -> getString(R.string.registration_name_required)
      else -> null
    })
  }

  private fun validateEmail() {
    val email = email.get()
    emailError.set(when {
      email == null || email.isEmpty() -> getString(R.string.registration_email_required)
      else -> null
    })
  }

  private fun validatePassword() {
    val password = password.get()
    passwordError.set(when {
      password == null || password.isEmpty() -> getString(R.string.registration_password_required)
      else -> null
    })
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}
