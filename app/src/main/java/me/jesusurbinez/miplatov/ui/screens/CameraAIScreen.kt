package me.jesusurbinez.miplatov.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.lifecycle.viewmodel.compose.viewModel
import me.jesusurbinez.miplatov.ui.viewmodels.CameraAIViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraAIScreen(
    onBack: () -> Unit,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: CameraAIViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Correct permission check
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(android.Manifest.permission.CAMERA)
        }
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }

    LaunchedEffect(hasCameraPermission, flashMode) {
        if (hasCameraPermission) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture.flashMode = flashMode

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(innerPadding)
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Permiso de cámara denegado", color = Color.White)
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

                // AI Tags simulation (only show if we have camera)
                if (hasCameraPermission) {
                    AITag(
                        label = "Detectando comida...",
                        confidence = "AI",
                        modifier = Modifier.align(Alignment.TopCenter).offset(y = 60.dp)
                    )
                }

                // Instruction
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp),
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = CircleShape
                ) {
                    Text(
                        text = if (hasCameraPermission) "Enfoca tu plato para analizarlo" else "Se requiere permiso de cámara",
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
                        if (hasCameraPermission) {
                            // Capture simulation - in a real app we would call imageCapture.takePicture
                            viewModel.captureFood()
                            onBack()
                        }
                    }
                ) {
                    Box(modifier = Modifier.padding(6.dp).fillMaxSize().background(Color.White, CircleShape))
                }

                IconButton(
                    onClick = {
                        flashMode = when (flashMode) {
                            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                            else -> ImageCapture.FLASH_MODE_OFF
                        }
                    },
                    modifier = Modifier.size(48.dp).background(
                        if (flashMode != ImageCapture.FLASH_MODE_OFF) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) 
                        else Color.White.copy(alpha = 0.2f), 
                        CircleShape
                    )
                ) {
                    Icon(
                        when (flashMode) {
                            ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
                            ImageCapture.FLASH_MODE_AUTO -> Icons.Default.FlashAuto
                            else -> Icons.Default.FlashOff
                        },
                        contentDescription = "Flash", 
                        tint = Color.White
                    )
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
