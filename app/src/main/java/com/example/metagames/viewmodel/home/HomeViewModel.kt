package com.example.metagames.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

data class GameOption(
    val title: String,
    val description: String,
    val buttonText: String,
    val buttonLink: String,  // "app://game" o URL
    val isExternal: Boolean
)

data class HowToStep(
    val icon: String,        // emoji simple para no depender de icon packs
    val title: String,
    val description: String
)

class HomeViewModel : ViewModel() {

    private val _countdown = MutableStateFlow("")
    val countdown: StateFlow<String> = _countdown.asStateFlow()

    val gameOptions: List<GameOption> = listOf(
        GameOption(
            title = "Modo Desafío",
            description = "500 Pesos Chilenos cada partida. Los enemigos son aleatorios",
            buttonText = "Jugar en Difícil",
            buttonLink = "https://mpago.la/19BPFvn",
            isExternal = true
        ),
        GameOption(
            title = "Modo Principiante",
            description = "1000 Pesos la partida. La velocidad y enemigos son constantes.",
            buttonText = "Jugar en Fácil",
            buttonLink = "https://mpago.la/2kMbM58",
            isExternal = true
        ),
        GameOption(
            title = "JUEGA GRATIS",
            description = "Modo de práctica. Familiarízate antes de competir, entrena duro y gana.",
            buttonText = "JUEGA AQUÍ",
            buttonLink = "app://game",
            isExternal = false
        ),
        GameOption(
            title = "Sala de Ganadores",
            description = "Consulta los puntajes ganadores con total transparencia para ganar.",
            buttonText = "Ver",
            buttonLink = "app://winners",
            isExternal = false
        ),
        GameOption(
            title = "Nuestro Instagram",
            description = "No te pierdas noticias, concursos y el dinero en juego.",
            buttonText = "Seguir",
            buttonLink = "https://www.instagram.com/metagames.latam/",
            isExternal = true
        ),
        GameOption(
            title = "Mi Carta de la Suerte",
            description = "Paga por revelar el precio y gana productos premium.",
            buttonText = "Descubrir",
            buttonLink = "app://luck",
            isExternal = false
        )
    )

    val howToPlay: List<HowToStep> = listOf(
        HowToStep("💳", "Paga", "Haz clic en los logos de arriba para jugar."),
        HowToStep("🎮", "Juega", "Juega Dino Run. El puntaje más alto gana el premio cada mes."),
        HowToStep("🏆", "Gana", "Se compara tu puntaje con tu pago para asegurar un juego justo."),
        HowToStep("🔗", "Comparte", "Si quieres, comparte cómo ganaste en Metagames.")
    )

    init {
        startCountdownTicker()
        // Si después quieres limpiar “storage”, aquí llamarías a un repo/datastore.
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (true) {
                _countdown.value = calculateCountdownText()
                delay(1000)
            }
        }
    }

    /**
     * Lunes 8:00 AM → Domingo 8:00 PM
     */
    private fun calculateCountdownText(): String {
        val now = Calendar.getInstance()

        // Start: Lunes 8:00 AM (semana actual)
        val start = Calendar.getInstance().apply {
            val dayOfWeek = get(Calendar.DAY_OF_WEEK) // 1=Domingo..7=Sábado
            val mondayOffset = when (dayOfWeek) {
                Calendar.SUNDAY -> -6
                else -> Calendar.MONDAY - dayOfWeek
            }
            add(Calendar.DAY_OF_MONTH, mondayOffset)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // End: Domingo 8:00 PM
        val end = (start.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, 6)
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val nowMs = now.timeInMillis
        val startMs = start.timeInMillis
        val endMs = end.timeInMillis

        return when {
            nowMs < startMs -> {
                val diff = startMs - nowMs
                val h = TimeUnit.MILLISECONDS.toHours(diff)
                val m = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
                val s = TimeUnit.MILLISECONDS.toSeconds(diff) % 60
                "Comienza el sorteo en ${h}h ${m}m ${s}s"
            }
            nowMs in startMs..endMs -> {
                val diff = endMs - nowMs
                val d = TimeUnit.MILLISECONDS.toDays(diff)
                val h = TimeUnit.MILLISECONDS.toHours(diff) % 24
                val m = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
                val s = TimeUnit.MILLISECONDS.toSeconds(diff) % 60
                "${d}d ${h}h ${m}m ${s}s"
            }
            else -> "El sorteo está cerrado. ¡Vuelve mañana a las 8 AM!"
        }
    }
}
