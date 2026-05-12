package me.jesusurbinez.miplatov.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.viewmodels.CameraAIViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraAIScreen(onBack: () -> Unit, viewModel: CameraAIViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Camera Preview Simulation
        Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))

        // UI Overlay
        Column(modifier = Modifier.fillMaxSize()) {
            // Scanning area
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Focus Frame
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp))
                ) {
                    // Scanning line animation simulation
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .offset(y = 100.dp),
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 2.dp
                    )
                }

                // AI Tags simulation
                AITag(
                    label = "Salmón Parrilla",
                    confidence = "94%",
                    modifier = Modifier.align(Alignment.TopCenter).offset(y = 60.dp, x = (-40).dp)
                )
                
                AITag(
                    label = "Tomate Cherry",
                    confidence = "88%",
                    isPrimary = false,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-60).dp, x = 40.dp)
                )

                // Instruction
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp),
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = CircleShape
                ) {
                    Text(
                        text = "Enfoca tu plato para analizarlo",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier.size(48.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                // Main Capture Button
                Surface(
                    modifier = Modifier.size(80.dp).border(4.dp, Color.White, CircleShape),
                    color = Color.Transparent,
                    shape = CircleShape,
                    onClick = {
                        viewModel.captureFood()
                        onBack()
                    }
                ) {
                    Box(modifier = Modifier.padding(6.dp).fillMaxSize().background(Color.White, CircleShape))
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = "Flash", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun AITag(label: String, confidence: String, modifier: Modifier = Modifier, isPrimary: Boolean = true) {
    Surface(
        modifier = modifier,
        color = if (isPrimary) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.9f),
        shape = CircleShape,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isPrimary) Icons.Default.Restaurant else Icons.Default.FiberManualRecord,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(8.dp))
            Surface(
                color = if (isPrimary) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            ) {
                Text(
                    text = confidence,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
