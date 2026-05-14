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
import me.jesusurbinez.miplatov.ui.components.MiPlatoButton
import androidx.compose.ui.graphics.Brush

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
                Text(text = "Tu plan personalizado", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = Color.White)
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
                        Text(text = "Nivel de actividad física", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
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
            MiPlatoCard(
                borderGradient = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Resumen de tu nuevo plan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Objetivo: ${uiState.estimatedKcal}", style = MaterialTheme.typography.bodySmall, color = me.jesusurbinez.miplatov.ui.theme.NeonYellow)
                    }
                    MiPlatoButton(
                        text = "Actualizar",
                        onClick = { viewModel.updateGlobalPlan() },
                        modifier = Modifier.width(150.dp).height(48.dp),
                        icon = Icons.Default.Sync
                    )
                }
            }
            
            Spacer(Modifier.height(100.dp)) // Padding for bottom bar
        }
    }
}

@Composable
fun GoalCard(title: String, kcal: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    MiPlatoCard(
        modifier = modifier.clickable { onClick() },
        borderGradient = if (isSelected) listOf(MaterialTheme.colorScheme.primary, Color.Transparent) else listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(56.dp), 
                shape = CircleShape, 
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(12.dp), tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(Modifier.height(8.dp))
            Text(text = "ESTIMADO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = kcal, style = MaterialTheme.typography.headlineSmall, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White, fontWeight = FontWeight.ExtraBold)
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
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected, 
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary, unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
