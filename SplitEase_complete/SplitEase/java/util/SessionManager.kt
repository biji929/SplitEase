package com.example.splitease.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("splitease_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_PHONE = "user_phone"
    }

    fun saveUser(name: String, email: String, phone: String = "") {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHONE, phone)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "User") ?: "User"

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""

    fun logout() {
        prefs.edit().clear().apply()
    }
}
