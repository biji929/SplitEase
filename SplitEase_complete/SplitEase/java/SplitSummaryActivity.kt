package com.example.splitease

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitease.adapter.MemberPaymentAdapter
import com.example.splitease.databinding.ActivitySplitSummaryBinding
import com.example.splitease.model.Member

class SplitSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplitSummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplitSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val billTitle = intent.getStringExtra("bill_title") ?: "Bill"
        val billTotal = intent.getDoubleExtra("bill_total", 0.0)
        val perPerson = intent.getDoubleExtra("bill_per_person", 0.0)
        val unpaidCount = intent.getIntExtra("bill_unpaid", 0)
        val memberNames = intent.getStringArrayListExtra("member_names") ?: arrayListOf()
        val memberPaid = intent.getBooleanArrayExtra("member_paid") ?: BooleanArray(memberNames.size)

        binding.tvBillName.text = billTitle
        binding.tvTotal.text = "₱${billTotal.toInt()}"
        binding.tvPerPerson.text = "₱${perPerson.toInt()}"
        binding.tvUnpaidCount.text = unpaidCount.toString()

        val members = memberNames.mapIndexed { i, name ->
            Member(name, memberPaid.getOrElse(i) { false })
        }

        val adapter = MemberPaymentAdapter(this, members, perPerson)
        binding.rvMembers.layoutManager = LinearLayoutManager(this)
        binding.rvMembers.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        binding.btnSendReminder.setOnClickListener {
            val unpaidNames = members.filter { !it.hasPaid }.map { it.name }
            if (unpaidNames.isEmpty()) {
                Toast.makeText(this, "Everyone has paid! 🎉", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Reminder sent to: ${unpaidNames.joinToString(", ")} ✈",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
