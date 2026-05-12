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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.graphics.StrokeCap
import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.viewmodels.ProfileViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { /* Minimalist: No Top Bar */ }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            Box(modifier = Modifier.padding(bottom = 32.dp)) {
                Surface(
                    modifier = Modifier.size(128.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shadowElevation = 4.dp
                ) {
                    // Placeholder for user photo
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    modifier = Modifier.align(Alignment.BottomEnd).size(36.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    shadowElevation = 2.dp
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(8.dp).size(16.dp), tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
            
            Text(text = uiState.userName, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(text = "Miembro desde ${uiState.memberSince}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(32.dp))

            // Stats Bento Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileStatCard(label = "Calorías", value = uiState.calories, icon = Icons.Default.LocalFireDepartment, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                ProfileStatCard(label = "Proteína", value = uiState.protein, icon = Icons.Default.FitnessCenter, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.weight(1f))
                ProfileStatCard(label = "Hidratación", value = uiState.hydration, icon = Icons.Default.WaterDrop, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(32.dp))

            // Options List
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column {
                    ProfileOption(label = "Datos personales", icon = Icons.Default.Person)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileOption(label = "Metas", icon = Icons.Default.Flag)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileOption(label = "Notificaciones", icon = Icons.Default.Notifications)
                }
            }

            Spacer(Modifier.height(32.dp))

            // Logout Button
            Button(
                onClick = { viewModel.logout(onLogout) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Cerrar Sesión", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun ProfileStatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = color)
                Surface(color = color.copy(alpha = 0.1f), shape = CircleShape) {
                    Text("META", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = color)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineLarge, color = color, fontSize = 22.sp)
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(progress = { 0.7f }, modifier = Modifier.fillMaxWidth().height(4.dp), color = color, trackColor = color.copy(alpha = 0.2f), strokeCap = StrokeCap.Round)
        }
    }
}

@Composable
fun ProfileOption(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.width(16.dp))
            Text(text = label, style = MaterialTheme.typography.titleLarge)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
    }
}
