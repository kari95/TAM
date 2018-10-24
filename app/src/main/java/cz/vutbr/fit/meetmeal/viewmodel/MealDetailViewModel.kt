package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.model.*
import org.joda.time.*

class MealDetailViewModel: ViewModel() {

  val meal: ObservableField<Meal> = ObservableField(Meal())
  val gender: ObservableField<User.Gender> = ObservableField(User.Gender.UNKNOWN)

  fun getReadableDate(): String {
    return meal.get()!!.formatedTime
  }
}