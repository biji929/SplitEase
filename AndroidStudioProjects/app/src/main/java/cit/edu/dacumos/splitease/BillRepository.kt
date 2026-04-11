package cit.edu.dacumos.splitease

data class Bill(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val participants: List<String> = listOf("Me")
)

object BillRepository {
    private val bills = mutableListOf<Bill>()

    fun addBill(bill: Bill) {
        bills.add(0, bill) // Add to top
    }

    fun getBills(): List<Bill> {
        return bills
    }

    fun getLatestBill(): Bill? {
        return bills.firstOrNull()
    }

    fun getTotalOwed(): Double {
        // Mock logic for demo
        return bills.sumOf { it.amount / 2 } 
    }
}