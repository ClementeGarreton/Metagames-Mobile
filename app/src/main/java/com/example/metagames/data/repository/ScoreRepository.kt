package com.example.metagames.data.repository

import android.content.Context
import android.content.SharedPreferences

class ScoreRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("metagames_scores", Context.MODE_PRIVATE)

    fun saveScore(score: Int) {
        prefs.edit().putInt("last_score", score).apply()
    }

    fun getLastScore(): Int {
        return prefs.getInt("last_score", 0)
    }

    fun clearScore() {
        prefs.edit().remove("last_score").apply()
    }

    fun hasScore(): Boolean {
        return prefs.contains("last_score")
    }
}