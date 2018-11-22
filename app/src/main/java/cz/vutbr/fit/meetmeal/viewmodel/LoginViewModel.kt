package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import io.reactivex.android.schedulers.*
import java.util.regex.*
import cz.vutbr.fit.meetmeal.R.id.email



class LoginViewModel(application: Application): AndroidViewModel(application) {

  val loggedIn: ObservableBoolean = ObservableBoolean(false)

  val email: ObservableField<String> = ObservableField()
  val emailError: ObservableField<String> = ObservableField()

  val password: ObservableField<String> = ObservableField()
  val passwordError: ObservableField<String> = ObservableField()

  private val userEngine = UserEngine()

  fun onSignIn() {
    if (validateEmail() && validatePassword()) {
      userEngine.loginUser(email.get() ?: "", password.get() ?: "")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ success ->
          loggedIn.set(success)
        }, {})
    }
  }

  fun validateEmail(): Boolean {
    val email = email.get()
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    emailError.set(when {
      email == null || email.isEmpty() -> getString(R.string.registration_email_required)
      !pattern.matcher(email).matches() -> getString(R.string.registration_email_format)
      else -> null
    })
    return emailError.get() == null
  }

  fun validatePassword(): Boolean {
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
