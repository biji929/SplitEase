package cit.edu.dacumos.splitease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class GroupsActivity : AppCompatActivity() {

    private lateinit var groupAdapter: GroupAdapter
    private lateinit var rvGroups: RecyclerView
    private lateinit var layoutEmptyState: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        rvGroups = findViewById(R.id.rvGroups)
        layoutEmptyState = findViewById(R.id.layoutEmptyState)

        setupRecyclerView()
        setupBottomNavigation()
        refreshGroups()

        findViewById<Button>(R.id.btnCreateGroup).setOnClickListener {
            showCreateGroupDialog()
        }
    }

    private fun setupRecyclerView() {
        rvGroups.layoutManager = LinearLayoutManager(this)
    }

    private fun refreshGroups() {
        val groups = GroupRepository.getGroups()
        if (groups.isEmpty()) {
            rvGroups.visibility = View.GONE
            layoutEmptyState.visibility = View.VISIBLE
        } else {
            rvGroups.visibility = View.VISIBLE
            layoutEmptyState.visibility = View.GONE
            groupAdapter = GroupAdapter(groups)
            rvGroups.adapter = groupAdapter
        }
    }

    private fun showCreateGroupDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_group, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val etName = dialogView.findViewById<EditText>(R.id.etGroupName)
        val etMembers = dialogView.findViewById<EditText>(R.id.etGroupMembers)

        dialogView.findViewById<Button>(R.id.btnCancelCreate).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnSaveGroup).setOnClickListener {
            val name = etName.text.toString().trim()
            val membersStr = etMembers.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val members = membersStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
            members.add(0, "Me")

            val newGroup = Group(
                id = System.currentTimeMillis().toString(),
                name = name,
                members = members,
                totalExpense = 0.0
            )

            GroupRepository.addGroup(newGroup)
            refreshGroups()
            Toast.makeText(this, "Group '$name' created!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_groups
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_groups -> true
                R.id.nav_activity -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
