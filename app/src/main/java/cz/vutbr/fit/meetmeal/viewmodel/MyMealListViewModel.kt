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
import io.reactivex.schedulers.*

class MyMealListViewModel: ViewModel() {

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()

  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()

  private val mealEngine = MealEngine()

  private val userEngine = UserEngine()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())

    requestMeals()
  }

  fun onMealClick() {
    requestMeals()
  }

  fun onSignInClick() {
  }

  fun onGroupsClick() {
  }

  fun onRefresh() {
    isLoading.set(true)
    requestMeals()
  }

  private fun setMeals(newMeals: List<Meal>) {
    meals.value = ArrayList(newMeals)
  }

  private fun requestMeals(): Disposable {
    return getTestingData()
      .doOnNext { isLoading.set(false) }
      .subscribe({
        setMeals(it)
      }, {
        Log.e("OldMainViewModel", "getMeals(): onError", it)
      })
  }

  private fun getMeals(): Observable<List<Meal>> {
    return mealEngine.findAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  private fun getTestingData(): Observable<ArrayList<Meal>> {
    val user = User(name = "Jakub", gender = User.Gender.MALE)

    return Observable.just(arrayListOf(
      Meal(name = "name", user = user, address = "", price = 500, peopleCount = 4,
        gender = User.Gender.MALE),
      Meal(name = "name", user = user, address = "", price = 350, peopleCount = 3),
      Meal(name = "name", user = user, address = "", price = 420, peopleCount = 2,
        gender = User.Gender.FEMALE)
    ))
  }
}
