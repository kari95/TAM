package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.android.schedulers.*

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

  val registred: ObservableBoolean = ObservableBoolean(false)

  private val userEngine = UserEngine()

  fun onRegister() {
    //validateName()
    //validateEmail()
    //validatePassword()
    val name = name.get()
    val email = email.get()
    val password = password.get()
    val gender = when {
      isFemale.get() && !isMale.get() -> User.Gender.FEMALE
      isMale.get() && !isFemale.get() -> User.Gender.MALE
      else -> User.Gender.UNKNOWN
    }
    if (name != null && email != null && password != null) {

      val user = User(name, arrayListOf(), gender)
      userEngine.registerUser(email, password, user)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ success ->
          registred.set(success)
        }, {

        })
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
