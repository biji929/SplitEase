package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        findViewById<TextView>(R.id.tvCreateAccount).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}