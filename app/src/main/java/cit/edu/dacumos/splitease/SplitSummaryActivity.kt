package cit.edu.dacumos.splitease

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SplitSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_summary)

        setupToolbar()
        loadBillData()
    }

    private fun setupToolbar() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<ImageButton>(R.id.btnMore).setOnClickListener {
            Toast.makeText(this@SplitSummaryActivity, "More options coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnSendReminder).setOnClickListener {
            Toast.makeText(this@SplitSummaryActivity, "Reminders sent to all unpaid members! 🔔", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadBillData() {
        val latestBill = BillRepository.getLatestBill() ?: return

        // Set Bill Header
        val icon = when (latestBill.category.lowercase(Locale.getDefault())) {
            "food" -> "🍕"
            "transport" -> "🚗"
            "stay" -> "🏠"
            "fun" -> "🎮"
            else -> "📄"
        }
        findViewById<TextView>(R.id.tvBillIcon).text = icon
        findViewById<TextView>(R.id.tvBillName).text = "${latestBill.title} · ${latestBill.date}"

        // Set Stats
        val numPeople = latestBill.participants.size
        val perPerson = if (numPeople > 0) latestBill.amount / numPeople else 0.0
        
        findViewById<TextView>(R.id.tvTotal).text = String.format(Locale.getDefault(), "₱%.2f", latestBill.amount)
        findViewById<TextView>(R.id.tvPerPerson).text = String.format(Locale.getDefault(), "₱%.2f", perPerson)
        
        // Assume everyone except "Me" is unpaid for initial display
        val unpaidCount = latestBill.participants.filter { it != "Me" }.size
        findViewById<TextView>(R.id.tvUnpaidCount).text = unpaidCount.toString()

        // Set up Members List
        val rvMembers = findViewById<RecyclerView>(R.id.rvMembers)
        rvMembers.layoutManager = LinearLayoutManager(this@SplitSummaryActivity)
        
        val memberPayments = latestBill.participants.map { name ->
            MemberPayment(
                name = if (name == "Me") "Me (you)" else name,
                amount = perPerson,
                isPaid = false // Set everyone to unpaid by default
            )
        }
        
        val adapter = MemberAdapter(memberPayments) { position ->
            val member = memberPayments[position]
            member.isPaid = !member.isPaid
            
            // If the person who paid is "Me", update the global repository
            if (member.name.contains("Me", ignoreCase = true)) {
                // We need the bill title. Let's assume we can get it from the intent or a repository.
                // For this demo, we'll mark the latest bill as paid by "Me".
                BillRepository.getLatestBill()?.let { bill ->
                    BillRepository.markMePaid(bill.title, member.isPaid)
                }
            }

            updateUnpaidCount(memberPayments)
            rvMembers.adapter?.notifyItemChanged(position)
        }
        rvMembers.adapter = adapter
    }

    private fun updateUnpaidCount(members: List<MemberPayment>) {
        val unpaidCount = members.count { !it.isPaid }
        findViewById<TextView>(R.id.tvUnpaidCount).text = unpaidCount.toString()
    }
}
