package cz.vutbr.fit.meetmeal.viewmodel

import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*
import io.reactivex.schedulers.*

class MealDetailViewModel: ViewModel() {

  val meal: ObservableField<Meal> = ObservableField(Meal())
  val user: ObservableField<User> = ObservableField(User())
  val gender: ObservableField<User.Gender> = ObservableField(User.Gender.UNKNOWN)
  val loading: ObservableBoolean = ObservableBoolean(true)

  private val mealEngine = MealEngine()

  private val disposableComposite = CompositeDisposable()

  fun getReadableDate(): String {
    return meal.get()!!.formatedTime
  }

  fun onMealIdChange(id: String) {
    requestMeal(id)
  }

  private fun requestMeal(id: String) {
    mealEngine.find(id)
      .doOnSubscribe { loading.set(true) }
      .doOnTerminate { loading.set(false) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        meal.set(it)
      }, {}).addTo(disposableComposite)
  }
}