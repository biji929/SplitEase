package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cit.edu.dacumos.splitease.R
import cit.edu.dacumos.splitease.LoginActivity
import cit.edu.dacumos.splitease.RegisterActivity
import cit.edu.dacumos.splitease.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check session first
        val prefs = getSharedPreferences("SplitEasePrefs", MODE_PRIVATE)
        if (prefs.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_splash)

        findViewById<android.view.View>(R.id.btnJoinNow).setOnClickListener {
            val intent = Intent(this@SplashActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
