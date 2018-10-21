package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.arch.lifecycle.*
import android.content.*
import android.databinding.*
import android.support.v4.content.ContextCompat.*
import cz.vutbr.fit.meetmeal.activity.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import org.joda.time.*

class MainViewModel(app: Application): AndroidViewModel(app) {

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val dayTimePickerVisible: ObservableField<Boolean> = ObservableField()

  init {
    getMeals()
      .subscribe({
        setMeals(it)
      })
  }

  fun onMealsClick() {
  }

  fun onAddClick() {
    val intent = Intent(this.getApplication(), AddMealActivity::class.java)

    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(this.getApplication(), intent, null)
  }

  fun onSignInClick() {
  }

  fun onGroupsClick() {
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

  private fun getMeals(): Observable<ArrayList<Meal>> {
    return getTestingData()
  }

  private fun getTestingData(): Observable<ArrayList<Meal>> {
    val user = User(id = 1, name = "John", email = "john@mail.com", gender = User.Gender.MALE)
    val user2 = User(id = 2, name = "Luke", email = "luke@mail.com", gender = User.Gender.MALE)
    val user3 = User(id = 3, name = "Bruce", email = "bruce@mail.com", gender = User.Gender.MALE)
    val user4 = User(id = 4, name = "Jane", email = "jane@mail.com", gender = User.Gender.FEMALE)
    val address = Address("Božetěchova 12", "Brno", "123 00")
    val address2 = Address("Mojmírovo náměstí 120", "Brno", "123 00")
    val address3 = Address("Palackého vrch 1050", "Brno", "123 45")
    return Observable.just(arrayListOf(
      Meal(id = 1, user = user, address = address, totalPrice = 500, peopleCount = 4,
        gender = User.Gender.MALE, time = DateTime.now()),
      Meal(id = 2, user = user, address = address2, totalPrice = 350, peopleCount = 3,
        time = DateTime.now()),
      Meal(id = 3, user = user4, address = address3, totalPrice = 420, peopleCount = 2,
        gender = User.Gender.FEMALE, time = DateTime.now())
    ))
  }
}