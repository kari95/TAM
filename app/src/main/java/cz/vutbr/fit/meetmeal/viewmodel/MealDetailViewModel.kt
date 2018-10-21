package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.model.*

class MealDetailViewModel: ViewModel() {

  val meal: ObservableField<Meal> = ObservableField()
  val gender: ObservableField<User.Gender> = ObservableField()

  fun getReadableDate(): String {

    return "10.5.2021"
  }

}