package com.example.metagames.ui.screens.luck

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class LuckProductUi(
    val id: String,
    val name: String,
    val category: String,
    val brand: String,
    val retailPrice: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckScreen() {
    val products = remember {
        listOf(
            LuckProductUi("1", "iPhone 15 Pro", "Celular", "Apple", "1.199.990"),
            LuckProductUi("2", "Galaxy S24 Ultra", "Celular", "Samsung", "1.099.990"),
            LuckProductUi("3", "iPad Air M2", "Tablet", "Apple", "799.990"),
            LuckProductUi("4", "Galaxy Tab S9", "Tablet", "Samsung", "699.990"),
            LuckProductUi("5", "MacBook Air M3", "Laptop", "Apple", "1.299.990"),
            LuckProductUi("6", "Dell XPS 13", "Laptop", "Dell", "1.149.990"),
        )
    }

    var mobileMenuOpen by remember { mutableStateOf(false) }
    var showSignupModal by remember { mutableStateOf(false) } // visual

    val bgHero = Brush.verticalGradient(
        colors = listOf(Color(0xFFF5F3FF), Color(0xFFEFF6FF))
    )

    Scaffold(
        topBar = {
            LuckTopBar(
                mobileMenuOpen = mobileMenuOpen,
                onToggleMobileMenu = { mobileMenuOpen = !mobileMenuOpen },
                onOpenLogin = { showSignupModal = true },
                onOpenSignup = { showSignupModal = true },
            )
        }
    ) { padding ->

        Box(Modifier.fillMaxSize().padding(padding)) {

            // ✅ TODO scrolleable con LazyColumn (sin crash)
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC)),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // HERO
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgHero)
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "Revela el precio oculto de productos tecnológicos premium.",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )

                            Text(
                                text = "Celulares, tablets y laptops de última generación.\n" +
                                        "Pagas 5500CLP para revelar el precio oculto. Ese monto se descuenta del precio final.\n" +
                                        "Cuando el precio llegue a $0, aparece “¡GANASTE!” y el producto es tuyo.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF334155)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { /* scroll real */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
                                            )
                                        )
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text("Ver productos", color = Color.White, fontWeight = FontWeight.SemiBold)
                                }

                                OutlinedButton(
                                    onClick = { /* scroll real */ },
                                    modifier = Modifier.height(44.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Ver cómo funciona", color = Color(0xFF6D28D9), fontWeight = FontWeight.Medium)
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            LuckExplainerCard()
                        }
                    }
                }

                // PRODUCTS header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                    ) {
                        Text(
                            text = "Elige tu producto",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Paga 5500 Pesos para revelar el precio oculto. Si el precio llega a 0 mientras revelas, ganas el producto.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF475569)
                        )
                        Spacer(Modifier.height(14.dp))
                    }
                }

                // ✅ “Grid” sin LazyVerticalGrid: filas de 2
                val rows = products.chunked(2)
                items(rows.size) { rowIndex ->
                    val row = rows[rowIndex]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LuckProductCard(
                            product = row[0],
                            onReveal = { showSignupModal = true },
                            modifier = Modifier.weight(1f)
                        )
                        if (row.size > 1) {
                            LuckProductCard(
                                product = row[1],
                                onReveal = { showSignupModal = true },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }

                // HOW IT WORKS
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 22.dp)
                    ) {
                        Text(
                            text = "Cómo funciona Mi Carta De La Suerte",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Es simple: pagas poco, revelas el precio y, con algo de suerte, te llevas el producto completo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF475569)
                        )

                        Spacer(Modifier.height(14.dp))

                        // En móvil: mejor en columna para que no se aplaste
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            StepCard(
                                number = "1",
                                title = "Elige y revela",
                                body = "Selecciona un producto y paga 5500CLP para revelar su precio oculto. Ese monto se descuenta del precio final."
                            )
                            StepCard(
                                number = "2",
                                title = "El precio baja",
                                body = "Con cada intento, el precio oculto disminuye. Otros usuarios también pueden estar revelando."
                            )
                            StepCard(
                                number = "3",
                                title = "Cuando llega a 0, ganas",
                                body = "Si eres quien lo baja a 0, aparece “¡GANASTE!” y te quedas con el producto."
                            )
                        }

                        Spacer(Modifier.height(18.dp))
                        CategoriesCard()
                    }
                }

                // FOOTER
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE2E8F0))
                            .background(Color(0xFFF8FAFC))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "© 2025 Mi Carta De La Suerte. Todos los derechos reservados.",
                            color = Color(0xFF64748B),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Mobile menu overlay (visual)
            if (mobileMenuOpen) {
                MobileMenuOverlay(
                    onDismiss = { mobileMenuOpen = false },
                    onOpenLogin = { showSignupModal = true; mobileMenuOpen = false },
                    onOpenSignup = { showSignupModal = true; mobileMenuOpen = false }
                )
            }

            // Modal Signup (visual)
            if (showSignupModal) {
                SignupModal(onClose = { showSignupModal = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LuckTopBar(
    mobileMenuOpen: Boolean,
    onToggleMobileMenu: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenSignup: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF7C3AED)),
                    contentAlignment = Alignment.Center
                ) { Text("🎁", color = Color.White) }

                Spacer(Modifier.width(10.dp))
            }
        },
        actions = {
            TextButton(onClick = { /* scroll */ }) { Text("Store") }
            TextButton(onClick = { /* scroll */ }) { Text("HOW") }
            TextButton(onClick = onOpenLogin) { Text("Login") }
            TextButton(onClick = onOpenSignup) { Text("Signup", color = Color(0xFF7C3AED)) }
            IconButton(onClick = onToggleMobileMenu) { Text(if (mobileMenuOpen) "✖" else "☰") }
        }
    )
}

@Composable
private fun LuckExplainerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cómo funciona el precio oculto",
                    color = Color(0xFF7C3AED),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFF3E8FF))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "5500 Pesos = 1 intento",
                        color = Color(0xFF6D28D9),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Productos reales, envíos automáticos, sin excusas, sin letra chica.\n" +
                            "Si lo ganaste, te lo llevas… siempre.\n\n" +
                            "“No vendemos suerte.\nVendemos finales felices.”",
                    color = Color(0xFFBBF7D0),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "A medida que más personas revelan el precio, este va bajando. Si eres quien lo lleva a 0, aparece “¡GANASTE!” y te quedas con el producto.",
                color = Color(0xFF64748B),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun LuckProductCard(
    product: LuckProductUi,
    onReveal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text("Imagen", color = Color(0xFF64748B))
            }

            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(product.category, color = Color(0xFF6D28D9), style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(product.brand, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
                }

                Column {
                    Text(product.name, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                    Text(
                        text = "Valor retail: $${product.retailPrice}",
                        color = Color(0xFF475569),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("XX.XX", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "El precio real se revelará después de pagar 5500CLP.",
                            color = Color(0xFF94A3B8),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                Button(
                    onClick = onReveal,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6)))
                        )
                ) {
                    Text("Revelar precio por 5500 Pesos", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun StepCard(number: String, title: String, body: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF7C3AED)),
                contentAlignment = Alignment.Center
            ) { Text(number, color = Color.White, fontWeight = FontWeight.Bold) }

            Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
            Text(body, color = Color(0xFF475569), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun CategoriesCard() {
    val grad = Brush.linearGradient(listOf(Color(0xFFF3E8FF), Color(0xFFEFF6FF)))
    Card(shape = RoundedCornerShape(20.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(grad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Categorías disponibles", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CategoryItem("Celulares", "iPhone, Samsung", "📱", Color(0xFFEDE9FE), Modifier.weight(1f))
                CategoryItem("Tablets", "iPad, Galaxy Tab", "📲", Color(0xFFDBEAFE), Modifier.weight(1f))
                CategoryItem("Laptops", "MacBook, Dell", "💻", Color(0xFFE0E7FF), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CategoryItem(title: String, subtitle: String, icon: String, bg: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bg),
                contentAlignment = Alignment.Center
            ) { Text(icon) }

            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                Text(subtitle, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun MobileMenuOverlay(
    onDismiss: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenSignup: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .padding(top = 56.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Productos") }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cómo funciona") }
                Divider()
                TextButton(onClick = onOpenLogin, modifier = Modifier.fillMaxWidth()) { Text("Iniciar sesión") }
                TextButton(onClick = onOpenSignup, modifier = Modifier.fillMaxWidth()) {
                    Text("Registrarse", color = Color(0xFF7C3AED))
                }
            }
        }
    }
}

@Composable
private fun SignupModal(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        val modalBg = Brush.linearGradient(
            listOf(Color(0xFF0F172A), Color(0xFF4C1D95), Color(0xFF0F172A))
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .background(modalBg)
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onClose) { Text("Cerrar ✖", color = Color.White.copy(alpha = 0.8f)) }
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔒", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    }

                    Text(
                        "Acceso Exclusivo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Crea tu cuenta para desbloquear beneficios ocultos",
                        color = Color(0xFFD8B4FE),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    BenefitRow("✅", "Ventajas multiplicadas", "Acceso a mecánicas especiales no reveladas")
                    BenefitRow("✅", "Historial encriptado", "Rastrea tus intentos y patrones ocultos")
                    BenefitRow("✅", "Notificaciones secretas", "Alertas sobre momentos críticos del precio")

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, Color(0xFFBFA6FF).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .background(Color(0xFF8B5CF6).copy(alpha = 0.12f))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "“Los que juegan sin cuenta... pierden más de lo que creen.”",
                            color = Color(0xFFE9D5FF),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = { /* visual */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6)))
                            )
                    ) { Text("Crear cuenta ahora", color = Color.White, fontWeight = FontWeight.SemiBold) }

                    Text(
                        "¿Ya tienes cuenta? Inicia sesión",
                        color = Color(0xFFD8B4FE),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun BenefitRow(icon: String, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0xFF8B5CF6).copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) { Text(icon, color = Color(0xFFD8B4FE)) }

        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, color = Color(0xFFD8B4FE), style = MaterialTheme.typography.labelSmall)
        }
    }
}
