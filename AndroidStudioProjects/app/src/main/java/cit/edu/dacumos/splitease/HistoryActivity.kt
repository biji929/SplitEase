package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupRecyclerView()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        val emptyState = findViewById<LinearLayout>(R.id.emptyState)
        
        val bills = BillRepository.getBills()
        
        if (bills.isEmpty()) {
            rvHistory.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            rvHistory.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
            
            rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvHistory.adapter = BillAdapter(bills)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_activity
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@HistoryActivity, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_groups -> {
                    startActivity(Intent(this@HistoryActivity, GroupsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_activity -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this@HistoryActivity, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }
}
