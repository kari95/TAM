package cz.vutbr.fit.meetmeal.viewmodel

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.activity.AddMealActivity
import cz.vutbr.fit.meetmeal.engine.MealEngine
import cz.vutbr.fit.meetmeal.model.Address
import cz.vutbr.fit.meetmeal.model.Meal
import cz.vutbr.fit.meetmeal.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime

class MyMealsViewModel: ViewModel() {
  // TODO: Implement the ViewModel
  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()

  private val mealEngine = MealEngine()

  init {
    requestMeals()
  }

  fun onMealsClick() {
    requestMeals()
  }

  /*fun onAddClick() {
    val intent = Intent(this.getApplication(), AddMealActivity::class.java)

    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ContextCompat.startActivity(this.getApplication(), intent, null)
  }*/

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
            Meal(id = 1, name = "name", user = user, address = address, price = 500, peopleCount = 4,
                    gender = User.Gender.MALE, time = DateTime.now()),
            Meal(id = 2, name = "name",user = user, address = address2, price = 350, peopleCount = 3,
                    time = DateTime.now()),
            Meal(id = 3, name = "name",user = user, address = address3, price = 420, peopleCount = 2,
                    gender = User.Gender.FEMALE, time = DateTime.now())
    ))
  }
}
