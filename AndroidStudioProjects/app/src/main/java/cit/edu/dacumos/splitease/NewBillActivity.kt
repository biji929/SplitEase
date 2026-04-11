package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale

class NewBillActivity : AppCompatActivity() {
    private lateinit var etAmount: EditText
    private lateinit var tvEachPersonPays: TextView
    private var numPeople = 1 // Starting with just "ME"

    private val participants = mutableListOf<String>("Me")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_bill)

        etAmount = findViewById(R.id.etAmount)
        tvEachPersonPays = findViewById(R.id.tvEachPersonPays)

        findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val amountStr = etAmount.text.toString()
            if (amountStr.isNotEmpty()) {
                val bill = Bill(
                    title = "Bill ${BillRepository.getBills().size + 1}", // Simple title for now
                    amount = amountStr.toDouble(),
                    category = "General",
                    date = "Today, " + java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date()),
                    participants = participants.toList()
                )
                BillRepository.addBill(bill)
                Toast.makeText(this, "Bill saved successfully! ✅", Toast.LENGTH_SHORT).show()
                
                val intent = Intent(this, SplitSummaryActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnSplitBill).setOnClickListener {
            val intent = Intent(this, SplitSummaryActivity::class.java)
            startActivity(intent)
        }

        // Make amount calculation functional
        etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateSplit()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Category Chips logic
        val chips = listOf(
            findViewById<CardView>(R.id.chipFood),
            findViewById<CardView>(R.id.chipTransport),
            findViewById<CardView>(R.id.chipStay),
            findViewById<CardView>(R.id.chipFun)
        )

        chips.forEach { chip ->
            chip.setOnClickListener {
                updateChipsUI(chip, chips)
            }
        }

        findViewById<CardView>(R.id.btnAddPerson).setOnClickListener {
            showAddPersonDialog()
        }

        findViewById<CardView>(R.id.cardSplitMethod).setOnClickListener {
            showSplitMethodDialog()
        }
    }

    private fun showAddPersonDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Add Person")

        val input = EditText(this)
        input.hint = "Enter name"
        input.setPadding(48, 32, 48, 32)
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val name = input.text.toString().trim()
            if (name.isNotEmpty()) {
                participants.add(name)
                numPeople++
                addPersonToUI(name)
                calculateSplit()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showSplitMethodDialog() {
        val methods = arrayOf("Equal split", "Percentage", "Exact amounts")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Split Method")
        builder.setItems(methods) { _, which ->
            val selectedMethod = methods[which]
            findViewById<TextView>(R.id.tvSplitMethod).text = selectedMethod
            Toast.makeText(this, "Method changed to $selectedMethod", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun addPersonToUI(name: String) {
        val container = findViewById<android.widget.LinearLayout>(R.id.layoutParticipants)
        val addButton = findViewById<CardView>(R.id.btnAddPerson)

        val newPersonCard = CardView(this).apply {
            layoutParams = android.widget.LinearLayout.LayoutParams(
                (48 * resources.displayMetrics.density).toInt(),
                (48 * resources.displayMetrics.density).toInt()
            ).apply {
                marginEnd = (8 * resources.displayMetrics.density).toInt()
            }
            radius = 14 * resources.displayMetrics.density
            cardElevation = 0f
            setCardBackgroundColor(getColor(R.color.green_dark))
        }

        val textView = TextView(this).apply {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
            )
            // Use first two letters of name as initials
            text = if (name.length >= 2) name.substring(0, 2).uppercase() else name.uppercase()
            setTextColor(getColor(R.color.white))
            gravity = android.view.Gravity.CENTER
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        newPersonCard.addView(textView)
        val index = container.indexOfChild(addButton)
        container.addView(newPersonCard, index)
    }

    private fun calculateSplit() {
        val amountStr = etAmount.text.toString()
        if (amountStr.isNotEmpty()) {
            try {
                val amount = amountStr.toDouble()
                val eachPays = amount / numPeople
                tvEachPersonPays.text = String.format(Locale.getDefault(), "Each person pays ₱%.2f", eachPays)
            } catch (e: Exception) {
                tvEachPersonPays.text = "Each person pays ₱0.00"
            }
        } else {
            tvEachPersonPays.text = "Each person pays ₱0.00"
        }
    }

    private fun updateChipsUI(selected: CardView, allChips: List<CardView>) {
        allChips.forEach { chip ->
            val textView = (chip.getChildAt(0) as TextView)
            if (chip == selected) {
                chip.setCardBackgroundColor(getColor(R.color.green_chip))
                textView.setTextColor(getColor(R.color.green_secondary))
                textView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                chip.setCardBackgroundColor(getColor(R.color.white))
                textView.setTextColor(getColor(R.color.text_secondary))
                textView.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }
}
