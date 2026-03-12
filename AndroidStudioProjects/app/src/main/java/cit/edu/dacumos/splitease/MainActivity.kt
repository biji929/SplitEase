package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn  = findViewById<Button>(R.id.btnSignIn)
        val tvForgot   = findViewById<TextView>(R.id.tvForgotPassword)
        val tvRegister = findViewById<TextView>(R.id.tvCreateAccount)

        btnSignIn.setOnClickListener {
            val name = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            when {
                name.isEmpty() -> {
                    etUsername.error = "Enter your name"
                    etUsername.requestFocus()
                }
                pass.isEmpty() -> {
                    etPassword.error = "Enter your password"
                    etPassword.requestFocus()
                }
                pass.length < 4 -> {
                    etPassword.error = "Password too short"
                    etPassword.requestFocus()
                }
                name == "admin" && pass == "1234" -> {
                    Toast.makeText(this, "Welcome back! 👋", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                else -> {
                    Toast.makeText(this, "Invalid name or password", Toast.LENGTH_SHORT).show()
                    etPassword.setText("")
                    etPassword.requestFocus()
                }
            }
        }

        tvForgot.setOnClickListener {
            Toast.makeText(this, "Password reset sent!", Toast.LENGTH_SHORT).show()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
