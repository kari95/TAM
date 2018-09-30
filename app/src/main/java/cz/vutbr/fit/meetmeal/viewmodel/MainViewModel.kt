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
    val user = User(name = "Name", email = "neco@mail.com")
    val address = Address("Ulice 12", "Brno", "123 00")
    return arrayListOf(
      NowMeal(time = DateTime.now().plusHours(5), user = user),
      NowMeal(user = user, place = "place"),
      NowMeal(user = user, place = "place"),
      NowMeal(user = user, place = "place"),
      PlannedMeal(user = user, address = address),
      PlannedMeal(user = user, address = address),
      PlannedMeal(user = user, address = address),
      NowMeal(user = user),
      NowMeal(),
      PlannedMeal(address = address),
      PlannedMeal(),
      PlannedMeal(),
      NowMeal()
    )
  }
}