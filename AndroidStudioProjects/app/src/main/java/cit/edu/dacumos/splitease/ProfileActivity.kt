package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ProfileActivity : AppCompatActivity() {

    // ── Mock data (swap in your real ViewModel / repository later) ───────────
    private val userName     = "Brixel Jay Dacumos"
    private val userEmail    = "brixeljay@email.com"
    private val billsCount   = 18
    private val splitTotal   = "₱12k"
    private val friendsCount = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bindHeader()
        bindMenuRows()
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private fun bindHeader() {
        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<TextView>(R.id.tvAvatarInitials).text = getInitials(userName)
        findViewById<TextView>(R.id.tvProfileName).text    = userName
        findViewById<TextView>(R.id.tvProfileEmail).text   = userEmail
        findViewById<TextView>(R.id.tvBillsCount).text     = billsCount.toString()
        findViewById<TextView>(R.id.tvSplitTotal).text     = splitTotal
        findViewById<TextView>(R.id.tvFriendsCount).text   = friendsCount.toString()

        findViewById<View>(R.id.ivMoreOptions).setOnClickListener {
            showMoreOptionsMenu()
        }
    }

    // ── Menu rows ─────────────────────────────────────────────────────────────

    private data class MenuRowConfig(
        val viewId:      Int,
        val iconRes:     Int,
        val iconBgColor: Int,
        val label:       String,
        val labelColor:  Int,
        val onClick:     () -> Unit
    )

    private fun bindMenuRows() {
        val rows = listOf(
            MenuRowConfig(
                viewId      = R.id.rowEditProfile,
                iconRes     = R.drawable.ic_edit,
                iconBgColor = getColor(R.color.icon_bg_edit),
                label       = "Edit profile",
                labelColor  = getColor(R.color.text_primary)
            ) { toast("Edit profile") },

            MenuRowConfig(
                viewId      = R.id.rowNotifications,
                iconRes     = R.drawable.ic_bell,
                iconBgColor = getColor(R.color.icon_bg_notif),
                label       = "Notifications",
                labelColor  = getColor(R.color.text_primary)
            ) { toast("Notifications") },

            MenuRowConfig(
                viewId      = R.id.rowPaymentMethods,
                iconRes     = R.drawable.ic_card,
                iconBgColor = getColor(R.color.icon_bg_payment),
                label       = "Payment methods",
                labelColor  = getColor(R.color.text_primary)
            ) { toast("Payment methods") },

            MenuRowConfig(
                viewId      = R.id.rowPrivacySecurity,
                iconRes     = R.drawable.ic_lock,
                iconBgColor = getColor(R.color.icon_bg_privacy),
                label       = "Privacy & security",
                labelColor  = getColor(R.color.text_primary)
            ) { toast("Privacy & security") },

            MenuRowConfig(
                viewId      = R.id.rowLogout,
                iconRes     = R.drawable.ic_logout,
                iconBgColor = getColor(R.color.icon_bg_logout),
                label       = "Log out",
                labelColor  = getColor(R.color.red_danger)
            ) { confirmLogout() }
        )

        for (row in rows) {
            val rootView = findViewById<View>(row.viewId) ?: continue

            // Bind icon drawable
            rootView.findViewById<ImageView>(R.id.ivMenuIcon)
                ?.setImageResource(row.iconRes)

            // Bind icon card background color
            rootView.findViewById<CardView>(R.id.cardMenuIconBg)
                ?.setCardBackgroundColor(row.iconBgColor)

            // Bind label text + color
            rootView.findViewById<TextView>(R.id.tvMenuLabel)?.apply {
                text = row.label
                setTextColor(row.labelColor)
            }

            // Row click
            rootView.setOnClickListener { row.onClick() }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Returns up to 2 uppercase initials from a full name. */
    private fun getInitials(name: String): String {
        val parts = name.trim().split(" ").filter { it.isNotEmpty() }
        return when {
            parts.size >= 2 -> "${parts.first()[0]}${parts.last()[0]}".uppercase()
            parts.size == 1 -> parts[0].take(2).uppercase()
            else            -> "?"
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun showMoreOptionsMenu() {
        val options = arrayOf("Share profile", "Help & support", "About SplitEase")
        AlertDialog.Builder(this)
            .setItems(options) { _, i -> toast(options[i]) }
            .show()
    }

    private fun confirmLogout() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<Button>(R.id.btnYesLogout).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        dialogView.findViewById<Button>(R.id.btnStayLoggedIn).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}