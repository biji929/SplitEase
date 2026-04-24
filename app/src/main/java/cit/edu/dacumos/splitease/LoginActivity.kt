package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cit.edu.dacumos.splitease.R
import cit.edu.dacumos.splitease.HomeActivity
import cit.edu.dacumos.splitease.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AUTO-LOGIN: if already logged in, skip to Home
        val prefs = getSharedPreferences("SplitEasePrefs", MODE_PRIVATE)
        if (prefs.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnTogglePassword = findViewById<ImageButton>(R.id.btnTogglePassword)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // Google Sign-In button (Modified to mock login)
        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            prefs.edit()
                .putBoolean("isLoggedIn", true)
                .putString("userEmail", "google@gmail.com")
                .putString("userName", "Google User")
                .apply()
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finishAffinity()
        }

        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty()) {
                etEmail.error = getString(R.string.error_enter_email)
                return@setOnClickListener
            }
            if (!email.endsWith("@gmail.com")) {
                etEmail.error = getString(R.string.error_invalid_email)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = "Please enter your password"
                return@setOnClickListener
            }
            if (password.length < 8) {
                etPassword.error = "Password must be at least 8 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Save login session to SharedPreferences
            prefs.edit()
                .putBoolean("isLoggedIn", true)
                .putString("userEmail", email)
                .putString("userName", email.substringBefore("@"))
                .apply()

            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finishAffinity()
        }

        findViewById<TextView>(R.id.tvCreateAccount).setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        var isPasswordVisible = false
        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(R.drawable.ic_eye)
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }
}