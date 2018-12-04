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
import io.reactivex.rxkotlin.*
import io.reactivex.schedulers.*
import org.joda.time.*

class MealListViewModel(app: Application): AndroidViewModel(app) {

  enum class DayTime {
    BREAKFAST{
      override fun contains(date: DateTime) = date.hourOfDay().get() < 10
    },
    LUNCH{
      override fun contains(date: DateTime) = date.hourOfDay().get() in 10..15
    },
    DINNER{
      override fun contains(date: DateTime) = date.hourOfDay().get() > 15
    };

    abstract fun contains(date: DateTime): Boolean
  }

  val meals: MutableLiveData<ArrayList<Meal>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val daytime: ObservableField<DayTime> = ObservableField()

  private val cachedMeals = ArrayList<Meal>()

  private val mealEngine = MealEngine()

  private val disposableComposite = CompositeDisposable()

  init {
    refreshMeals(true)
  }

  fun onDaytimeChanged(dt: DayTime?) {
    daytime.set(dt)
    refreshMeals(false)
  }

  fun onRefresh() {
    isLoading.set(true)
    refreshMeals()
  }

  override fun onCleared() {
    disposableComposite.dispose()
    super.onCleared()
  }

  private fun setMeals(newMeals: List<Meal>) {
    meals.value = ArrayList(newMeals.filter {  meal ->
      daytime.get()?.contains(meal.dateTime) ?: true
    })
  }

  private fun refreshMeals(forceDownload: Boolean = true) {
    getMeals(forceDownload)
      .doOnNext { isLoading.set(false) }
      .subscribe({
        setMeals(it)
      }, {}).addTo(disposableComposite)
  }

  private fun getMeals(forceDownload: Boolean = false): Observable<List<Meal>> {
    if (forceDownload || cachedMeals.isEmpty()) {
      return mealEngine.findAll()
        .doOnNext {
          cachedMeals.clear()
          cachedMeals.addAll(it)
        }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }
    return Observable.just(cachedMeals)
  }
}