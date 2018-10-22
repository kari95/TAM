package cz.vutbr.fit.meetmeal.viewmodel

import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.schedulers.*

class MyMealsViewModel: ViewModel() {
  // TODO: Implement the ViewModel
  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()

  private val mealEngine = MealEngine()

  init {
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
    val user = User(id = 1, name = "Jakub", email = "john@mail.com", gender = User.Gender.MALE)

    val address = Address("Jánská 12", "Brno", "123 00")
    val address2 = Address("Mendlovo náměstí 120", "Brno", "123 00")
    val address3 = Address("Purykyňovy koleje 1050", "Brno", "123 45")
    return Observable.just(arrayListOf(
            Meal(name = "name", user = user, address = address, price = 500, peopleCount = 4,
                    gender = User.Gender.MALE),
            Meal(name = "name",user = user, address = address2, price = 350, peopleCount = 3),
            Meal(name = "name",user = user, address = address3, price = 420, peopleCount = 2,
                    gender = User.Gender.FEMALE)
    ))
  }
}
