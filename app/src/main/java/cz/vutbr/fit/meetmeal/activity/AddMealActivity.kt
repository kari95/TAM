package cz.vutbr.fit.meetmeal.activity

import android.arch.lifecycle.*
import android.databinding.*
import android.os.*
import android.support.v7.app.*
import cz.vutbr.fit.meetmeal.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.viewmodel.*
import cz.vutbr.fit.meetmeal.databinding.*

class AddMealActivity : AppCompatActivity() {

  lateinit var binding: AddMealBinding

  lateinit var viewModel: AddMealViewModel


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.add_meal)
    viewModel = ViewModelProviders.of(this).get(AddMealViewModel::class.java)
  }

}