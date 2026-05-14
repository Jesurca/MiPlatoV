package me.jesusurbinez.miplatov.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.jesusurbinez.miplatov.ui.screens.*
import me.jesusurbinez.miplatov.ui.theme.MiPlatoVTheme

enum class Screen(val route: String, val icon: ImageVector? = null, val label: String? = null) {
    Splash("splash"),
    Login("login"),
    Register("register"),
    Dashboard("dashboard", Icons.Rounded.Home, "Inicio"),
    FoodSearch("search", Icons.Rounded.Search, "Buscar"),
    CameraAI("camera", Icons.Rounded.PhotoCamera, "Cámara"),
    FoodDetail("detail"),
    History("history", Icons.Rounded.History, "Historial"),
    Plans("plans"),
    Profile("profile", Icons.Rounded.Person, "Perfil")
}

@Composable
fun MiPlatoApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    MiPlatoVTheme {
        val showBottomBar = currentRoute in listOf(
            Screen.Dashboard.route,
            Screen.FoodSearch.route,
            Screen.CameraAI.route,
            Screen.History.route,
            Screen.Profile.route
        )

        // Using Box to overlay the bottom bar and avoid Scaffold's default bottom bar container/shadows
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onNavigateToDashboard = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = { navController.navigate(Screen.Dashboard.route) },
                        onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        onRegisterSuccess = { navController.navigate(Screen.Dashboard.route) },
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                    )
                }
                composable(Screen.Dashboard.route) {
                    DashboardScreen()
                }
                composable(Screen.FoodSearch.route) {
                    FoodSearchScreen(
                        onFoodClick = { navController.navigate(Screen.FoodDetail.route) },
                        onCameraClick = { navController.navigate(Screen.CameraAI.route) },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.CameraAI.route) {
                    CameraAIScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.FoodDetail.route) {
                    FoodDetailScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.History.route) {
                    NutritionalHistoryScreen()
                }
                composable(Screen.Plans.route) {
                    NutritionalPlansScreen()
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }

            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    MiPlatoBottomBar(navController, currentRoute)
                }
            }
        }
    }
}

@Composable
fun MiPlatoBottomBar(navController: NavHostController, currentRoute: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            tonalElevation = 0.dp, // This often causes a "dark layer" tint in M3
            shadowElevation = 0.dp, // Remove shadow for a clean translucent look
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                )
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(Screen.Dashboard, Screen.FoodSearch, Screen.CameraAI, Screen.History, Screen.Profile)
                items.forEach { screen ->
                    val selected = currentRoute == screen.route
                    IconButton(
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = screen.icon!!,
                            contentDescription = screen.label,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = if (selected) Modifier.size(28.dp) else Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
