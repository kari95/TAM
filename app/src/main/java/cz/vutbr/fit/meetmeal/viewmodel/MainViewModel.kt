package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.arch.lifecycle.*
import android.databinding.*
import cz.vutbr.fit.meetmeal.model.*
import org.joda.time.*

class MainViewModel(app: Application): AndroidViewModel(app) {

  enum class MealType(val value: Int) {
    NOW(0),
    PLANNED(1)
  }

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()
  val mealType: ObservableField<MealType> = ObservableField()

  init {
    setMealType(MealType.NOW)
    meals.value = getTestingData()
  }

  fun onMealTypeChanged(type: MealType) {
    setMealType(type)
  }

  fun onAddClick() {
    val newMeals = ArrayList(meals.value)
    newMeals.add(NowMeal())
    meals.value = newMeals
  }

  fun onSignInClick() {
  }

  fun onGroupsClick() {
  }

  private fun setMealType(type: MealType) {
    dayTimePickerVisible.set(type == MealType.PLANNED)
    mealType.set(type)
  }

  private fun getTestingData(): ArrayList<Meal> {
    val user = User(id = 1, name = "John", email = "john@mail.com", gender = User.Gender.MALE)
    val user2 = User(id = 2, name = "Luke", email = "luke@mail.com", gender = User.Gender.MALE)
    val user3 = User(id = 3, name = "Bruce", email = "bruce@mail.com", gender = User.Gender.MALE)
    val user4 = User(id = 4, name = "Jane", email = "jane@mail.com", gender = User.Gender.FEMALE)
    val address = Address("Božetěchova 12", "Brno", "123 00")
    val address2 = Address("Mojmírovo náměstí 120", "Brno", "123 00")
    val address3 = Address("Palackého vrch 1050", "Brno", "123 00")
    return arrayListOf(
      NowMeal(time = DateTime.now().plusHours(1), user = user, place = "Zastávka"),
      NowMeal(time = DateTime.now().plusHours(2), user = user2, place = "Před kolejemi"),
      PlannedMeal(user = user, address = address, totalPrice = 500, peoplesCount = 4, gender = User.Gender.MALE),
      NowMeal(time = DateTime.now().plusHours(4), user = user3, place = "Hospoda"),
      NowMeal(time = DateTime.now().plusHours(6), user = user4, place = "Na nádvoří"),
      PlannedMeal(user = user, address = address2, totalPrice = 350, peoplesCount = 3),
      PlannedMeal(user = user4, address = address3, totalPrice = 420, peoplesCount = 2, gender = User.Gender.FEMALE)
    )
  }
}