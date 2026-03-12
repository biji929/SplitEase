package com.example.splitease

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.splitease.databinding.ActivitySplashBinding
import com.example.splitease.util.SessionManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // If already logged in, go directly to Home
        if (session.isLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding.btnJoinNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
