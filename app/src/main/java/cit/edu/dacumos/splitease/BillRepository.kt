package cit.edu.dacumos.splitease

data class Bill(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val participants: List<String> = listOf("Me"),
    var isMePaid: Boolean = false,
    val otherParticipantsPaid: MutableMap<String, Boolean> = mutableMapOf()
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

    fun markMePaid(billTitle: String, isPaid: Boolean) {
        billsList.find { it.title == billTitle }?.isMePaid = isPaid
    }

    fun getTotalOwed(): Double {
        var total = 0.0
        for (bill in billsList) {
            if (!bill.isMePaid) {
                // Assuming "Me" is one of the participants
                val myShare = bill.amount / bill.participants.size
                total += (bill.amount - myShare) // Total others owe you if you paid the whole bill
                // OR if you haven't paid your share:
                // total += myShare 
                
                // Let's stick to the logic: total amount minus your share is what's "owed" to you by others
                // But if "you" paid, it should show what others owe you.
                // If the user meant "I paid my debt", then it's about what you owe others.
            }
        }
        // Simple logic for demo: Total of all bills where you haven't marked as paid
        return billsList.filter { !it.isMePaid }.sumOf { it.amount / 2 }
    }
}
