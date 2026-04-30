package cit.edu.dacumos.splitease

data class Group(
    val id: String,
    val name: String,
    val members: List<String>,
    val totalExpense: Double = 0.0
)

object GroupRepository {
    private val groupsList = mutableListOf<Group>()

    init {
        // Sample data
        groupsList.add(Group("1", "Flatmates", listOf("Me", "Alice", "Bob"), 4500.0))
        groupsList.add(Group("2", "Boracay Trip", listOf("Me", "Charlie", "Dave"), 12000.0))
    }

    fun getGroups(): List<Group> = groupsList

    fun addGroup(group: Group) {
        groupsList.add(0, group)
    }

    fun updateGroupExpense(groupName: String, amount: Double) {
        val index = groupsList.indexOfFirst { it.name == groupName }
        if (index != -1) {
            val oldGroup = groupsList[index]
            groupsList[index] = oldGroup.copy(totalExpense = oldGroup.totalExpense + amount)
        }
    }
}
