package cz.vutbr.fit.meetmeal.viewmodel

import android.arch.lifecycle.*
import android.databinding.*
import cz.vutbr.fit.meetmeal.model.*

class MealDetailViewModel: ViewModel() {

  val meal: ObservableField<Meal> = ObservableField()
  val gender: ObservableField<User.Gender> = ObservableField()

  fun getReadableDate(): String {

    return "10.5.2021"
  }

}