package com.example.metagames.data.repository

import android.content.Context
import android.content.SharedPreferences

data class User(
    val email: String,
    val token: String
)

class AuthRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("metagames_auth", Context.MODE_PRIVATE)

    fun saveUser(email: String, token: String) {
        prefs.edit().apply {
            putString("email", email)
            putString("token", token)
            apply()
        }
    }

    fun getUser(): User? {
        val email = prefs.getString("email", null)
        val token = prefs.getString("token", null)

        return if (email != null && token != null) {
            User(email, token)
        } else null
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getUser() != null
}