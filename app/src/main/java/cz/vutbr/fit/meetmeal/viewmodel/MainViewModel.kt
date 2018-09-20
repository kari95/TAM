package cz.vutbr.fit.meetmeal.viewmodel

import android.app.*
import android.arch.lifecycle.*
import android.databinding.*

class MainViewModel(app : Application) : AndroidViewModel(app) {

  val testText: ObservableField<String> = ObservableField()

  fun test() {
    testText.set("It works!")
  }
}