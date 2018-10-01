package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.arch.lifecycle.*
import android.databinding.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.*
import io.reactivex.schedulers.*
import org.joda.time.*

class MainViewModel(app: Application): AndroidViewModel(app) {

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()
  val mealType: ObservableField<Meal.MealType> = ObservableField()

  init {
    setMealType(Meal.MealType.NOW)
    setMeals(getTestingData())
  }

  fun onMealTypeChanged(type: Meal.MealType) {
    setMealType(type)
  }

  fun onAddClick() {
  }

  fun onSignInClick() {
  }

  fun onGroupsClick() {
  }

  private fun setMealType(type: Meal.MealType) {
    dayTimePickerVisible.set(type == Meal.MealType.PLANNED)
    mealType.set(type)
    Observable.just(getTestingData())
      .observeOn(Schedulers.computation())
      .map {
        it.filter {
          type == it.type
        }
      }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {setMeals(it)}
  }

  private fun setMeals(newMeals: List<Meal>) {
    meals.value = ArrayList(newMeals)
  }

  private fun getTestingData(): ArrayList<Meal> {
    val user = User(id = 1, name = "John", email = "john@mail.com", gender = User.Gender.MALE)
    val user2 = User(id = 2, name = "Luke", email = "luke@mail.com", gender = User.Gender.MALE)
    val user3 = User(id = 3, name = "Bruce", email = "bruce@mail.com", gender = User.Gender.MALE)
    val user4 = User(id = 4, name = "Jane", email = "jane@mail.com", gender = User.Gender.FEMALE)
    val address = Address("Božetěchova 12", "Brno", "123 00")
    val address2 = Address("Mojmírovo náměstí 120", "Brno", "123 00")
    val address3 = Address("Palackého vrch 1050", "Brno", "123 45")
    return arrayListOf(
      NowMeal(time = DateTime.now().plusHours(1), user = user, place = "Zastávka"),
      NowMeal(time = DateTime.now().plusHours(2), user = user2, place = "Před kolejemi"),
      PlannedMeal(user = user, address = address, totalPrice = 500, peoplesCount = 4,
        gender = User.Gender.MALE),
      NowMeal(time = DateTime.now().plusHours(4), user = user3, place = "Hospoda"),
      NowMeal(time = DateTime.now().plusHours(6), user = user4, place = "Na nádvoří"),
      PlannedMeal(user = user, address = address2, totalPrice = 350, peoplesCount = 3),
      PlannedMeal(user = user4, address = address3, totalPrice = 420, peoplesCount = 2,
        gender = User.Gender.FEMALE)
    )
  }
}