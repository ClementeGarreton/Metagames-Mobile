package com.example.metagames.viewmodel.auth

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest {

    @Mock
    private lateinit var mockApplication: Application

    private lateinit var viewModel: SignupViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignupViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setEmail should update email in state`() {
        // Given
        val email = "test@example.com"

        // When
        viewModel.setEmail(email)

        // Then
        assertEquals(email, viewModel.state.value.email)
    }

    @Test
    fun `setPassword should update password in state`() {
        // Given
        val password = "password123"

        // When
        viewModel.setPassword(password)

        // Then
        assertEquals(password, viewModel.state.value.password)
    }

    @Test
    fun `setConfirmPassword should update confirmPassword in state`() {
        // Given
        val confirmPassword = "password123"

        // When
        viewModel.setConfirmPassword(confirmPassword)

        // Then
        assertEquals(confirmPassword, viewModel.state.value.confirmPassword)
    }

    @Test
    fun `signup should fail with empty fields`() = runTest {
        // Given
        viewModel.setEmail("")
        viewModel.setPassword("")
        viewModel.setConfirmPassword("")

        // When
        viewModel.signup()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Por favor completa todos los campos", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `signup should fail with invalid email format`() = runTest {
        // Given
        viewModel.setEmail("invalid-email")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("password123")

        // When
        viewModel.signup()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Email inválido", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `signup should fail with password less than 6 characters`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("pass")
        viewModel.setConfirmPassword("pass")

        // When
        viewModel.signup()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `signup should fail when passwords do not match`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("different123")

        // When
        viewModel.signup()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Las contraseñas no coinciden", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `signup should show loading state during signup`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("password123")

        // When
        viewModel.signup()

        // Then - should be loading before completion
        assertTrue(viewModel.state.value.isLoading)

        advanceUntilIdle()

        // After completion, should not be loading
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `signup should succeed with valid data`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("password123")

        // When
        viewModel.signup()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value.isSuccess)
        assertNull(viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `setEmail should clear error`() {
        // Given
        viewModel.setEmail("")
        viewModel.setPassword("pass")
        viewModel.setConfirmPassword("pass")
        viewModel.signup()

        // When
        viewModel.setEmail("new@example.com")

        // Then
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `password validation should be case sensitive`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("Password123")
        viewModel.setConfirmPassword("password123")

        // When
        viewModel.signup()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Las contraseñas no coinciden", viewModel.state.value.error)
    }
}