package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.schedulers.*

class MealListViewModel(app: Application): AndroidViewModel(app) {

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)

  private val mealEngine = MealEngine()

  init {
    requestMeals()
  }

  fun onMealClick() {
  }

  fun onAddClick() {
    getTestingData().subscribe {
      for (item in it) {
        mealEngine.add(item)
      }
    }
  }

  fun onSignInClick() {
  }

  fun onGroupsClick() {
  }

  fun onRefresh() {
    isLoading.set(true)
    requestMeals()
  }
  /*
    private fun setMealType(type: Meal.MealType) {
      dayTimePickerVisible.set(type == Meal.MealType.PLANNED)
      mealType.set(type)
      getMeals()
        .subscribeOn(Schedulers.io())
        .map {
          it.filter {
            type == it.type
          }
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {setMeals(it)}
    }*/

  private fun setMeals(newMeals: List<Meal>) {
    meals.value = ArrayList(newMeals)
  }

  private fun requestMeals(): Disposable {
    return getMeals()
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
    val user = User(id = 1, name = "John", email = "john@mail.com", gender = User.Gender.MALE)
    val user4 = User(id = 4, name = "Jane", email = "jane@mail.com", gender = User.Gender.FEMALE)
    val address = Address("Božetěchova 12", "Brno", "123 00")
    val address2 = Address("Mojmírovo náměstí 120", "Brno", "123 00")
    val address3 = Address("Palackého vrch 1050", "Brno", "123 45")
    return Observable.just(arrayListOf(
      Meal(name = "name", user = user, address = address, price = 500, peopleCount = 4,
        gender = User.Gender.MALE),
      Meal(name = "name", user = user, address = address2, price = 350, peopleCount = 3),
      Meal(name = "name", user = user4, address = address3, price = 420, peopleCount = 2,
        gender = User.Gender.FEMALE)
    ))
  }
}