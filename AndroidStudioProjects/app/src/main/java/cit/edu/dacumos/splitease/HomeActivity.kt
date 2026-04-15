package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale
import cit.edu.dacumos.splitease.R
import cit.edu.dacumos.splitease.GroupsActivity
import cit.edu.dacumos.splitease.HistoryActivity
import cit.edu.dacumos.splitease.ProfileActivity
import cit.edu.dacumos.splitease.NewBillActivity
import cit.edu.dacumos.splitease.BillRepository
import cit.edu.dacumos.splitease.BillAdapter

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupHeader()
        setupRecyclerView()
        setupBottomNavigation()
        setupAddBill()
        updateStats()
    }

    private fun setupHeader() {
        val prefs = getSharedPreferences("SplitEasePrefs", MODE_PRIVATE)
        val userName = prefs.getString("userName", "User") ?: "User"

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvAvatar = findViewById<TextView>(R.id.tvAvatar)
        
        tvUserName.text = userName
        tvAvatar.text = if (userName.isNotEmpty()) userName.take(1).uppercase() else "U"
    }

    private fun setupRecyclerView() {
        val rvBills = findViewById<RecyclerView>(R.id.rvBills)
        rvBills.layoutManager = LinearLayoutManager(this@HomeActivity)
        rvBills.adapter = BillAdapter(BillRepository.getBills())
    }

    private fun updateStats() {
        val totalOwed = BillRepository.getTotalOwed()
        findViewById<TextView>(R.id.tvOwedAmount).text = String.format(Locale.getDefault(), "₱%.2f", totalOwed)
        findViewById<TextView>(R.id.tvNetAmount).text = String.format(Locale.getDefault(), "₱%.2f", totalOwed)
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_groups -> {
                    startActivity(Intent(this@HomeActivity, GroupsActivity::class.java))
                    true
                }
                R.id.nav_activity -> {
                    startActivity(Intent(this@HomeActivity, HistoryActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAddBill() {
        findViewById<View>(R.id.fabAddBill).setOnClickListener {
            startActivity(Intent(this@HomeActivity, NewBillActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to Home
        updateStats()
        setupRecyclerView()
    }
}
