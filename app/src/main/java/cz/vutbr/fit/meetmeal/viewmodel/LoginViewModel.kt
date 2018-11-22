package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import io.reactivex.android.schedulers.*
import java.util.regex.*
import cz.vutbr.fit.meetmeal.R.id.email
import io.reactivex.disposables.*
import io.reactivex.internal.disposables.*
import io.reactivex.internal.util.NotificationLite.disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.rxkotlin.*

class LoginViewModel(application: Application): AndroidViewModel(application) {

  val message: ObservableField<String> = ObservableField()

  val loggedIn: ObservableBoolean = ObservableBoolean(false)
  val loading: ObservableBoolean = ObservableBoolean(false)

  val email: ObservableField<String> = ObservableField()
  val emailError: ObservableField<String> = ObservableField()

  val password: ObservableField<String> = ObservableField()
  val passwordError: ObservableField<String> = ObservableField()

  private val userEngine = UserEngine()

  private val disposableComposite = CompositeDisposable()

  fun onSignIn() {
    val emailOk = validateEmail()
    val passwordOk = validatePassword()
    if (emailOk && passwordOk) {
      userEngine.loginUser(email.get() ?: "", password.get() ?: "")
        .doOnSubscribe { loading.set(true) }
        .doOnTerminate { loading.set(false) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          loggedIn.set(true)
        }, { err ->
          handleLoginError(err)
        }).addTo(disposableComposite)
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

  override fun onCleared() {
    disposableComposite.dispose()
    super.onCleared()
  }

  private fun handleLoginError(err: Throwable) {
    loggedIn.set(false)
    message.set(null)
    message.set(when (err) {
      is FirebaseAuthInvalidUserException -> getString(R.string.login_invalid_account)
      else -> getString(R.string.unknown_error)
    })
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}
