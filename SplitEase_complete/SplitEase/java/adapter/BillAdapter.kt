package com.example.splitease.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.splitease.R
import com.example.splitease.model.Bill

class BillAdapter(
    private val context: Context,
    private val bills: List<Bill>,
    private val onBillClick: (Bill) -> Unit
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    inner class BillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBillIcon: TextView = view.findViewById(R.id.tvBillIcon)
        val tvBillTitle: TextView = view.findViewById(R.id.tvBillTitle)
        val tvBillDetails: TextView = view.findViewById(R.id.tvBillDetails)
        val tvBillAmount: TextView = view.findViewById(R.id.tvBillAmount)
        val tvBillStatus: TextView = view.findViewById(R.id.tvBillStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = bills[position]
        holder.tvBillIcon.text = bill.icon
        holder.tvBillTitle.text = bill.title
        holder.tvBillDetails.text = "${bill.members.size} people. ${bill.date}"
        holder.tvBillAmount.text = "₱${bill.totalAmount.toInt()}"

        if (bill.isSettled) {
            holder.tvBillStatus.text = "settled"
            holder.tvBillStatus.setTextColor(ContextCompat.getColor(context, R.color.green_primary))
        } else {
            holder.tvBillStatus.text = "${bill.unpaidCount} unpaid"
            holder.tvBillStatus.setTextColor(ContextCompat.getColor(context, R.color.red_unpaid))
        }

        holder.itemView.setOnClickListener { onBillClick(bill) }
    }

    override fun getItemCount() = bills.size
}
