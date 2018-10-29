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

    //MealListFragmentDirections.actionAddMeal()
    /*getTestingData().subscribe {
      for (item in it) {
        mealEngine.add(item)
      }
    }*/
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
}