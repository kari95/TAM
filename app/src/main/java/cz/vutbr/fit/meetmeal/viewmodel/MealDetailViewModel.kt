package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.text.TextUtils.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*

class MealDetailViewModel(application: Application): AndroidViewModel(application) {

  val message: ObservableField<String> = ObservableField()

  val meal: ObservableField<Meal> = ObservableField(Meal())
  val createdBy: ObservableField<User> = ObservableField(User())
  val gender: ObservableField<User.Gender> = ObservableField(User.Gender.UNKNOWN)
  val loading: ObservableBoolean = ObservableBoolean(true)

  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  val swipeLoading: ObservableBoolean = ObservableBoolean(false)

  private val mealEngine = MealEngine()
  private val userEngine = UserEngine()

  private val disposableComposite = CompositeDisposable()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
  }

  fun onMealIdChange(id: String) {

    requestMeal(id)
  }

  fun onRefresh() {
    swipeLoading.set(true)
    requestMeal(meal.get()?.id ?: "")
  }

  fun onJoinClick() {
    mealEngine.find(meal.get()?.id ?: "")
      .doOnSubscribe { loading.set(true) }
      .doOnError { loading.set(false) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        meal.set(it)
        if (it.freeSpaces <= 0) {
          showMessage(R.string.meal_unable_join)
        } else {
          joinCurrentUser(it)
        }
      }, {
        showMessage(R.string.unknown_error)
      }).addTo(disposableComposite)
  }

  private fun joinCurrentUser(meal: Meal) {
    userEngine.getCurrentUser()
      .doOnSubscribe { loading.set(true) }
      .doOnError { loading.set(false) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        mealEngine.join(it, meal)
          .doOnSubscribe { loading.set(true) }
          .doOnTerminate { loading.set(false) }
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            showMessage(R.string.meal_successfull_join)
            swipeLoading.set(true)
            requestMeal(meal.id)
          }, {
            showMessage(R.string.unknown_error)
          }).addTo(disposableComposite)
      }, {
        showMessage(R.string.unknown_error)
      }).addTo(disposableComposite)

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
        createdBy.set(it)
      }, {}).addTo(disposableComposite)
  }

  private fun showMessage(messageId: Int) {
    message.set(null)
    message.set(getString(messageId))
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}