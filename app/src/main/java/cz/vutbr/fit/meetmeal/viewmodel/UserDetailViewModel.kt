package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.model.User

class UserDetailViewModel: ViewModel() {

  val user: ObservableField<User> = ObservableField(User())

  // TODO: Implement the ViewModel

}
