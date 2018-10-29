package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.model.User

class UserDetailViewModel: ViewModel() {

  val user: ObservableField<User> = ObservableField(User())
  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  // TODO: Implement the ViewModel

}
