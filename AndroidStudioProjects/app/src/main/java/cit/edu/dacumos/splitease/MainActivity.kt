package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cit.edu.dacumos.splitease.HomeActivity
import cit.edu.dacumos.splitease.SplashActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("SplitEasePrefs", MODE_PRIVATE)
        val intent = if (prefs.getBoolean("isLoggedIn", false)) {
            Intent(this@MainActivity, HomeActivity::class.java)
        } else {
            Intent(this@MainActivity, SplashActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
