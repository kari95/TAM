package cz.vutbr.fit.meetmeal.viewmodel

import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.schedulers.*

class GroupsViewModel: ViewModel() {

  val groups: MutableLiveData<ArrayList<Group>> = MutableLiveData()

  val isLoading: ObservableBoolean = ObservableBoolean(false)

  private val groupEngine = GroupEngine()

  init {
    requestGroups()
  }

  fun onGroupsClick() {
    requestGroups()
  }

  fun onGroupClick(group: Group) {
    // Tady to se provolá když se klikne na položku ze seznamu skupin
  }
  
  fun onSignInClick() {
  }

  fun onMealClick() {
  }

  fun onRefresh() {
    isLoading.set(true)
    requestGroups()
  }

  private fun setGroups(newGroups: List<Group>) {
    groups.value = ArrayList(newGroups)
  }

  private fun requestGroups(): Disposable {
    return getGroups()
      .doOnNext { isLoading.set(false) }
      .subscribe({
        setGroups(it)
      }, {
        Log.e("OldMainViewModel", "getMeals(): onError", it)
      })
  }

  private fun getGroups(): Observable<List<Group>> {
    return groupEngine.findAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }
}
