package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cit.edu.dacumos.splitease.R
import cit.edu.dacumos.splitease.HomeActivity
import cit.edu.dacumos.splitease.LoginActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvSignIn = findViewById<TextView>(R.id.tvSignIn)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        // Back arrow
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Google Mock Sign-In
        btnGoogle.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
            finishAffinity()
        }

        // Sign In link at bottom
        tvSignIn.setOnClickListener {
            finish()
        }

        // Create Account button
        btnCreateAccount.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (email.isEmpty()) {
                findViewById<EditText>(R.id.etEmail).error = getString(R.string.error_enter_email)
                return@setOnClickListener
            }
            if (!email.endsWith("@gmail.com")) {
                findViewById<EditText>(R.id.etEmail).error = getString(R.string.error_invalid_email)
                return@setOnClickListener
            }

            // Password matching filter
            if (password.isEmpty()) {
                etPassword.error = "Please enter a password"
                return@setOnClickListener
            }
            if (password.length < 8) {
                etPassword.error = "Password must be at least 8 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                Toast.makeText(this@RegisterActivity, "Please agree to the Terms of Service", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Success Mockup - Saving to SharedPreferences
            val prefs = getSharedPreferences("SplitEasePrefs", MODE_PRIVATE)
            prefs.edit().apply {
                // Just save the credentials, don't log in yet
                putString("registeredEmail", email)
                putString("registeredPassword", password)
                putString("userName", email.substringBefore("@"))
                apply()
            }

            Toast.makeText(this@RegisterActivity, "Account created! Please sign in.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            // Clear top to avoid backstack issues
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Toggle password visibility
        var isPasswordVisible = false
        val btnTogglePassword = findViewById<ImageButton>(R.id.btnTogglePassword)
        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnTogglePassword.setImageResource(R.drawable.ic_eye)
            }
            // Keep cursor at end
            etPassword.setSelection(etPassword.text.length)
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }
    }
}
