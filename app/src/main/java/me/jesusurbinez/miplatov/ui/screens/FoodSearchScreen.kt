package me.jesusurbinez.miplatov.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.viewmodels.FoodSearchViewModel
import me.jesusurbinez.miplatov.ui.viewmodels.AIResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onFoodClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onBack: () -> Unit,
    viewModel: FoodSearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(20.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { Text("Buscar alimentos...", color = MaterialTheme.colorScheme.outline) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.outline) },
            trailingIcon = {
                IconButton(onClick = onCameraClick) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Camera", tint = MaterialTheme.colorScheme.primary)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(Modifier.height(24.dp))

        // Suggested Chips
        Text(text = "Sugerencias", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.suggestions) { item ->
                SuggestionChip(label = item, onClick = { viewModel.onSearchQueryChange(item) })
            }
        }

        Spacer(Modifier.height(24.dp))

        // AI Results Section
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Resultados Recientes (IA)", style = MaterialTheme.typography.headlineMedium)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.aiResults) { result ->
                AICard(result, onClick = { 
                    viewModel.addFood(result)
                    onBack()
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionChip(label: String, onClick: () -> Unit) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = null
    )
}

@Composable
fun AICard(result: AIResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // AI Badge
                Surface(
                    modifier = Modifier.padding(8.dp).align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        Spacer(Modifier.width(4.dp))
                        Text("DETECTADO", style = MaterialTheme.typography.labelLarge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = result.title, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${result.protein}g P • ${result.carbs}g C • ${result.fat}g G",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = result.kcal.toString(), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    Text(text = "KCAL", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                result.tags.forEach { tag ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ) {
                        Text(
                            text = tag,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
