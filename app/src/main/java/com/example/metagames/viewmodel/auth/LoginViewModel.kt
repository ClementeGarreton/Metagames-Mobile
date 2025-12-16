package com.example.metagames.viewmodel.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.metagames.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository(application)

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun setEmail(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    fun setPassword(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    fun login() {
        val s = _state.value

        if (s.email.isBlank() || s.password.isBlank()) {
            _state.value = s.copy(error = "Por favor completa todos los campos")
            return
        }

        if (!isValidEmail(s.email)) {
            _state.value = s.copy(error = "Email inválido")
            return
        }

        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, error = null)

            // Simulación de API call
            delay(1500)

            // En producción, aquí harías una llamada real a tu API
            // Por ahora, simulamos un login exitoso
            val mockToken = "mock_token_${System.currentTimeMillis()}"
            authRepo.saveUser(s.email, mockToken)

            _state.value = s.copy(
                isLoading = false,
                isSuccess = true,
                error = null
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}