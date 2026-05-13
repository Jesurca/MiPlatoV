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

@Composable
fun DashboardScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: DashboardViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Circular Progress
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
                    val progress = if (viewModel.targetCalories > 0) 
                        (viewModel.consumedCalories.toFloat() / viewModel.targetCalories).coerceIn(0f, 1f)
                        else 0f
                        
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 12.dp,
                        trackColor = MaterialTheme.colorScheme.secondaryContainer,
                        strokeCap = StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = viewModel.consumedCalories.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "KCAL CONSUMIDAS",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "de ${viewModel.targetCalories}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SummaryStat(label = "Restantes", value = viewModel.remainingCalories.toString())
                    VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                    SummaryStat(label = "Objetivo", value = viewModel.targetCalories.toString())
                }
            }
        }

        // Macros Card
        MiPlatoCard {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(
                    text = "Macronutrientes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                MacroProgress(label = "Proteínas", currentValue = viewModel.proteinCurrent, targetValue = viewModel.proteinTarget)
                MacroProgress(label = "Carbohidratos", currentValue = viewModel.carbsCurrent, targetValue = viewModel.carbsTarget)
                MacroProgress(label = "Grasas", currentValue = viewModel.fatCurrent, targetValue = viewModel.fatTarget)
            }
        }

        // Today's Meals Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Comidas de hoy", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(Modifier.height(16.dp))

            if (viewModel.recentFoods.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No hay datos aún", style = MaterialTheme.typography.titleLarge)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    viewModel.recentFoods.forEach { food ->
                        FoodItemCard(food)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(food: me.jesusurbinez.miplatov.data.FoodRecord) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
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
                Text(text = food.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "${food.time} • ${food.calories} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${food.protein}g P • ${food.carbs}g C",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${food.fat}g G",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun SummaryStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    }
}
