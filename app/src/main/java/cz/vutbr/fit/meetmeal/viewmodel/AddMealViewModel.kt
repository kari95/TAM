package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import androidx.databinding.*
import androidx.lifecycle.*
import com.google.firebase.*
import com.google.firebase.auth.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.engine.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.android.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.rxkotlin.*
import org.joda.time.*
import org.joda.time.format.*
import java.time.format.*
import java.util.regex.*

class AddMealViewModel(application: Application): AndroidViewModel(application) {

  val message: ObservableField<String> = ObservableField()

  val name: ObservableField<String> = ObservableField()
  val nameError: ObservableField<String> = ObservableField()
  val peopleCount: ObservableField<Int> = ObservableField()
  val peopleCountError: ObservableField<String> = ObservableField()
  val price: ObservableField<Int> = ObservableField()
  val priceError: ObservableField<String> = ObservableField()
  val female: ObservableBoolean = ObservableBoolean(false)
  val male: ObservableBoolean = ObservableBoolean(false)
  val booth: ObservableBoolean = ObservableBoolean(true)
  val address: ObservableField<String> = ObservableField()
  val addressError: ObservableField<String> = ObservableField()
  val group: ObservableField<Group> = ObservableField()
  val time: ObservableField<DateTime> = ObservableField()
  val timeSelected: ObservableBoolean = ObservableBoolean(false)
  val dateSelected: ObservableBoolean = ObservableBoolean(false)

  val added: ObservableBoolean = ObservableBoolean(false)
  val loading: ObservableBoolean = ObservableBoolean(false)

  val firebaseUser: ObservableField<FirebaseUser> = ObservableField()
  val groups: ObservableField<List<Group>> = ObservableField()

  val timeFormatter =  DateTimeFormat.forPattern("HH:mm")
  val dateFormatter =  DateTimeFormat.forPattern("dd. MM. yyyy")

  private val mealEngine = MealEngine()
  private val userEngine = UserEngine()
  private val groupEngine = GroupEngine()

  private val disposableComposite = CompositeDisposable()

  fun onScreenShowed() {
    firebaseUser.set(userEngine.getCurrentFirebaseUser())
    groupEngine.findAll()
      .doOnSubscribe { loading.set(true) }
      .doOnTerminate { loading.set(false) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        groups.set(it)
      }, {}).addTo(disposableComposite)
  }

  fun onGroupSelected(position: Int) {
    val list = groups.get()
    if (list != null && list.size > position) {
      group.set(list[position])
    }
  }

  fun onTimeSelected(hour: Int, minute: Int) {
    val old = time.get() ?: DateTime.now().plusHours(5)
    time.set(
      old.withHourOfDay(hour).withMinuteOfHour(minute)
    )
    timeSelected.set(true)
  }

  fun onDateSelected(year: Int, month: Int, day: Int) {
    val old = time.get() ?: DateTime.now().plusHours(5)
    time.set(
      old.withYear(year).withMonthOfYear(month).withDayOfMonth(day)
    )
    dateSelected.set(true)
  }

  fun onSaveClick() {
    val nameOk = validateName()
    val priceOk = validatePrice()
    val addressOk = validateAddress()
    val peopleCountOk = validateCount()
    val restOk = validateTime() && validateGender()

    if (nameOk && priceOk && addressOk && peopleCountOk && restOk) {
      val name = name.get() ?: ""
      val groupId = group.get()?.id ?: ""
      val time = Timestamp((time.get() ?: DateTime.now()).toDate())
      val address = address.get() ?: ""
      val peopleCount = peopleCount.get() ?: 0
      val price = price.get() ?: 0
      val gender = when {
        female.get() -> User.Gender.FEMALE
        male.get() -> User.Gender.MALE
        else -> User.Gender.BOTH
      }
      userEngine.getCurrentUser()
        .doOnSubscribe { loading.set(true) }
        .doOnTerminate { loading.set(false) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ user ->
          val meal = Meal(name, time, gender, user.id, groupId, peopleCount, price, address)
          mealEngine.add(meal)
            .doOnSubscribe { loading.set(true) }
            .doOnTerminate { loading.set(false) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              added.set(true)
            }, {
              handleAddingError(it)
            })
        }, {}).addTo(disposableComposite)
    }

    /*val meal = Meal(name, time, gender, createdBy, peopleCount, price, address)
    mealEngine.add(meal)*/
  }

  fun validateName(): Boolean {
    val name = name.get()
    nameError.set(when {
      name == null || name.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return nameError.get() == null
  }

  fun validatePrice(): Boolean {
    val price = price.get()
    priceError.set(when {
      price == null || price <= 0 -> getString(R.string.required_field)
      else -> null
    })
    return priceError.get() == null
  }

  fun validateAddress(): Boolean {
    val name = name.get()
    addressError.set(when {
      name == null || name.isEmpty() -> getString(R.string.required_field)
      else -> null
    })
    return addressError.get() == null
  }

  fun validateCount(): Boolean {
    val peopleCount = peopleCount.get()
    peopleCountError.set(when {
      peopleCount == null || peopleCount <= 0 -> getString(R.string.required_field)
      else -> null
    })
    return peopleCountError.get() == null
  }

  fun validateTime(): Boolean {
    val time = time.get()
    val timeSelected = timeSelected.get()
    val dateSelected = dateSelected.get()
    message.set(null)
    message.set(when {
      time == null || !timeSelected || !dateSelected -> getString(R.string.registration_time_required)
      time.isBeforeNow  -> getString(R.string.registration_time_before_now)
      else -> null
    })
    return message.get() == null
  }

  fun validateGender(): Boolean {
    val female = female.get()
    val male = male.get()
    val booth = booth.get()
    message.set(null)
    message.set(when {
      !female && !male && !booth -> getString(R.string.registration_gender_required)
      else -> null
    })
    return message.get() == null
  }

  private fun handleAddingError(err: Throwable) {
    added.set(false)
    message.set(null)
    message.set(when (err) {
      is FirebaseAuthWeakPasswordException -> getString(R.string.registration_weak_password)
      else -> getString(R.string.unknown_error)
    })
  }

  private fun getString(id: Int): String {
    val app = getApplication<Application>()
    return app.getString(id)
  }
}