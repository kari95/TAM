package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.arch.lifecycle.*
import android.databinding.*

class MainViewModel(app: Application): AndroidViewModel(app) {

  enum class MealType {
    NOW,
    PLANNED
  }

  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()
  val mealType: ObservableField<MealType> = ObservableField()

  init {
    setMealType(MealType.NOW)
  }

  fun onMealTypeChanged(type: MealType) {
    setMealType(type)
  }

  fun onAddClick() {
    setMealType(MealType.NOW)
  }

  fun onSignInClick() {
    dayTimePickerVisible.set(!(dayTimePickerVisible.get() ?: false))
  }

  fun onGroupsClick() {
    setMealType(MealType.PLANNED)
  }

  private fun setMealType(type: MealType) {
    dayTimePickerVisible.set(type == MealType.PLANNED)
    mealType.set(type)
  }
}