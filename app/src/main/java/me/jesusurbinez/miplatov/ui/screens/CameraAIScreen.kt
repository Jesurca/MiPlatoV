package me.jesusurbinez.miplatov.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import me.jesusurbinez.miplatov.data.ScannedFood
import me.jesusurbinez.miplatov.ui.components.MiPlatoButton
import me.jesusurbinez.miplatov.ui.components.MiPlatoCard
import me.jesusurbinez.miplatov.ui.viewmodels.CameraAIViewModel
import me.jesusurbinez.miplatov.ui.viewmodels.CameraUIState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraAIScreen(
    onBack: () -> Unit,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: CameraAIViewModel = viewModel()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val imageCapture = remember { ImageCapture.Builder().build() }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(innerPadding)
    ) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(imageCapture = imageCapture)
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Permiso de cámara no concedido", color = Color.White)
            }
        }

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

                // AI Tags simulation (only if idle)
                if (uiState is CameraUIState.Idle) {
                    AITag(
                        label = "Salmón Parrilla",
                        confidence = "94%",
                        modifier = Modifier.align(Alignment.TopCenter).offset(y = 80.dp, x = (-40).dp)
                    )
                    
                    AITag(
                        label = "Tomate Cherry",
                        confidence = "88%",
                        isPrimary = false,
                        modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-180).dp, x = 40.dp)
                    )
                }

                // Instruction / Result
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp),
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = CircleShape
                ) {
                    Text(
                        text = when (uiState) {
                            is CameraUIState.Idle -> "Enfoca tu plato para analizarlo"
                            is CameraUIState.Loading -> "Analizando plato..."
                            is CameraUIState.Success -> "¡Plato analizado!"
                            is CameraUIState.Error -> (uiState as CameraUIState.Error).message
                        },
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Result Card
                val food = (uiState as? CameraUIState.Success)?.food
                androidx.compose.animation.AnimatedVisibility(
                    visible = food != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    if (food != null) {
                        ResultCard(
                            food = food,
                            onConfirm = {
                                viewModel.confirmFood(food)
                                onBack()
                            },
                            onCancel = { viewModel.resetState() }
                        )
                    }
                }

                if (uiState is CameraUIState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            // Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 140.dp), // Increased padding to clear the new floating bottom bar
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier.size(48.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                // Main Capture Button
                Surface(
                    modifier = Modifier.size(80.dp).border(4.dp, Color.White, CircleShape),
                    color = Color.Transparent,
                    shape = CircleShape,
                    enabled = uiState is CameraUIState.Idle,
                    onClick = {
                        takePicture(imageCapture, context) { bitmap ->
                            viewModel.scanImage(bitmap)
                        }
                    }
                ) {
                    Box(modifier = Modifier.padding(6.dp).fillMaxSize().background(
                        if (uiState is CameraUIState.Idle) Color.White else Color.Gray, 
                        CircleShape
                    ))
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Rounded.FlashOn, contentDescription = "Flash", tint = Color.White)
                }
            }
        }
    }
}

private fun takePicture(
    imageCapture: ImageCapture,
    context: android.content.Context,
    onImageCaptured: (Bitmap) -> Unit
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                image.close()
                onImageCaptured(bitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraAIScreen", "Capture failed", exception)
            }
        }
    )
}

@Composable
fun ResultCard(food: ScannedFood, onConfirm: () -> Unit, onCancel: () -> Unit) {
    MiPlatoCard(
        modifier = Modifier.padding(16.dp).width(300.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(food.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text("${food.calories} kcal", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
            
            Spacer(Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                MacroMiniItem("Prot", "${food.protein}g")
                MacroMiniItem("Carbs", "${food.carbs}g")
                MacroMiniItem("Grasas", "${food.fat}g")
            }

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Cancelar", color = Color.White)
                }
                MiPlatoButton(
                    text = "Añadir",
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f).height(48.dp),
                    icon = Icons.Rounded.Check
                )
            }
        }
    }
}

@Composable
fun MacroMiniItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun CameraPreview(imageCapture: ImageCapture) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (ex: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", ex)
                }
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
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
