package com.example.metagames.data.repository

import com.example.metagames.data.api.WinnersApi
import com.example.metagames.data.dto.WinnerDto

data class Winner(
    val email: String,
    val score: Int
)

class WinnersRepository(
    private val api: WinnersApi
) {
    suspend fun fetchWinners(): List<Winner> {
        val data: List<WinnerDto> = api.getWinners()

        // Normaliza como en tu React: tolera nulls y tipos raros
        return data.map {
            Winner(
                email = it.email ?: "",
                score = it.score ?: 0
            )
        }
    }
}
