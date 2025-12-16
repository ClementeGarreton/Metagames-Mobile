package com.example.metagames.viewmodel.auth

import android.app.Application
import com.example.metagames.data.repository.AuthRepository
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
class LoginViewModelTest {

    @Mock
    private lateinit var mockApplication: Application

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(mockApplication)
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
    fun `login should fail with empty email`() = runTest {
        // Given
        viewModel.setEmail("")
        viewModel.setPassword("password123")

        // When
        viewModel.login()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Por favor completa todos los campos", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `login should fail with empty password`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("")

        // When
        viewModel.login()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Por favor completa todos los campos", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `login should fail with invalid email format`() = runTest {
        // Given
        viewModel.setEmail("invalid-email")
        viewModel.setPassword("password123")

        // When
        viewModel.login()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.value.error)
        assertEquals("Email inválido", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isSuccess)
    }

    @Test
    fun `login should show loading state during login`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("password123")

        // When
        viewModel.login()

        // Then - should be loading before completion
        assertTrue(viewModel.state.value.isLoading)

        advanceUntilIdle()

        // After completion, should not be loading
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `login should succeed with valid credentials`() = runTest {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("password123")

        // When
        viewModel.login()
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
        viewModel.login()

        // When
        viewModel.setEmail("new@example.com")

        // Then
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `setPassword should clear error`() {
        // Given
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("")
        viewModel.login()

        // When
        viewModel.setPassword("newpassword")

        // Then
        assertNull(viewModel.state.value.error)
    }
}