package me.jesusurbinez.miplatov.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.graphics.StrokeCap
import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.viewmodels.NutritionalHistoryViewModel
import me.jesusurbinez.miplatov.ui.viewmodels.DayInfo
import me.jesusurbinez.miplatov.ui.viewmodels.FoodItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Brush
import me.jesusurbinez.miplatov.ui.components.MiPlatoCard
import me.jesusurbinez.miplatov.ui.theme.NeonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionalHistoryScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: NutritionalHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Historial de Consumo", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.ExtraBold)
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }

        // Calendar Row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 24.dp)) {
            items(uiState.days) { day ->
                DayCard(day = day, isSelected = day.num == uiState.selectedDayNum)
            }
        }

        // Daily Summary Bento
        Row(modifier = Modifier.fillMaxWidth().height(110.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiPlatoCard(
                modifier = Modifier.weight(2f),
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    val progress = if (uiState.targetCalories > 0) uiState.totalCalories.toFloat() / uiState.targetCalories else 0f
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                        CircularProgressIndicator(
                            progress = { progress }, 
                            color = MaterialTheme.colorScheme.primary, 
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), 
                            strokeWidth = 6.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${uiState.totalCalories}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("kcal", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Calorías Diarias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("${(progress * 100).toInt()}% de tu meta", style = MaterialTheme.typography.bodySmall, color = NeonYellow)
                    }
                }
            }

            val proteinProgress = if (uiState.targetProtein > 0) uiState.proteinGrams.toFloat() / uiState.targetProtein else 0f
            MacroMiniCard(label = "Proteína", value = "${uiState.proteinGrams}g", percentage = proteinProgress, icon = Icons.Default.FitnessCenter, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        // Food List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item { MealSectionHeader(title = "Consumo de hoy", time = "Detalles", icon = Icons.Default.Restaurant) }
            items(uiState.breakfastItems) { item -> FoodListItem(item) }
        }
    }
}

@Composable
fun DayCard(day: DayInfo, isSelected: Boolean) {
    Surface(
        modifier = Modifier.width(56.dp).height(72.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = day.name, style = MaterialTheme.typography.labelLarge, color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = day.num, style = MaterialTheme.typography.titleLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.White)
        }
    }
}

@Composable
fun MacroMiniCard(label: String, value: String, percentage: Float, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    MiPlatoCard(
        modifier = modifier.height(110.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.secondary)
            }
            Column {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                LinearProgressIndicator(
                    progress = { percentage }, 
                    modifier = Modifier.fillMaxWidth().height(4.dp).padding(top = 4.dp), 
                    color = MaterialTheme.colorScheme.secondary, 
                    trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), 
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
fun MealSectionHeader(title: String, time: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Text(time, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun FoodListItem(item: FoodItem) {
    MiPlatoCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${item.kcal} kcal • ${item.protein}g Proteína", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}
