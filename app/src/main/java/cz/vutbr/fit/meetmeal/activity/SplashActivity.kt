package cz.vutbr.fit.meetmeal.activity

import android.content.*
import android.os.*
import androidx.appcompat.app.*

class SplashActivity: AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState);

    startActivity(Intent(this, MainActivity::class.java))

    finish();
  }
}