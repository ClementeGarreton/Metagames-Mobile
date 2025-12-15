package com.example.metagames.viewmodel.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class Difficulty { EASY, HARD }

data class Obstacle(
    val x: Float,
    val width: Float,
    val height: Float
)

data class GameUiState(
    val score: Int = 0,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false,
    val difficulty: Difficulty = Difficulty.HARD,
    val dinoY: Float = 150f,
    val obstacles: List<Obstacle> = emptyList(),
    val speed: Float = 3f
)

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    // Física del salto
    private var velocityY = 0f
    private val gravity = 0.55f
    private val jumpForce = -12.5f

    // Spawn controlado por tiempo (como tu React)
    private var lastObstacleTimeMs = 0L
    private var nextObstacleDelayMs = 2000L

    fun startGame() {
        val now = System.currentTimeMillis()
        lastObstacleTimeMs = now
        nextObstacleDelayMs = 2000L
        velocityY = 0f

        _state.value = GameUiState(
            isRunning = true,
            isPaused = false,
            isGameOver = false,
            score = 0,
            obstacles = emptyList(),
            speed = 3f,
            dinoY = 150f,
            difficulty = _state.value.difficulty
        )

        gameLoop()
    }

    fun jump() {
        val s = _state.value
        if (!s.isRunning || s.isPaused || s.isGameOver) return

        // "suelo" = 150f
        if (s.dinoY >= 150f) {
            velocityY = jumpForce
        }
    }

    fun pause() {
        val s = _state.value
        if (!s.isRunning || s.isGameOver) return
        _state.value = s.copy(isPaused = !s.isPaused)
    }

    fun setDifficulty(difficulty: Difficulty) {
        // Puedes permitir cambiarla en runtime.
        _state.value = _state.value.copy(difficulty = difficulty)
    }

    private fun gameLoop() {
        viewModelScope.launch {
            while (_state.value.isRunning && !_state.value.isGameOver) {
                val s = _state.value
                if (!s.isPaused) {
                    updatePhysics()
                    updateObstacles()
                    checkCollisions()
                }
                delay(16) // ~60 FPS
            }
        }
    }

    private fun updatePhysics() {
        val s = _state.value

        velocityY += gravity
        val newY = (s.dinoY + velocityY).coerceAtMost(150f)

        // Si tocó el suelo, resetea velocidad
        if (newY == 150f) velocityY = 0f

        _state.value = s.copy(dinoY = newY)
    }

    private fun updateObstacles() {
        val s = _state.value
        val speed = s.speed
        val now = System.currentTimeMillis()

        // 1) mover obstáculos
        val moved = s.obstacles
            .map { it.copy(x = it.x - speed) }
            .filter { it.x > -60f } // fuera de pantalla

        // 2) spawn por tiempo (fácil: 3s fijo / difícil: 2-4s random)
        val spawnEvery = if (s.difficulty == Difficulty.EASY) 3000L else nextObstacleDelayMs
        val timeSinceLast = now - lastObstacleTimeMs

        var newList = moved

        if (timeSinceLast >= spawnEvery) {
            // distancia mínima para que sea jugable
            val last = moved.lastOrNull()
            val hasDistance = last == null || (800f - last.x) > 180f

            if (hasDistance) {
                val height = Random.nextInt(30, 70).toFloat()
                newList = moved + Obstacle(
                    x = 800f,
                    width = 25f,
                    height = height
                )

                lastObstacleTimeMs = now

                // siguiente delay random SOLO en hard
                if (s.difficulty == Difficulty.HARD) {
                    nextObstacleDelayMs = listOf(2000L, 3000L, 4000L).random()
                }
            }
        }

        // 3) score: +1 por cada obstáculo que desapareció
        val passedCount = (s.obstacles.size - newList.size).coerceAtLeast(0)
        val newScore = s.score + passedCount

        _state.value = s.copy(
            obstacles = newList,
            score = newScore
        )
    }

    private fun checkCollisions() {
        val s = _state.value

        val groundY = 160f

        // Dino como caja (pero con padding para perdonar)
        val dinoX = 50f
        val dinoSize = 30f

        val padding = 6f

        val dinoLeft = dinoX + padding
        val dinoRight = dinoX + dinoSize - padding
        val dinoTop = s.dinoY + padding
        val dinoBottom = s.dinoY + dinoSize - padding

        for (obs in s.obstacles) {
            val obsLeft = obs.x + padding
            val obsRight = obs.x + obs.width - padding
            val obsTop = (groundY - obs.height) + padding
            val obsBottom = groundY - padding

            val hitX = dinoLeft < obsRight && dinoRight > obsLeft
            val hitY = dinoTop < obsBottom && dinoBottom > obsTop

            if (hitX && hitY) {
                _state.value = s.copy(isGameOver = true, isRunning = false)
                return
            }
        }
    }
}
