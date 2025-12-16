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
class AuthRepositoryTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        `when`(mockContext.getSharedPreferences("metagames_auth", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)

        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)
        `when`(mockEditor.apply()).then { }

        authRepository = AuthRepository(mockContext)
    }

    @Test
    fun `saveUser should store email and token`() {
        // Given
        val email = "test@example.com"
        val token = "test_token_123"

        // When
        authRepository.saveUser(email, token)

        // Then
        verify(mockEditor).putString("email", email)
        verify(mockEditor).putString("token", token)
        verify(mockEditor).apply()
    }

    @Test
    fun `getUser should return User when data exists`() {
        // Given
        val email = "test@example.com"
        val token = "test_token_123"
        `when`(mockSharedPreferences.getString("email", null)).thenReturn(email)
        `when`(mockSharedPreferences.getString("token", null)).thenReturn(token)

        // When
        val user = authRepository.getUser()

        // Then
        assertNotNull(user)
        assertEquals(email, user?.email)
        assertEquals(token, user?.token)
    }

    @Test
    fun `getUser should return null when no data exists`() {
        // Given
        `when`(mockSharedPreferences.getString("email", null)).thenReturn(null)
        `when`(mockSharedPreferences.getString("token", null)).thenReturn(null)

        // When
        val user = authRepository.getUser()

        // Then
        assertNull(user)
    }

    @Test
    fun `logout should clear preferences`() {
        // When
        authRepository.logout()

        // Then
        verify(mockEditor).clear()
        verify(mockEditor).apply()
    }

    @Test
    fun `isLoggedIn should return true when user exists`() {
        // Given
        `when`(mockSharedPreferences.getString("email", null)).thenReturn("test@example.com")
        `when`(mockSharedPreferences.getString("token", null)).thenReturn("token")

        // When
        val isLoggedIn = authRepository.isLoggedIn()

        // Then
        assertTrue(isLoggedIn)
    }

    @Test
    fun `isLoggedIn should return false when user does not exist`() {
        // Given
        `when`(mockSharedPreferences.getString("email", null)).thenReturn(null)
        `when`(mockSharedPreferences.getString("token", null)).thenReturn(null)

        // When
        val isLoggedIn = authRepository.isLoggedIn()

        // Then
        assertFalse(isLoggedIn)
    }
}