package com.example.metagames.ui.screens.winners

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metagames.viewmodel.winners.SortOrder
import com.example.metagames.viewmodel.winners.WinnersViewModel

private val BgTop = Color(0xFF0B0F1A)
private val BgBottom = Color(0xFF05070E)
private val Panel = Color(0xFF0F172A)
private val RowCard = Color(0xFF0B1226)
private val Muted = Color(0xFFB6C2D9)
private val Orange = Color(0xFFF97316)
private val Red = Color(0xFFEF4444)
private val Purple = Color(0xFF7C3AED)
private val Gold = Color(0xFFFBBF24)
private val Silver = Color(0xFF9CA3AF)
private val Bronze = Color(0xFFFB923C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinnersScreen(
    vm: WinnersViewModel = viewModel()
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val bg = remember { Brush.verticalGradient(listOf(BgTop, BgBottom)) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Ganadores", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xCC000000),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
        ) {

            when {
                ui.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Orange)
                        Spacer(Modifier.height(12.dp))
                        Text("Cargando ganadores...", color = Muted)
                    }
                }

                ui.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            ui.error!!,
                            color = Red,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = vm::refresh,
                            colors = ButtonDefaults.buttonColors(containerColor = Purple),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Reintentar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            "Tabla de puntuaciones más altas con transparencia total",
                            color = Muted,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        // Search
                        OutlinedTextField(
                            value = ui.searchTerm,
                            onValueChange = vm::setSearchTerm,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Buscar por email...", color = Muted) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Muted)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange,
                                unfocusedBorderColor = Color(0x22FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Orange,
                                focusedContainerColor = Panel,
                                unfocusedContainerColor = Panel
                            ),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        // Sort row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Orden: Puntaje ${if (ui.sortOrder == SortOrder.DESC) "↓" else "↑"}",
                                color = Muted
                            )

                            AssistChip(
                                onClick = vm::toggleSort,
                                label = { Text("Cambiar", fontWeight = FontWeight.SemiBold) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF111827),
                                    labelColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Orange.copy(alpha = 0.45f))
                            )
                        }

                        // List container
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Panel),
                            border = BorderStroke(1.dp, Color(0x22FFFFFF))
                        ) {
                            if (ui.filtered.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(18.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (ui.searchTerm.isNotBlank()) "No se encontraron resultados" else "No hay ganadores aún",
                                        color = Muted
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize().padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    itemsIndexed(ui.filtered, key = { idx, w -> "${w.email}-$idx" }) { index, winner ->
                                        WinnerRowCard(
                                            index = index,
                                            rank = vm.rankIcon(index),
                                            email = vm.censorEmail(winner.email),
                                            score = winner.score.toString()
                                        )
                                    }
                                }
                            }
                        }

                        // Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCardPro(
                                title = "Total Jugadores",
                                value = ui.totalPlayers.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatCardPro(
                                title = "Puntaje Más Alto",
                                value = ui.topScore.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            StatCardPro(
                                title = "Promedio",
                                value = ui.avgScore.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WinnerRowCard(
    index: Int,
    rank: String,
    email: String,
    score: String
) {
    val accent = when (index) {
        0 -> Gold
        1 -> Silver
        2 -> Bronze
        else -> Orange
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RowCard),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rank,
                color = accent,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(56.dp)
            )

            Text(
                text = email,
                color = Color.White,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = score,
                color = accent,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun StatCardPro(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        border = BorderStroke(1.dp, Color(0x22FFFFFF))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                value,
                color = Orange,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )
            Text(
                title,
                color = Muted,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
