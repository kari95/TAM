package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.User
import io.reactivex.android.schedulers.*

class UserDetailViewModel: ViewModel() {

  val user: ObservableField<User> = ObservableField(User())
  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  private val userEngine = UserEngine()

  init {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
    userEngine.getCurrentUser()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        user.set(it)
      }, {})
  }

  fun onLogOut() {
    userEngine.logoutUser()
    firebaseUser.set(null)
  }

}
