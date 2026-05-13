package me.jesusurbinez.miplatov.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    Dashboard("dashboard", Icons.Default.Home, "Home"),
    FoodSearch("search", Icons.Default.Search, "Search"),
    CameraAI("camera", Icons.Default.PhotoCamera, "Camera"),
    FoodDetail("detail"),
    History("history", Icons.Default.History, "History"),
    Plans("plans"),
    Profile("profile", Icons.Default.Person, "Profile")
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

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    MiPlatoBottomBar(navController, currentRoute)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
        }
    }
}

@Composable
fun MiPlatoBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(Screen.Dashboard, Screen.FoodSearch, Screen.CameraAI, Screen.History, Screen.Profile)
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon!!, contentDescription = screen.label) },
                label = { Text(screen.label!!) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.outline,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}
