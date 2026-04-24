package cit.edu.dacumos.splitease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class BillAdapter(private val bills: List<Bill>) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    class BillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvBillTitle)
        val tvDate: TextView = view.findViewById(R.id.tvBillDate)
        val tvAmount: TextView = view.findViewById(R.id.tvBillAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = bills[position]
        holder.tvTitle.text = bill.title
        holder.tvDate.text = bill.date
        holder.tvAmount.text = String.format(Locale.getDefault(), "₱%.2f", bill.amount)
    }

    override fun getItemCount() = bills.size
}