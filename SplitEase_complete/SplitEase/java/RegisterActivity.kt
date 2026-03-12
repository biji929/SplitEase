package com.example.splitease

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splitease.databinding.ActivityRegisterBinding
import com.example.splitease.util.SessionManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnCreateAccount.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // Validation
            when {
                fullName.isEmpty() -> {
                    binding.etFullName.error = "Full name is required"
                    binding.etFullName.requestFocus()
                    return@setOnClickListener
                }
                email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = "Enter a valid email"
                    binding.etEmail.requestFocus()
                    return@setOnClickListener
                }
                phone.isEmpty() -> {
                    binding.etPhone.error = "Phone number is required"
                    binding.etPhone.requestFocus()
                    return@setOnClickListener
                }
                password.length < 4 -> {
                    binding.etPassword.error = "Password must be at least 4 characters"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }
                password != confirmPassword -> {
                    binding.etConfirmPassword.error = "Passwords do not match"
                    binding.etConfirmPassword.requestFocus()
                    return@setOnClickListener
                }
                !binding.cbTerms.isChecked -> {
                    Toast.makeText(this, "Please agree to the Terms of Service", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Save user and go to Home
            session.saveUser(fullName, email, phone)
            Toast.makeText(this, "Welcome to SplitEase, $fullName! 🎉", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
