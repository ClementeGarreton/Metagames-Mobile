package com.example.metagames.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metagames.viewmodel.home.HomeViewModel
import com.example.metagames.viewmodel.home.GameOption
import com.example.metagames.viewmodel.home.HowToStep

private val BgTop = Color(0xFF0B0F1A)
private val BgMid = Color(0xFF070A12)
private val CardBg = Color(0xFF0F172A)
private val CardStroke = Color(0x33F97316) // naranja suave
private val TextMuted = Color(0xFFB6C2D9)
private val Orange = Color(0xFFF97316)
private val Red = Color(0xFFEF4444)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel = viewModel(),
    onNavigate: (route: String) -> Unit = {} // "game", "winners", "luck"
) {
    val countdown by vm.countdown.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val bgBrush = remember {
        Brush.verticalGradient(listOf(BgTop, BgMid))
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            MetagamesTopBar(
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
        ) {

            item {
                HeroBlock(countdown = countdown)
                Spacer(Modifier.height(16.dp))
            }

            item {
                SectionHeader(
                    title = "Modos de Juego",
                    subtitle = "Elige tu modo. Practica gratis o compite por premios reales."
                )
                Spacer(Modifier.height(10.dp))
            }

            items(vm.gameOptions) { option ->
                GameOptionCardStyled(
                    option = option,
                    onClick = { link, isExternal ->
                        if (isExternal) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        } else {
                            val route = link.removePrefix("app://")
                            onNavigate(route)
                        }
                    }
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
                Spacer(Modifier.height(12.dp))
                DividerSoft()
                Spacer(Modifier.height(16.dp))

                SectionHeader(
                    title = "Cómo Jugar",
                    subtitle = "Simple y transparente. Paga, juega, gana (si tu puntaje lo merece)."
                )
                Spacer(Modifier.height(10.dp))
            }

            items(vm.howToPlay) { step ->
                HowToPlayRowStyled(step = step)
                Spacer(Modifier.height(10.dp))
            }

            item {
                Spacer(Modifier.height(12.dp))
                DividerSoft()
                Spacer(Modifier.height(16.dp))

                SectionHeader(title = "Sobre Nosotros")
                Spacer(Modifier.height(10.dp))

                InfoCard {
                    InfoParagraph("Aquí en Metagames puedes ganar dinero online jugando Dino Run haciendo el máximo puntaje a fin de mes.")
                    InfoParagraph("NO somos un casino online. Nuestra propuesta no es “azar”, es competencia con reglas claras.")
                    InfoParagraph("El premio millonario se paga el día 28 de cada mes y hay 2 modos de juego.")
                    InfoParagraph("Modo gratuito para practicar y modo premium con Mercado Pago y Power-Ups.")
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                DividerSoft()
                Spacer(Modifier.height(16.dp))

                SectionHeader(title = "Transparencia & Confianza")
                Spacer(Modifier.height(10.dp))

                InfoCard {
                    InfoParagraph("Publicamos la cantidad de dinero en juego en nuestro Instagram o en red Bitcoin con total transparencia y dominio público.")
                    InfoParagraph("Antes de entregar el premio, revisamos que el ganador no haya hecho trampa (puntaje modificado o juego sin pago).")
                    InfoParagraph("En modo premium ingresas tu email y se censura en la tabla de ganadores para proteger tu identidad.")
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                FooterBlock()
                Spacer(Modifier.height(22.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetagamesTopBar(
    onNavigate: (String) -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(listOf(Orange, Red))),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎮", color = Color.White)
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("METAGAMES", fontWeight = FontWeight.Black)
                    Text(
                        "Dino Run • Premios",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xCC000000),
            titleContentColor = Color.White
        ),
        actions = {
            TextButton(onClick = { onNavigate("game") }) { Text("Jugar", color = Color.White) }
            TextButton(onClick = { onNavigate("winners") }) { Text("Ganadores", color = Color.White) }
        }
    )
}

@Composable
private fun HeroBlock(countdown: String) {
    val heroBrush = Brush.verticalGradient(
        listOf(Color(0x331B2550), Color(0x11000000))
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardStroke, RoundedCornerShape(22.dp))
                .background(heroBrush)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Próximo premio",
                color = TextMuted,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Cuenta regresiva",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0B1226))
                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = countdown,
                    color = Orange,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GradientButton(
                    text = "Jugar ahora",
                    modifier = Modifier.weight(1f),
                    onClick = { /* solo visual */ }
                )
                OutlinedAction(
                    text = "Ver ganadores",
                    modifier = Modifier.weight(1f),
                    onClick = { /* solo visual */ }
                )
            }

            Text(
                text = "El sorteo se define por puntaje. Revisamos pagos y trampas para asegurar juego justo.",
                color = Color(0xFF93A4C7),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String? = null) {
    Column {
        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                subtitle,
                color = TextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameOptionCardStyled(
    option: GameOption,
    onClick: (link: String, isExternal: Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        onClick = { onClick(option.buttonLink, option.isExternal) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF111C3A))
                        .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        when {
                            option.title.contains("Desafio", ignoreCase = true) -> "🔥"
                            option.title.contains("Principiante", ignoreCase = true) -> "🟢"
                            option.title.contains("GRATIS", ignoreCase = true) -> "🆓"
                            option.title.contains("Ganadores", ignoreCase = true) -> "🏆"
                            option.title.contains("Instagram", ignoreCase = true) -> "📸"
                            option.title.contains("Suerte", ignoreCase = true) -> "🎁"
                            else -> "🎮"
                        },
                        color = Color.White
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        option.title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        option.description,
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            GradientButton(
                text = option.buttonText,
                onClick = { onClick(option.buttonLink, option.isExternal) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HowToPlayRowStyled(step: HowToStep) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(16.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF111C3A)),
                contentAlignment = Alignment.Center
            ) {
                Text(step.icon, style = MaterialTheme.typography.titleLarge)
            }
            Column(Modifier.weight(1f)) {
                Text(step.title, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(step.description, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun InfoParagraph(text: String) {
    Text(
        text = "• $text",
        color = TextMuted,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun DividerSoft() {
    Divider(color = Color(0x22FFFFFF))
}

@Composable
private fun FooterBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x99000000))
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(16.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Copyright 2025 • Metagames",
            color = Color(0xFF9FB0D4),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.horizontalGradient(listOf(Orange, Red)))
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun OutlinedAction(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(14.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(listOf(Orange, Red))
        )
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Medium)
    }
}
