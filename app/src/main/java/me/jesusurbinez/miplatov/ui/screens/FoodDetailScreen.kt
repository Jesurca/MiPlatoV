package me.jesusurbinez.miplatov.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.StrokeCap
import me.jesusurbinez.miplatov.ui.components.MiPlatoButton
import me.jesusurbinez.miplatov.ui.components.MiPlatoCard
import me.jesusurbinez.miplatov.ui.theme.NeonYellow
import androidx.compose.ui.graphics.Brush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = { /* Minimalist: No Top Bar */ },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 0.dp), // Removed specific padding here
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.navigationBarsPadding().padding(horizontal = 20.dp).padding(bottom = 100.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiPlatoButton(
                        text = "Añadir a mi plato",
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.AddCircle
                    )
                    OutlinedIconButton(
                        onClick = {},
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Calendar", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-32).dp)
                    .padding(horizontal = 20.dp)
            ) {
                // Header Card
                MiPlatoCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                    shape = CircleShape,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = "Almuerzo Saludable",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Bowl de Quinoa y Salmón",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = "Porción: 350g (1 plato)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "485",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    lineHeight = 40.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "KCAL TOTAL",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // Macro Rings Bento
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MacroStatCard(label = "Proteínas", value = "32g", percentage = 0.4f, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.weight(1f))
                            MacroStatCard(label = "Carbos", value = "45g", percentage = 0.3f, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                            MacroStatCard(label = "Grasas", value = "18g", percentage = 0.3f, color = me.jesusurbinez.miplatov.ui.theme.NeonOrange, modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Micronutrients
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        MiPlatoCard(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Micronutrientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(Modifier.height(16.dp))
                                MicroProgress(label = "Fibra", value = "8.5g", percentage = 0.3f)
                                MicroProgress(label = "Azúcares", value = "2.1g", percentage = 0.1f)
                                MicroProgress(label = "Sodio", value = "240mg", percentage = 0.15f)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Health Insight
                MiPlatoCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderGradient = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), Color.Transparent)
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Insight Nutricional", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                "Este alimento es una excelente fuente de ácidos grasos Omega-3 y proteínas de alta calidad, lo que favorece la salud cardiovascular y la recuperación muscular.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(Modifier.height(100.dp)) // Padding for bottom bar
            }
        }
    }
}

@Composable
fun MacroStatCard(label: String, value: String, percentage: Float, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) {
                CircularProgressIndicator(
                    progress = { percentage },
                    color = color,
                    trackColor = color.copy(alpha = 0.15f),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round
                )
                Text("${(percentage * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun MicroProgress(label: String, value: String, percentage: Float) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        }
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            strokeCap = StrokeCap.Round
        )
    }
}
