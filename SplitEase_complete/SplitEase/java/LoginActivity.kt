package com.example.splitease

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splitease.databinding.ActivityLoginBinding
import com.example.splitease.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        binding.btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(R.drawable.ic_eye)
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(R.drawable.ic_eye)
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

        binding.btnSignIn.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check saved credentials from registration
            val savedName = session.getUserName()
            val savedEmail = session.getUserEmail()

            // Demo login: match by name or use default admin credentials
            if ((username == savedName || username == savedEmail || username == "admin") && password == "1234") {
                val displayName = if (username == "admin") "Admin" else savedName
                session.saveUser(displayName, savedEmail)
                Toast.makeText(this, "Welcome back, $displayName! 👋", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials. Try admin / 1234", Toast.LENGTH_SHORT).show()
                binding.etUsername.background = getDrawable(R.drawable.input_border_active)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset link sent! (demo)", Toast.LENGTH_SHORT).show()
        }

        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google Sign-In coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
