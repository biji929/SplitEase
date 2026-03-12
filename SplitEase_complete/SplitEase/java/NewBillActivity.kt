package com.example.splitease

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splitease.databinding.ActivityNewBillBinding

class NewBillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBillBinding
    private var selectedCategory = "food"

    private val defaultMembers = listOf("BR", "KE", "RO", "SE", "SA", "RO", "ST", "LA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategoryChips()
        setupMemberChips()
        setupAmountListener()

        binding.btnClose.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val title = binding.etBillTitle.text.toString().trim()
            val amountStr = binding.etAmount.text.toString().trim()

            if (title.isEmpty()) {
                binding.etBillTitle.error = "Enter a bill title"
                return@setOnClickListener
            }
            if (amountStr.isEmpty()) {
                binding.etAmount.error = "Enter an amount"
                return@setOnClickListener
            }

            Toast.makeText(this, "Bill '$title' saved! ✅", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupCategoryChips() {
        binding.chipFood.setOnClickListener { selectCategory("food") }
        binding.chipTranspo.setOnClickListener { selectCategory("transpo") }
        binding.chipStay.setOnClickListener { selectCategory("stay") }
        binding.chipFun.setOnClickListener { selectCategory("fun") }
    }

    private fun selectCategory(cat: String) {
        selectedCategory = cat
        // Visual feedback - reset all then highlight selected
        listOf(binding.chipFood, binding.chipTranspo, binding.chipStay, binding.chipFun)
            .forEach { it.setCardBackgroundColor(getColor(R.color.chip_bg)) }

        val selected = when (cat) {
            "food" -> binding.chipFood
            "transpo" -> binding.chipTranspo
            "stay" -> binding.chipStay
            "fun" -> binding.chipFun
            else -> binding.chipFood
        }
        selected.setCardBackgroundColor(getColor(R.color.green_primary))
    }

    private fun setupMemberChips() {
        val colors = listOf("#2D7A50", "#6A1B9A", "#E53935", "#1B5E3B", "#E65100", "#1565C0", "#00838F", "#5D4037")
        defaultMembers.forEachIndexed { index, initials ->
            val chip = com.google.android.material.chip.Chip(this).apply {
                text = initials
                textSize = 12f
                setTextColor(android.graphics.Color.WHITE)
                chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor(colors[index % colors.size])
                )
                isCloseIconVisible = false
                isCheckable = true
                isChecked = true
            }
            binding.chipGroupMembers.addView(chip)
        }
    }

    private fun setupAmountListener() {
        binding.etAmount.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val amount = s.toString().toDoubleOrNull() ?: 0.0
                val checkedCount = (0 until binding.chipGroupMembers.childCount)
                    .count { (binding.chipGroupMembers.getChildAt(it) as? com.google.android.material.chip.Chip)?.isChecked == true }
                    .coerceAtLeast(1)
                val perPerson = amount / checkedCount
                // Update hint
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
