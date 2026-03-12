package com.example.splitease

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitease.adapter.BillAdapter
import com.example.splitease.databinding.ActivityHomeBinding
import com.example.splitease.model.Bill
import com.example.splitease.model.Member
import com.example.splitease.util.SessionManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var session: SessionManager
    private val bills = mutableListOf<Bill>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        setupUserInfo()
        setupSampleBills()
        setupRecyclerView()
        setupNavigation()

        binding.fabAddBill.setOnClickListener {
            startActivity(Intent(this, NewBillActivity::class.java))
        }
    }

    private fun setupUserInfo() {
        val name = session.getUserName()
        binding.tvUserName.text = name
        binding.tvAvatar.text = name.trim().split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .take(2).joinToString("")
    }

    private fun setupSampleBills() {
        bills.clear()
        bills.addAll(listOf(
            Bill(
                id = "1",
                title = "Pizza night 🍕",
                totalAmount = 400.0,
                category = "food",
                date = "March 1",
                members = listOf(
                    Member("Brixel (you)", hasPaid = true),
                    Member("Keith", hasPaid = true),
                    Member("Selwyn", hasPaid = true),
                    Member("Steven", hasPaid = true)
                )
            ),
            Bill(
                id = "2",
                title = "Ride Grab home",
                totalAmount = 300.0,
                category = "transpo",
                date = "March 1",
                members = listOf(
                    Member("Brixel (you)", hasPaid = true),
                    Member("Keith", hasPaid = true),
                    Member("Selwyn", hasPaid = true),
                    Member("Steven", hasPaid = true)
                )
            ),
            Bill(
                id = "3",
                title = "Airbnb weekend",
                totalAmount = 300.0,
                category = "stay",
                date = "March 1",
                members = listOf(
                    Member("Brixel (you)", hasPaid = true),
                    Member("Keith", hasPaid = false),
                    Member("Selwyn", hasPaid = false),
                    Member("Steven", hasPaid = false)
                )
            )
        ))
    }

    private fun setupRecyclerView() {
        val adapter = BillAdapter(this, bills) { bill ->
            val intent = Intent(this, SplitSummaryActivity::class.java).apply {
                putExtra("bill_title", bill.title)
                putExtra("bill_total", bill.totalAmount)
                putExtra("bill_per_person", bill.perPerson)
                putExtra("bill_unpaid", bill.unpaidCount)
                putStringArrayListExtra("member_names", ArrayList(bill.members.map { it.name }))
                putExtra("member_paid", BooleanArray(bill.members.size) { bill.members[it].hasPaid })
            }
            startActivity(intent)
        }
        binding.rvBills.layoutManager = LinearLayoutManager(this)
        binding.rvBills.adapter = adapter
    }

    private fun setupNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> {
                    showLogoutDialog()
                    true
                }
                else -> true
            }
        }
    }

    private fun showLogoutDialog() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.findViewById<android.widget.Button>(R.id.btnStayLoggedIn).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<android.widget.Button>(R.id.btnYesLogout).setOnClickListener {
            session.logout()
            dialog.dismiss()
            startActivity(Intent(this, SplashActivity::class.java))
            finishAffinity()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.nav_home
    }
}
