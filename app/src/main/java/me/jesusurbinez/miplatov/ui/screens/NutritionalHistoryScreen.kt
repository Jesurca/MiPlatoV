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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionalHistoryScreen(
    viewModel: NutritionalHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { /* Minimalist: No Top Bar */ }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Historial de Consumo", style = MaterialTheme.typography.headlineMedium)
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }

            // Calendar Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 24.dp)) {
                items(uiState.days) { day ->
                    DayCard(day = day, isSelected = day.num == uiState.selectedDayNum)
                }
            }

            // Daily Summary Bento
            Row(modifier = Modifier.fillMaxWidth().height(100.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        val progress = if (uiState.targetCalories > 0) uiState.totalCalories.toFloat() / uiState.targetCalories else 0f
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                            CircularProgressIndicator(progress = { progress }, color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.secondaryContainer, strokeWidth = 6.dp)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${uiState.totalCalories}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                Text("kcal", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Calorías Diarias", style = MaterialTheme.typography.titleLarge)
                            Text("${(progress * 100).toInt()}% de tu meta", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
                
                val proteinProgress = if (uiState.targetProtein > 0) uiState.proteinGrams.toFloat() / uiState.targetProtein else 0f
                MacroMiniCard(label = "Proteína", value = "${uiState.proteinGrams}g", percentage = proteinProgress, icon = Icons.Default.FitnessCenter, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // Food List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                item { MealSectionHeader(title = "Consumo de hoy", time = "Detalles", icon = Icons.Default.Restaurant) }
                items(uiState.breakfastItems) { item -> FoodListItem(item) }
            }
        }
    }
}

@Composable
fun DayCard(day: DayInfo, isSelected: Boolean) {
    Surface(
        modifier = Modifier.width(56.dp).height(72.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = day.name, style = MaterialTheme.typography.labelLarge, color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.outline)
            Text(text = day.num, style = MaterialTheme.typography.titleLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
fun MacroMiniCard(label: String, value: String, percentage: Float, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
                Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Column {
                Text(value, style = MaterialTheme.typography.headlineMedium)
                LinearProgressIndicator(progress = { percentage }, modifier = Modifier.fillMaxWidth().height(4.dp).padding(top = 4.dp), color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.secondaryContainer, strokeCap = StrokeCap.Round)
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
            Text(title, style = MaterialTheme.typography.titleLarge)
        }
        Text(time, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun FoodListItem(item: FoodItem) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text("${item.kcal} kcal • ${item.protein}g Proteína", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
            }
            IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.outline) }
        }
    }
}
