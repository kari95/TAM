package cz.vutbr.fit.meetmeal.viewmodel

import android.app.Application
import androidx.databinding.*
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.User
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*

class UserDetailViewModel(application: Application): AndroidViewModel(application) {

  val message: ObservableField<String> = ObservableField()

  val user: ObservableField<User> = ObservableField(User())
  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  val currentPass: ObservableField<String> = ObservableField()
  val currentPassError: ObservableField<String> = ObservableField()
  val newPass: ObservableField<String> = ObservableField()
  val newPassError: ObservableField<String> = ObservableField()
  val newAgainPass: ObservableField<String> = ObservableField()
  val newAgainPassError: ObservableField<String> = ObservableField()

  val loading: ObservableBoolean = ObservableBoolean(false)

  private val userEngine = UserEngine()
  private val disposableComposite = CompositeDisposable()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
    userEngine.getCurrentUser()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        user.set(it)
      }, {}).addTo(disposableComposite)
  }

  fun onLogOut() {
    userEngine.logoutUser()
    user.set(User())
    firebaseUser.set(null)
  }

  fun onChangePassword() {
    val passwordOk = validatePassword()
    val newPasswordOk = validateNewPassword()
    val newPasswordAgainOk = validateNewPasswordAgain()

    if (passwordOk && newPasswordOk && newPasswordAgainOk) {
      userEngine.checkCurrentPassword(currentPass.get() ?: "")
        .doOnSubscribe { loading.set(true) }
        .doOnError { loading.set(false) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          userEngine.changePassword(newPass.get() ?: "")
            .doOnTerminate { loading.set(false) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              handleChangePasswordSuccess()
            }, {
              handleChangePasswordError(it)
            }).addTo(disposableComposite)
        }, {
          handleChangePasswordError(it)
        }).addTo(disposableComposite)
    }
  }

  fun validatePassword(): Boolean {
    val password = currentPass.get()
    currentPassError.set(when {
      password == null || password.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return currentPassError.get() == null
  }

  fun validateNewPassword(): Boolean {
    val password = newPass.get()
    newPassError.set(when {
      password == null || password.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return newPassError.get() == null
  }

  fun validateNewPasswordAgain(): Boolean {
    val password = newPass.get()
    val passwordAgain = newAgainPass.get()
    newAgainPassError.set(when {
      passwordAgain == null || passwordAgain.isEmpty() -> getString(R.string.required_field)
      password != passwordAgain -> getString(R.string.registration_password_not_same)
      else -> null
    })
    return newAgainPassError.get() == null
  }


  override fun onCleared() {
    disposableComposite.dispose()
    super.onCleared()
  }

  private fun handleChangePasswordSuccess() {
    message.set(null)
    message.set(getString(R.string.password_changed))
    currentPass.set(null)
    newPass.set(null)
    newAgainPass.set(null)
  }

  private fun handleChangePasswordError(err: Throwable) {
    message.set(null)
    message.set(when (err) {
      is FirebaseAuthWeakPasswordException -> getString(R.string.registration_weak_password)
      is FirebaseAuthInvalidCredentialsException -> getString(R.string.login_invalid_account)
      else -> getString(R.string.unknown_error)
    })
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}
