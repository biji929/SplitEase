package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import cit.edu.dacumos.splitease.HomeActivity
import cit.edu.dacumos.splitease.HistoryActivity
import cit.edu.dacumos.splitease.ProfileActivity

class GroupsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        setupBottomNavigation()

        findViewById<Button>(R.id.btnCreateGroup).setOnClickListener {
            // Future implementation for group creation
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_groups
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@GroupsActivity, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_groups -> true
                R.id.nav_activity -> {
                    startActivity(Intent(this@GroupsActivity, HistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this@GroupsActivity, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
