package cz.vutbr.fit.meetmeal.viewmodel

import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.disposables.*

class MyMealDetailViewModel: ViewModel() {

  val meal: ObservableField<Meal> = ObservableField(Meal())
  val gender: ObservableField<User.Gender> = ObservableField(User.Gender.UNKNOWN)

  fun getReadableDate(): String {

    return meal.get()!!.formatedTime
  }
}
