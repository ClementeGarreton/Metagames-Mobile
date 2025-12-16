package com.example.metagames.data.repository

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class ScoreRepositoryTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var scoreRepository: ScoreRepository

    @Before
    fun setup() {
        `when`(mockContext.getSharedPreferences("metagames_scores", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)

        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putInt(anyString(), anyInt())).thenReturn(mockEditor)
        `when`(mockEditor.remove(anyString())).thenReturn(mockEditor)
        `when`(mockEditor.apply()).then { }

        scoreRepository = ScoreRepository(mockContext)
    }

    @Test
    fun `saveScore should store score value`() {
        // Given
        val score = 1500

        // When
        scoreRepository.saveScore(score)

        // Then
        verify(mockEditor).putInt("last_score", score)
        verify(mockEditor).apply()
    }

    @Test
    fun `getLastScore should return stored score`() {
        // Given
        val expectedScore = 2000
        `when`(mockSharedPreferences.getInt("last_score", 0)).thenReturn(expectedScore)

        // When
        val score = scoreRepository.getLastScore()

        // Then
        assertEquals(expectedScore, score)
    }

    @Test
    fun `getLastScore should return 0 when no score exists`() {
        // Given
        `when`(mockSharedPreferences.getInt("last_score", 0)).thenReturn(0)

        // When
        val score = scoreRepository.getLastScore()

        // Then
        assertEquals(0, score)
    }

    @Test
    fun `clearScore should remove score from preferences`() {
        // When
        scoreRepository.clearScore()

        // Then
        verify(mockEditor).remove("last_score")
        verify(mockEditor).apply()
    }

    @Test
    fun `hasScore should return true when score exists`() {
        // Given
        `when`(mockSharedPreferences.contains("last_score")).thenReturn(true)

        // When
        val hasScore = scoreRepository.hasScore()

        // Then
        assertTrue(hasScore)
    }

    @Test
    fun `hasScore should return false when score does not exist`() {
        // Given
        `when`(mockSharedPreferences.contains("last_score")).thenReturn(false)

        // When
        val hasScore = scoreRepository.hasScore()

        // Then
        assertFalse(hasScore)
    }

    @Test
    fun `saveScore should update existing score`() {
        // Given
        val firstScore = 1000
        val secondScore = 2000

        // When
        scoreRepository.saveScore(firstScore)
        scoreRepository.saveScore(secondScore)

        // Then
        verify(mockEditor, times(2)).putInt("last_score", anyInt())
        verify(mockEditor).putInt("last_score", secondScore)
    }
}