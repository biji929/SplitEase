package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupUI()
        updateStats()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        updateStats()
        updateRecyclerView()
    }

    private fun setupUI() {
        findViewById<LinearLayout>(R.id.fabAddBill).setOnClickListener {
            startActivity(Intent(this, NewBillActivity::class.java))
        }

        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_groups -> {
                    startActivity(Intent(this, GroupsActivity::class.java))
                    true
                }
                R.id.nav_activity -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateStats() {
        val bills = BillRepository.getBills()
        val totalOwed = bills.sumOf { bill ->
            val perPerson = bill.amount / bill.participants.size
            if (bill.participants.contains("Me")) {
                perPerson * (bill.participants.size - 1)
            } else {
                0.0
            }
        }

        findViewById<TextView>(R.id.tvOwedAmount).text = String.format(Locale.getDefault(), "₱%.0f", totalOwed)
        findViewById<TextView>(R.id.tvNetAmount).text = String.format(Locale.getDefault(), "₱%.0f", totalOwed)
    }

    private fun setupRecyclerView() {
        val rvBills = findViewById<RecyclerView>(R.id.rvBills)
        rvBills.layoutManager = LinearLayoutManager(this)
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        val rvBills = findViewById<RecyclerView>(R.id.rvBills)
        rvBills.adapter = BillAdapter(BillRepository.getBills())
    }
}