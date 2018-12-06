package cz.vutbr.fit.meetmeal.viewmodel

import android.content.*
import android.util.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.android.gms.common.util.CollectionUtils.*
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.schedulers.*

class GroupViewModel: ViewModel() {

  val groups: MutableLiveData<ArrayList<Group>> = MutableLiveData()
  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val loading: ObservableBoolean = ObservableBoolean(true)

  var sharedPreferences: SharedPreferences? = null
    set(value) {
      field = value
      checkedGroups.set(sharedPreferences?.getStringSet(GROUP_TAG, mutableSetOf()))
    }

  var editor: SharedPreferences.Editor? = null
  var checkedGroups = ObservableField<MutableSet<String>>()

  private val groupEngine = GroupEngine()

  init {
    requestGroups()
  }

  companion object {
    const val GROUP_TAG = "group"
  }

  fun onGroupClick(group: Group) {
    val newGroups = checkedGroups.get()
    if (newGroups != null) {
      if (newGroups.contains(group.id)) {
        newGroups.remove(group.id)
      } else {
        newGroups.add(group.id)
      }
      checkedGroups.set(HashSet(newGroups))
    } else {
      checkedGroups.set(mutableSetOf(group.id))
    }
  }

  fun onSaveClick(value: MutableSet<String>?) {
    sharedPreferences?.edit()
      ?.putStringSet(GROUP_TAG, value)
      ?.apply()
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
      .doOnSubscribe { loading.set(isEmpty(groups.value)) }
      .doOnError { loading.set(false) }
      .doOnNext {
        isLoading.set(false)
      }
      .subscribe({
        setGroups(it)
      }, {
        Log.e("OldMainViewModel", "getMeals(): onError", it)
      })
  }

  private fun getGroups(): Observable<List<Group>> {
    return groupEngine.findAll()
      .doOnComplete{
        loading.set(false)
      }
      .doOnError {
        loading.set(false)
        isLoading.set(false)
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }
}
