package cz.vutbr.fit.meetmeal.viewmodel

import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*
import io.reactivex.schedulers.*

class MyMealListViewModel: ViewModel() {

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()

  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  private val mealEngine = MealEngine()
  private val userEngine = UserEngine()

  private val cachedMeals = ArrayList<Meal>()

  private val disposableComposite = CompositeDisposable()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
    refreshMeals(true)
  }

  fun onRefresh() {
    isLoading.set(true)
    refreshMeals()
  }

  private fun setMeals(newMeals: List<Meal>) {
    meals.value = ArrayList(newMeals.filter {  meal ->
      val userId = firebaseUser.get()?.uid
      return@filter meal.joinedUsers.contains(userId) || meal.userId == userId
    })
  }


  private fun refreshMeals(forceDownload: Boolean = true) {
    getMeals(forceDownload)
      .doOnNext { isLoading.set(false) }
      .subscribe({
        setMeals(it)
      }, {}).addTo(disposableComposite)
  }

  private fun getMeals(forceDownload: Boolean = false): Observable<List<Meal>> {
    if (forceDownload || cachedMeals.isEmpty()) {
      return mealEngine.findAll()
        .doOnNext {
          cachedMeals.clear()
          cachedMeals.addAll(it)
        }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }
    return Observable.just(cachedMeals)
  }
}
