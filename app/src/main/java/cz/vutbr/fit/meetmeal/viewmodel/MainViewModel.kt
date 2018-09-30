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
    meals.value = ArrayList()
  }

  fun onMealTypeChanged(type: MealType) {
    setMealType(type)
  }

  fun onAddClick() {
    val newMeals = ArrayList(meals.value)
    newMeals.add(NowMeal("test", DateTime.now()))
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
}