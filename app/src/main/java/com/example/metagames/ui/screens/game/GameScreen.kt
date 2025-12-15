package com.example.metagames.ui.screens.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metagames.viewmodel.game.*
import androidx.compose.ui.text.font.FontWeight


private val BgDark = Color(0xFF0F172A)
private val PanelDark = Color(0xFF020617)
private val Green = Color(0xFF22C55E)
private val Red = Color(0xFFEF4444)
private val Orange = Color(0xFFF59E0B)
private val Purple = Color(0xFF7C3AED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    vm: GameViewModel = viewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dino Run – Modo Práctica",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(BgDark, Color.Black)
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // SCORE
            Card(
                colors = CardDefaults.cardColors(containerColor = PanelDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Score: ${state.score}",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Orange,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(12.dp))

            // DIFICULTAD
            DifficultySelector(
                selected = state.difficulty,
                onSelect = { d: Difficulty -> vm.setDifficulty(d) }
            )

            Spacer(Modifier.height(16.dp))

            // GAME CANVAS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(listOf(Purple, Orange))
                )
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(PanelDark)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            if (!state.isRunning) vm.startGame()
                            else vm.jump()
                        }
                ) {
                    // FONDO
                    drawRect(Color(0xFF020617))

                    // DINO
                    drawCircle(
                        color = if (state.difficulty == Difficulty.EASY) Green else Red,
                        radius = 16f,
                        center = Offset(60f, state.dinoY)
                    )

                    // OBSTÁCULOS
                    state.obstacles.forEach {
                        drawRect(
                            color = Orange,
                            topLeft = Offset(it.x, 170f - it.height),
                            size = androidx.compose.ui.geometry.Size(it.width, it.height)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // CONTROLES
            if (!state.isRunning) {
                PrimaryAction(
                    text = "Iniciar Juego",
                    onClick = { vm.startGame() }
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryAction(
                        text = if (state.isPaused) "Reanudar" else "Pausar",
                        onClick = { vm.pause() }
                    )
                }
            }

            if (state.isGameOver) {
                Spacer(Modifier.height(20.dp))
                Text(
                    "GAME OVER",
                    color = Red,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        DifficultyChip(
            text = "Fácil",
            selected = selected == Difficulty.EASY,
            color = Green,
            onClick = { onSelect(Difficulty.EASY) }
        )

        DifficultyChip(
            text = "Difícil",
            selected = selected == Difficulty.HARD,
            color = Red,
            onClick = { onSelect(Difficulty.HARD) }
        )
    }
}

@Composable
private fun DifficultyChip(
    text: String,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(50),
        color = if (selected) color.copy(alpha = 0.15f) else PanelDark,
        border = BorderStroke(
            1.dp,
            if (selected) color else Color.DarkGray
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
            color = if (selected) color else Color.LightGray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PrimaryAction(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple
        )
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
