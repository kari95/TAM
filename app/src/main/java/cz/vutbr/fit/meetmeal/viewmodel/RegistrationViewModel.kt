package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*
import java.util.regex.*

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

  val message: ObservableField<String> = ObservableField()

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
  val loading: ObservableBoolean = ObservableBoolean(false)

  private val userEngine = UserEngine()

  private val disposableComposite = CompositeDisposable()

  fun onRegister() {
    val nameOk = validateName()
    val emailOk = validateEmail()
    val genderOk = validateGender()
    val passwordOk = validatePassword()
    val passwordAgainOk = validatePasswordAgain()
    if (nameOk && emailOk && genderOk && passwordOk &&passwordAgainOk) {
      val name = name.get()
      val email = email.get()
      val password = password.get()
      val gender = when {
        isFemale.get() && !isMale.get() -> User.Gender.FEMALE
        isMale.get() && !isFemale.get() -> User.Gender.MALE
        else -> User.Gender.UNKNOWN
      }
      if (name != null && email != null && password != null) {

        val user = User(name, gender)
        userEngine.registerUser(email, password, user)
          .doOnSubscribe { loading.set(true) }
          .doOnTerminate { loading.set(false) }
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            registred.set(true)
          }, { err ->
            handleRegisterError(err)
          }).addTo(disposableComposite)
      }
    }
  }

  fun validateName(): Boolean {
    val name = name.get()
    nameError.set(when {
      name == null || name.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return nameError.get() == null
  }

  fun validateEmail(): Boolean {
    val email = email.get()
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    emailError.set(when {
      email == null || email.isEmpty() -> getString(R.string.required_field)
      !pattern.matcher(email).matches() -> getString(R.string.registration_email_format)
      else -> null
    })
    return emailError.get() == null
  }

  fun validateGender(): Boolean {
    val isFemale = isFemale.get()
    val isMale = isMale.get()
    message.set(when {
      !isFemale && !isMale -> getString(R.string.registration_gender_required)
      else -> null
    })
    return message.get() == null
  }

  fun validatePassword(): Boolean {
    val password = password.get()
    passwordError.set(when {
      password == null || password.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return passwordError.get() == null
  }

  fun validatePasswordAgain(): Boolean {
    val password = password.get()
    val passwordAgain = passwordAgain.get()
    passwordAgainError.set(when {
      passwordAgain == null || passwordAgain.isEmpty() -> getString(R.string.required_field)
      password != passwordAgain -> getString(R.string.registration_password_not_same)
      else -> null
    })
    return passwordError.get() == null
  }

  override fun onCleared() {
    disposableComposite.dispose()
    super.onCleared()
  }

  private fun handleRegisterError(err: Throwable) {
    registred.set(false)
    message.set(null)
    message.set(when (err) {
      is FirebaseAuthWeakPasswordException -> getString(R.string.registration_weak_password)
      else -> getString(R.string.unknown_error)
    })
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}
