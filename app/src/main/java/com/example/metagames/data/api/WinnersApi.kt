package com.example.metagames.data.api

import com.example.metagames.data.dto.WinnerDto
import retrofit2.http.GET

interface WinnersApi {
    @GET("winners")
    suspend fun getWinners(): List<WinnerDto>
}
