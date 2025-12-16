package com.example.metagames.viewmodel.game

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel() // ✅ constructor vacío
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have correct defaults`() {
        val state = viewModel.state.value
        assertEquals(0, state.score)
        assertFalse(state.isRunning)
        assertFalse(state.isPaused)
        assertFalse(state.isGameOver)
        assertEquals(Difficulty.HARD, state.difficulty)
        assertEquals(150f, state.dinoY)
        assertTrue(state.obstacles.isEmpty())
        assertEquals(3f, state.speed)
    }

    @Test
    fun `startGame should initialize game state`() = runTest {
        viewModel.startGame()

        // deja correr el dispatcher lo suficiente para que se procese el seteo inicial
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isRunning)
        assertFalse(state.isPaused)
        assertFalse(state.isGameOver)
        assertEquals(0, state.score)
        assertTrue(state.obstacles.isEmpty())
        assertEquals(150f, state.dinoY)
        assertEquals(3f, state.speed)
    }

    @Test
    fun `setDifficulty should update difficulty in state`() {
        viewModel.setDifficulty(Difficulty.EASY)
        assertEquals(Difficulty.EASY, viewModel.state.value.difficulty)
    }

    @Test
    fun `pause should toggle isPaused when game is running`() = runTest {
        viewModel.startGame()
        advanceUntilIdle()

        viewModel.pause()
        assertTrue(viewModel.state.value.isPaused)

        viewModel.pause()
        assertFalse(viewModel.state.value.isPaused)
    }

    @Test
    fun `pause should not work when game is not running`() {
        viewModel.pause()
        assertFalse(viewModel.state.value.isPaused)
    }

    @Test
    fun `jump should not work when game is not running`() = runTest {
        val initialY = viewModel.state.value.dinoY

        viewModel.jump()
        advanceUntilIdle()

        assertEquals(initialY, viewModel.state.value.dinoY)
    }

    @Test
    fun `difficulty can be changed before game starts`() {
        assertEquals(Difficulty.HARD, viewModel.state.value.difficulty)

        viewModel.setDifficulty(Difficulty.EASY)
        assertEquals(Difficulty.EASY, viewModel.state.value.difficulty)

        viewModel.setDifficulty(Difficulty.HARD)
        assertEquals(Difficulty.HARD, viewModel.state.value.difficulty)
    }
}
