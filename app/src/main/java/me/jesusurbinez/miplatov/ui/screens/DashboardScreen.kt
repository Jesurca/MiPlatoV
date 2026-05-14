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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.components.MacroProgress
import me.jesusurbinez.miplatov.ui.components.MiPlatoCard
import me.jesusurbinez.miplatov.ui.viewmodels.DashboardViewModel

import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow

@Composable
fun DashboardScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: DashboardViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Daily Summary Card
        MiPlatoCard {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Resumen Diario",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Circular Progress with Neo Glow
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
                    val progress = if (viewModel.targetCalories > 0) 
                        (viewModel.consumedCalories.toFloat() / viewModel.targetCalories).coerceIn(0f, 1f)
                        else 0f
                    
                    val primaryColor = MaterialTheme.colorScheme.primary

                    // Neon Glow Effect (Replaces shadow to avoid octagon artifact)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            primaryColor.copy(alpha = 0.3f),
                                            Color.Transparent
                                        ),
                                        center = center,
                                        radius = size.minDimension / 1.1f
                                    ),
                                    radius = size.minDimension / 1.1f
                                )
                            }
                    )
                        
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        color = primaryColor,
                        strokeWidth = 14.dp,
                        trackColor = primaryColor.copy(alpha = 0.15f),
                        strokeCap = StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = viewModel.consumedCalories.toString(),
                            style = MaterialTheme.typography.displayLarge.copy(
                                shadow = Shadow(
                                    color = primaryColor,
                                    blurRadius = 15f
                                )
                            ),
                            color = primaryColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "KCAL CONSUMIDAS",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 2.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "de ${viewModel.targetCalories}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SummaryStat(label = "Restantes", value = viewModel.remainingCalories.toString(), color = me.jesusurbinez.miplatov.ui.theme.NeonGreen)
                    VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    SummaryStat(label = "Objetivo", value = viewModel.targetCalories.toString(), color = me.jesusurbinez.miplatov.ui.theme.NeonYellow)
                }
            }
        }

        // Macros Card
        MiPlatoCard(borderGradient = listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), Color.Transparent)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(
                    text = "Macronutrientes",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                MacroProgress(label = "Proteínas", currentValue = viewModel.proteinCurrent, targetValue = viewModel.proteinTarget, color = MaterialTheme.colorScheme.tertiary)
                MacroProgress(label = "Carbohidratos", currentValue = viewModel.carbsCurrent, targetValue = viewModel.carbsTarget, color = MaterialTheme.colorScheme.secondary)
                MacroProgress(label = "Grasas", currentValue = viewModel.fatCurrent, targetValue = viewModel.fatTarget, color = me.jesusurbinez.miplatov.ui.theme.NeonOrange)
            }
        }

        // Today's Meals Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Comidas de hoy", 
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(16.dp))

            if (viewModel.recentFoods.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(16.dp))
                        Text(text = "No hay datos aún", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    viewModel.recentFoods.forEach { food ->
                        FoodItemCard(food)
                    }
                }
            }
            
            Spacer(Modifier.height(100.dp)) // Extra space for floating bottom bar
        }
    }
}

@Composable
fun FoodItemCard(food: me.jesusurbinez.miplatov.data.FoodRecord) {
    MiPlatoCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = food.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    text = "${food.time} • ${food.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Row {
                    Text(
                        text = "${food.protein}g P",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${food.carbs}g C",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${food.fat}g G",
                    style = MaterialTheme.typography.labelSmall,
                    color = me.jesusurbinez.miplatov.ui.theme.NeonOrange,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SummaryStat(label: String, value: String, color: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value, 
            style = MaterialTheme.typography.headlineMedium, 
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}
