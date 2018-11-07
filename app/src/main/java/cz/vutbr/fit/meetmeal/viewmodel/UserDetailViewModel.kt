package cz.vutbr.fit.meetmeal.viewmodel

import android.app.Application
import androidx.databinding.*
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.User
import io.reactivex.android.schedulers.*

class UserDetailViewModel(application: Application): AndroidViewModel(application) {

  val user: ObservableField<User> = ObservableField(User())
  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  private val userEngine = UserEngine()
  val currentPass: ObservableField<String> = ObservableField<String>()
  val newPass: ObservableField<String> = ObservableField<String>()
  val newAgainPass: ObservableField<String> = ObservableField<String>()

  fun onScreenShowed() {
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

  fun changePassword() {
    // TODO zkontrolovani stavajiciho hesla
    userEngine.checkCurrentPass(currentPass.get())
    //userEngine.checkPassword() reautorizace
    // TODO verifikovani noveho hesla - neni prazdne
    if (newPass.get() != newAgainPass.get()){
      // TODO chybova hlaska
    } else {
      userEngine.changePass(newPass.get().toString())
    }
  }



}
