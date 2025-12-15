package com.example.metagames.viewmodel.winners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metagames.data.api.ApiClient
import com.example.metagames.data.repository.Winner
import com.example.metagames.data.repository.WinnersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class SortOrder { ASC, DESC }

data class WinnersUiState(
    val winners: List<Winner> = emptyList(),
    val searchTerm: String = "",
    val sortOrder: SortOrder = SortOrder.DESC,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val filtered: List<Winner>
        get() {
            val q = searchTerm.trim().lowercase()
            val base = if (q.isEmpty()) winners else winners.filter { it.email.lowercase().contains(q) }
            return if (sortOrder == SortOrder.DESC) base.sortedByDescending { it.score } else base.sortedBy { it.score }
        }

    val totalPlayers: Int get() = filtered.size
    val topScore: Int get() = filtered.maxOfOrNull { it.score } ?: 0
    val avgScore: Int
        get() = if (filtered.isEmpty()) 0 else (filtered.sumOf { it.score }.toFloat() / filtered.size).roundToInt()
}

class WinnersViewModel(
    private val repo: WinnersRepository = WinnersRepository(ApiClient.winnersApi)
) : ViewModel() {

    private val _ui = MutableStateFlow(WinnersUiState(isLoading = true))
    val ui: StateFlow<WinnersUiState> = _ui.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, error = null)
            runCatching { repo.fetchWinners() }
                .onSuccess { list ->
                    _ui.value = _ui.value.copy(winners = list, isLoading = false, error = null)
                }
                .onFailure {
                    _ui.value = _ui.value.copy(
                        isLoading = false,
                        error = "Error al cargar los ganadores. Por favor, intenta de nuevo."
                    )
                }
        }
    }

    fun setSearchTerm(value: String) {
        _ui.value = _ui.value.copy(searchTerm = value)
    }

    fun toggleSort() {
        val next = if (_ui.value.sortOrder == SortOrder.DESC) SortOrder.ASC else SortOrder.DESC
        _ui.value = _ui.value.copy(sortOrder = next)
    }

    fun censorEmail(email: String): String {
        val length = email.length
        if (length <= 6) return email
        return email.take(3) + "..." + email.takeLast(3)
    }

    fun rankIcon(index: Int): String = when (index) {
        0 -> "🥇"
        1 -> "🥈"
        2 -> "🥉"
        else -> "#${index + 1}"
    }
}
