package cz.vutbr.fit.meetmeal.activity

import android.arch.lifecycle.*
import android.databinding.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*
import cz.vutbr.fit.meetmeal.viewmodel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ml.kari.justdoit.adapter.*

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

  lateinit var binding: ActivityMainBinding
  lateinit var viewModel: MainViewModel

  private val adapter = MealAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

    binding.viewModel = viewModel
    setSupportActionBar(toolbar)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }



    setupView()
    setupListeners()
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.activity_main_actions, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    when (item.itemId) {
      R.id.action_groups -> {
        viewModel.onGroupsClick()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.nav_now -> {
        viewModel.onMealTypeChanged(MainViewModel.MealType.NOW)
      }
      R.id.nav_planned -> {
        viewModel.onMealTypeChanged(MainViewModel.MealType.PLANNED)
      }
      R.id.nav_sign_in -> {
        viewModel.onSignInClick()
      }
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }

  private fun setupView() {
    val layoutManager = LinearLayoutManager(this)
    val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)

    meal_list.adapter = adapter
    meal_list.layoutManager = layoutManager
    meal_list.addItemDecoration(dividerItemDecoration)

    viewModel.mealType.get()?.let { setMealType(it) }
  }

  private fun setupListeners() {
    val toggle = ActionBarDrawerToggle(
      this, drawer_layout, toolbar, R.string.navigation_drawer_open,
      R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)

    viewModel.mealType.addOnPropertyChangedCallback(mealTypeChangedListener)


    viewModel.meals.observe(this, Observer { meals ->
      if (meals == null)
        return@Observer
      adapter.meals = meals
    })
  }

  private fun setMealType(type: MainViewModel.MealType) {
    nav_view.setCheckedItem(when (type) {
      MainViewModel.MealType.NOW -> R.id.nav_now
      MainViewModel.MealType.PLANNED -> R.id.nav_planned
    })
  }

  private val mealTypeChangedListener = object: Observable.OnPropertyChangedCallback() {
    override fun onPropertyChanged(sender: Observable, propertyId: Int) {
      if (sender is ObservableField<*>) {
        val type = sender.get() as MainViewModel.MealType
        setMealType(type)
      }
    }
  }
}
