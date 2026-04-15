package cit.edu.dacumos.splitease

data class Bill(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val participants: List<String> = listOf("Me")
)

object BillRepository {
    private val billsList = mutableListOf<Bill>()

    fun addBill(bill: Bill) {
        billsList.add(0, bill) // Add to top
    }

    fun getBills(): List<Bill> {
        return billsList
    }

    fun getLatestBill(): Bill? {
        return billsList.firstOrNull()
    }

    fun getTotalOwed(): Double {
        // Mock logic for demo
        return billsList.sumOf { it.amount / 2 }
    }
}
