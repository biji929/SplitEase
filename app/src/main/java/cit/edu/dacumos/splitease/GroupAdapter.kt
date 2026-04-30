package cit.edu.dacumos.splitease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class GroupAdapter(private val groups: List<Group>) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvGroupIcon)
        val tvName: TextView = view.findViewById(R.id.tvGroupName)
        val tvMembers: TextView = view.findViewById(R.id.tvGroupMembers)
        val tvTotal: TextView = view.findViewById(R.id.tvGroupTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.tvName.text = group.name
        holder.tvIcon.text = group.name.take(1).uppercase()
        holder.tvMembers.text = group.members.joinToString(", ")
        holder.tvTotal.text = String.format(Locale.getDefault(), "₱%,.0f", group.totalExpense)
    }

    override fun getItemCount() = groups.size
}
