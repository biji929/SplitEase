package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewBillActivity : AppCompatActivity() {
    private lateinit var etAmount: EditText
    private lateinit var etBillTitle: EditText
    private lateinit var tvEachPersonPays: TextView
    private var numPeople = 1 
    private var selectedCategory = "General"

    private val participants = mutableListOf<String>("Me")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_bill)

        etAmount = findViewById(R.id.etAmount)
        etBillTitle = findViewById(R.id.etBillTitle)
        tvEachPersonPays = findViewById(R.id.tvEachPersonPays)

        findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveBill()
        }

        findViewById<Button>(R.id.btnSplitBill).setOnClickListener {
            saveBill()
        }

        etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateSplit()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val chips = mapOf(
            findViewById<CardView>(R.id.chipFood) to "Food",
            findViewById<CardView>(R.id.chipTransport) to "Transport",
            findViewById<CardView>(R.id.chipStay) to "Stay",
            findViewById<CardView>(R.id.chipFun) to "Fun"
        )

        chips.forEach { (chip, category) ->
            chip.setOnClickListener {
                selectedCategory = category
                updateChipsUI(chip, chips.keys.toList())
            }
        }

        findViewById<CardView>(R.id.btnAddPerson).setOnClickListener {
            showAddPersonDialog()
        }

        findViewById<CardView>(R.id.cardSplitMethod).setOnClickListener {
            showSplitMethodDialog()
        }
    }

    private fun saveBill() {
        val amountStr = etAmount.text.toString()
        val titleStr = etBillTitle.text.toString().trim()
        
        if (amountStr.isNotEmpty()) {
            val amount = amountStr.toDouble()
            val billTitle = if (titleStr.isNotEmpty()) titleStr else "Bill ${BillRepository.getBills().size + 1}"
            
            val bill = Bill(
                title = billTitle,
                amount = amount,
                category = selectedCategory,
                date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
                participants = participants.toList()
            )
            BillRepository.addBill(bill)
            
            // Automatically update Group expenses if a group name matches the title or is specified
            // For now, let's show a dialog to pick a group or just match by title prefix
            GroupRepository.updateGroupExpense(selectedCategory, amount) 

            Toast.makeText(this@NewBillActivity, "Bill saved successfully! ✅", Toast.LENGTH_SHORT).show()
            
            val intent = Intent(this@NewBillActivity, SplitSummaryActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@NewBillActivity, "Please enter an amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddPersonDialog() {
        val builder = AlertDialog.Builder(this@NewBillActivity)
        builder.setTitle("Add Person")

        val input = EditText(this@NewBillActivity)
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
        val builder = AlertDialog.Builder(this@NewBillActivity)
        builder.setTitle("Select Split Method")
        builder.setItems(methods) { _, which ->
            val selectedMethod = methods[which]
            findViewById<TextView>(R.id.tvSplitMethod).text = selectedMethod
            Toast.makeText(this@NewBillActivity, "Method changed to $selectedMethod", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun addPersonToUI(name: String) {
        val container = findViewById<LinearLayout>(R.id.layoutParticipants)
        val addButton = findViewById<CardView>(R.id.btnAddPerson)

        val newPersonCard = CardView(this@NewBillActivity).apply {
            layoutParams = LinearLayout.LayoutParams(
                (48 * resources.displayMetrics.density).toInt(),
                (48 * resources.displayMetrics.density).toInt()
            ).apply {
                marginEnd = (8 * resources.displayMetrics.density).toInt()
            }
            radius = 14 * resources.displayMetrics.density
            cardElevation = 0f
            setCardBackgroundColor(getColor(R.color.brand_green))
        }

        val textView = TextView(this@NewBillActivity).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            text = if (name.length >= 2) name.substring(0, 2).uppercase() else name.uppercase()
            setTextColor(getColor(R.color.white))
            gravity = Gravity.CENTER
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
                val participantsCount = participants.size
                val eachPays = if (participantsCount > 0) amount / participantsCount else 0.0
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
