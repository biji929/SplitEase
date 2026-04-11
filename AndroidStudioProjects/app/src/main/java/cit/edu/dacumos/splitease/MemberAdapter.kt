package cit.edu.dacumos.splitease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

data class MemberPayment(
    val name: String,
    val amount: Double,
    val isPaid: Boolean
)

class MemberAdapter(private val members: List<MemberPayment>) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitials: TextView = view.findViewById(R.id.tvMemberInitials)
        val tvName: TextView = view.findViewById(R.id.tvMemberName)
        val tvStatus: TextView = view.findViewById(R.id.tvPaymentStatus)
        val tvAmount: TextView = view.findViewById(R.id.tvMemberAmount)
        val tvStatusBadge: TextView = view.findViewById(R.id.tvStatusBadge)
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
        } else {
            holder.tvStatus.text = "unpaid"
            holder.tvStatusBadge.text = "UNPAID"
            holder.tvStatusBadge.setTextColor(holder.itemView.context.getColor(R.color.red_danger))
        }
    }

    override fun getItemCount() = members.size
}