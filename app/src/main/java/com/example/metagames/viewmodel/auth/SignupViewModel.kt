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

data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository(application)

    private val _state = MutableStateFlow(SignupUiState())
    val state: StateFlow<SignupUiState> = _state.asStateFlow()

    fun setEmail(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    fun setPassword(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    fun setConfirmPassword(value: String) {
        _state.value = _state.value.copy(confirmPassword = value, error = null)
    }

    fun signup() {
        val s = _state.value

        if (s.email.isBlank() || s.password.isBlank() || s.confirmPassword.isBlank()) {
            _state.value = s.copy(error = "Por favor completa todos los campos")
            return
        }

        if (!isValidEmail(s.email)) {
            _state.value = s.copy(error = "Email inválido")
            return
        }

        if (s.password.length < 6) {
            _state.value = s.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (s.password != s.confirmPassword) {
            _state.value = s.copy(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _state.value = s.copy(isLoading = true, error = null)

            val result = authRepo.signup(s.email, s.password)

            _state.value = result.fold(
                onSuccess = {
                    s.copy(isLoading = false, isSuccess = true)
                },
                onFailure = {
                    s.copy(isLoading = false, error = it.message)
                }
            )
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}