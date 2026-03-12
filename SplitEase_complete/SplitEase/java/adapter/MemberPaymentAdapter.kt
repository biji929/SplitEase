package com.example.splitease.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.splitease.R
import com.example.splitease.model.Member

class MemberPaymentAdapter(
    private val context: Context,
    private val members: List<Member>,
    private val amountEach: Double
) : RecyclerView.Adapter<MemberPaymentAdapter.ViewHolder>() {

    private val avatarColors = listOf(
        "#2D7A50", "#E53935", "#1565C0", "#6A1B9A", "#E65100", "#00838F"
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitials: TextView = view.findViewById(R.id.tvMemberInitials)
        val tvName: TextView = view.findViewById(R.id.tvMemberName)
        val tvStatus: TextView = view.findViewById(R.id.tvPaymentStatus)
        val tvAmount: TextView = view.findViewById(R.id.tvMemberAmount)
        val tvBadge: TextView = view.findViewById(R.id.tvStatusBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_member_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members[position]
        holder.tvInitials.text = member.initials
        holder.tvInitials.setBackgroundColor(Color.parseColor(avatarColors[position % avatarColors.size]))
        holder.tvName.text = member.name
        holder.tvAmount.text = "₱${amountEach.toInt()}"

        if (member.hasPaid) {
            holder.tvStatus.text = "paid"
            holder.tvBadge.text = "paid"
            holder.tvBadge.setTextColor(ContextCompat.getColor(context, R.color.green_primary))
        } else {
            holder.tvStatus.text = "pending payment"
            holder.tvBadge.text = "Unpaid"
            holder.tvBadge.setTextColor(ContextCompat.getColor(context, R.color.red_unpaid))
        }
    }

    override fun getItemCount() = members.size
}
