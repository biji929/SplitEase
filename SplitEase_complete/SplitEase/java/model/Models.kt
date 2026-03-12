package com.example.splitease.model

data class Bill(
    val id: String,
    val title: String,
    val totalAmount: Double,
    val category: String,
    val members: List<Member>,
    val date: String,
    val splitMethod: String = "Equal"
) {
    val perPerson: Double get() = if (members.isNotEmpty()) totalAmount / members.size else 0.0
    val unpaidCount: Int get() = members.count { !it.hasPaid }
    val isSettled: Boolean get() = members.all { it.hasPaid }
    val icon: String get() = when (category) {
        "food" -> "🍕"
        "transpo" -> "🚗"
        "stay" -> "🏠"
        "fun" -> "🎮"
        else -> "💸"
    }
}

data class Member(
    val name: String,
    val hasPaid: Boolean = false,
    val avatarColor: Int = 0
) {
    val initials: String get() = name.trim().split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2).joinToString("")
}

data class User(
    val fullName: String,
    val email: String,
    val phone: String = ""
) {
    val initials: String get() = fullName.trim().split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2).joinToString("")
}
