package cz.vutbr.fit.meetmeal.viewmodel

import android.text.TextUtils.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*

class MealDetailViewModel: ViewModel() {

  val meal: ObservableField<Meal> = ObservableField(Meal())
  val user: ObservableField<User> = ObservableField(User())
  val gender: ObservableField<User.Gender> = ObservableField(User.Gender.UNKNOWN)
  val loading: ObservableBoolean = ObservableBoolean(true)

  val swipeLoading: ObservableBoolean = ObservableBoolean(false)

  private val mealEngine = MealEngine()
  private val userEngine = UserEngine()

  private val disposableComposite = CompositeDisposable()

  fun onMealIdChange(id: String) {
    requestMeal(id)
  }

  fun onRefresh() {
    swipeLoading.set(true)
    requestMeal(meal.get()?.id ?: "")
  }

  private fun requestMeal(id: String) {
    mealEngine.find(id)
      .doOnSubscribe { loading.set(isEmpty(meal.get()?.id)) }
      .doOnError {
        loading.set(false)
        swipeLoading.set(false)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        meal.set(it)
        requestUser(it.userId)
      }, {}).addTo(disposableComposite)
  }

  private fun requestUser(id: String) {
    userEngine.find(id)
      .doOnTerminate {
        loading.set(false)
        swipeLoading.set(false)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        user.set(it)
      }, {}).addTo(disposableComposite)
  }
}