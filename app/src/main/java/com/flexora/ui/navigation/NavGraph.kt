package com.flexora.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.flexora.ui.screens.auth.*
import com.flexora.ui.screens.main.*
import com.flexora.ui.viewmodel.AuthViewModel
import com.flexora.ui.viewmodel.WorkoutViewModel

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object AddWorkout : Screen("add_workout")
    object History : Screen("history", "History", Icons.Default.DateRange)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onGuestClick = { navController.navigate(Screen.Dashboard.route) }
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { 
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    } 
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = { 
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    } 
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                workoutViewModel = workoutViewModel,
                onAddWorkoutClick = { navController.navigate(Screen.AddWorkout.route) }
            )
        }
        composable(route = Screen.AddWorkout.route) {
            AddWorkoutScreen(
                workoutViewModel = workoutViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.History.route) {
            HistoryScreen(
                workoutViewModel = workoutViewModel
            )
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Dashboard,
        Screen.History,
        Screen.Profile
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
