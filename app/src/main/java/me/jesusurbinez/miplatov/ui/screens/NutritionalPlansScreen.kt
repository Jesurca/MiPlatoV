package me.jesusurbinez.miplatov.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.jesusurbinez.miplatov.ui.components.MiPlatoCard

import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.viewmodels.NutritionalPlansViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionalPlansScreen(
    viewModel: NutritionalPlansViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { /* Minimalist: No Top Bar */ }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                Text(text = "Tu plan personalizado", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Ajusta tus metas para obtener recomendaciones nutricionales precisas.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Goals Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GoalCard(
                    title = "Perder peso",
                    kcal = "1,650 kcal",
                    icon = Icons.Default.TrendingDown,
                    isSelected = uiState.selectedGoal == "Perder peso",
                    onClick = { viewModel.onGoalSelected("Perder peso") },
                    modifier = Modifier.weight(1f)
                )
                GoalCard(
                    title = "Mantener",
                    kcal = "2,100 kcal",
                    icon = Icons.Default.Balance,
                    isSelected = uiState.selectedGoal == "Mantener",
                    onClick = { viewModel.onGoalSelected("Mantener") },
                    modifier = Modifier.weight(1f)
                )
            }
            GoalCard(
                title = "Ganar músculo",
                kcal = "2,450 kcal",
                icon = Icons.Default.FitnessCenter,
                isSelected = uiState.selectedGoal == "Ganar músculo",
                onClick = { viewModel.onGoalSelected("Ganar músculo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Activity Level Section
            MiPlatoCard {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Text(text = "Nivel de actividad física", style = MaterialTheme.typography.headlineMedium)
                    }
                    Spacer(Modifier.height(16.dp))
                    
                    ActivityOption(
                        label = "Sedentario",
                        description = "Poco o ningún ejercicio.",
                        isSelected = uiState.activityLevel == "Sedentario",
                        onSelect = { viewModel.onActivityLevelSelected("Sedentario") }
                    )
                    ActivityOption(
                        label = "Activo",
                        description = "Ejercicio moderado 3-5 días.",
                        isSelected = uiState.activityLevel == "Activo",
                        onSelect = { viewModel.onActivityLevelSelected("Activo") }
                    )
                    ActivityOption(
                        label = "Muy Activo",
                        description = "Deportista intenso.",
                        isSelected = uiState.activityLevel == "Muy Activo",
                        onSelect = { viewModel.onActivityLevelSelected("Muy Activo") }
                    )
                }
            }

            // Summary Footer
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Resumen de tu nuevo plan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Objetivo: ${uiState.estimatedKcal}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                    }
                    Button(
                        onClick = { viewModel.updateGlobalPlan() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}

@Composable
fun GoalCard(title: String, kcal: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceContainerLowest,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(12.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Spacer(Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(8.dp))
            Text(text = "ESTIMADO", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
            Text(text = kcal, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun ActivityOption(label: String, description: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelect() }
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(8.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.surfaceContainerLow else Color.Transparent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onSelect)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.titleLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
