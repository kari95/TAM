package cz.vutbr.fit.meetmeal.activity

import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.databinding.*
import androidx.navigation.fragment.*
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.*
import com.google.android.material.tabs.*
import cz.vutbr.fit.meetmeal.R
import cz.vutbr.fit.meetmeal.databinding.*

class MainActivity: AppCompatActivity() {

  lateinit var binding: ActivityMainBinding
  lateinit var navHost: NavHostFragment


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    navHost = supportFragmentManager
      .findFragmentById(R.id.nav_host) as NavHostFragment

    val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)

    setSupportActionBar(toolbar)
    NavigationUI.setupActionBarWithNavController(this, navHost.navController)
    NavigationUI.setupWithNavController(bottomNavigation, navHost.navController)

    navHost.navController.addOnNavigatedListener { _, destination ->
      binding.daytimeTabs.visibility = when (destination.id) {
        R.id.nav_meal_list -> View.VISIBLE
        else -> View.GONE
      }
    }
  }

  /*
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
      return NavigationUI.onNavDestinationSelected(item, navHost.navController) || super.onOptionsItemSelected(item)
  }
  */

  override fun onSupportNavigateUp(): Boolean {
    return navHost.navController.navigateUp() || super.onSupportNavigateUp()
    //return NavigationUI.navigateUp(drawer, navHost.navController) || super.onSupportNavigateUp()
  }
}
