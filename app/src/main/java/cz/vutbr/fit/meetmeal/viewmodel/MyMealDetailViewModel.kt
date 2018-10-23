package cz.vutbr.fit.meetmeal.viewmodel

import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.disposables.*

class MyMealDetailViewModel: ViewModel() {

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)

  init {
    requestMyMeals()
  }

  fun onSignInClick() {
  }

  fun onGroupsClick() {
  }

  fun onRefresh() {
    isLoading.set(true)
    requestMyMeals()
  }

  private fun setMeals(newMeals: List<Meal>) {
    meals.value = ArrayList(newMeals)
  }

  private fun requestMyMeals(): Disposable {
    return getTestingData()
      .doOnNext { isLoading.set(false) }
      .subscribe({
        setMeals(it)
      }, {
        Log.e("OldMainViewModel", "getMeals(): onError", it)
      })
  }

  //TODO remove when data load finished
  private fun getTestingData(): Observable<ArrayList<Meal>> {
    val user = User(id = 1, name = "Jakub", email = "john@mail.com", gender = User.Gender.MALE)

    val address = Address("Jánská 12", "Brno", "123 00")
    val address2 = Address("Mendlovo náměstí 120", "Brno", "123 00")
    val address3 = Address("Purykyňovy koleje 1050", "Brno", "123 45")
    return Observable.just(arrayListOf(
      Meal(name = "name", user = user, address = address, price = 500, peopleCount = 4,
        gender = User.Gender.MALE),
      Meal(name = "name", user = user, address = address2, price = 350, peopleCount = 3),
      Meal(name = "name", user = user, address = address3, price = 420, peopleCount = 2,
        gender = User.Gender.FEMALE)
    ))
  }
}
