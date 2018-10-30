package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import io.reactivex.android.schedulers.*

class LoginViewModel(application: Application): AndroidViewModel(application) {

  val loggedInt: ObservableBoolean = ObservableBoolean(false)

  val email: ObservableField<String> = ObservableField()
  val emailError: ObservableField<String> = ObservableField()

  val password: ObservableField<String> = ObservableField()
  val passwordError: ObservableField<String> = ObservableField()

  private val userEngine = UserEngine()

  fun onSignIn() {
    if (validateEmail() && validatePassword()) {
      userEngine.loginUser(email.get() ?: "", password.get() ?: "")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { success ->
          loggedInt.set(success)
        }
    }
  }

  private fun validateEmail(): Boolean {
    val email = email.get()
    emailError.set(when {
      email == null || email.isEmpty() -> getString(R.string.registration_email_required)
      else -> null
    })
    return emailError.get() == null
  }

  private fun validatePassword(): Boolean {
    val password = password.get()
    passwordError.set(when {
      password == null || password.isEmpty() -> getString(R.string.registration_password_required)
      else -> null
    })
    return passwordError.get() == null
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}
