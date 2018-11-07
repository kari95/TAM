package cz.vutbr.fit.meetmeal.viewmodel

import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import cz.vutbr.fit.meetmeal.R
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

  private val userEngine = UserEngine()

  init {
    requestGroups()
  }

  fun onGroupsClick() {
    requestGroups()
  }

  fun onGroupClick(group: Group) {

    val user: ObservableField<User> = ObservableField(User())

    userEngine.getCurrentUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              user.set(it)
              toggleCheckBox(it,group.name)

            }, {

              val err = it
            })
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

  private fun toggleCheckBox(user:User, group_name: String){

    if(user.groups.contains(group_name)){
      user.groups.remove(group_name)
      userEngine.deleteGroupToUser(user,group_name)
    } else {
      user.groups.add(group_name)
      userEngine.addGroupToUser(user,group_name)
    }
  }

  /*private fun check_groups_in_checkboxes(){
    // TODO projet seznam a vratit jiz oznacene skupiny
  }*/
}
