package cit.edu.dacumos.splitease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

data class MemberPayment(
    val name: String,
    val amount: Double,
    var isPaid: Boolean
)

class MemberAdapter(
    private val members: List<MemberPayment>,
    private val onPaidClick: (Int) -> Unit
) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitials: TextView = view.findViewById(R.id.tvMemberInitials)
        val tvName: TextView = view.findViewById(R.id.tvMemberName)
        val tvStatus: TextView = view.findViewById(R.id.tvPaymentStatus)
        val tvAmount: TextView = view.findViewById(R.id.tvMemberAmount)
        val tvStatusBadge: TextView = view.findViewById(R.id.tvStatusBadge)
        val btnPaid: Button = view.findViewById(R.id.btnMarkAsPaid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member_payment, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        
        holder.tvName.text = member.name
        holder.tvInitials.text = if (member.name.length >= 2) member.name.substring(0, 2).uppercase() else member.name.uppercase()
        holder.tvAmount.text = String.format(Locale.getDefault(), "₱%.2f", member.amount)
        
        if (member.isPaid) {
            holder.tvStatus.text = "paid"
            holder.tvStatusBadge.text = "PAID"
            holder.tvStatusBadge.setTextColor(holder.itemView.context.getColor(R.color.status_settled))
            holder.btnPaid.text = "Undo"
            holder.btnPaid.setTextColor(holder.itemView.context.getColor(R.color.text_secondary))
        } else {
            holder.tvStatus.text = "unpaid"
            holder.tvStatusBadge.text = "UNPAID"
            holder.tvStatusBadge.setTextColor(holder.itemView.context.getColor(R.color.red_danger))
            
            // Check if it's the current user
            holder.btnPaid.text = if (member.name.contains("Me", ignoreCase = true)) "I Paid" else "Paid?"
            holder.btnPaid.setTextColor(holder.itemView.context.getColor(R.color.green_dark))
        }

        holder.btnPaid.setOnClickListener {
            onPaidClick(position)
        }
    }

    override fun getItemCount() = members.size
}