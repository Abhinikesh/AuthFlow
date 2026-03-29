package com.flexora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.flexora.ui.screens.auth.*
import com.flexora.ui.screens.main.*
import com.flexora.ui.viewmodel.WorkoutViewModel

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object AddWorkout : Screen("add_workout")
    object History : Screen("history")
    object Profile : Screen("profile")
}

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
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
                onLoginSuccess = { navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                } },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                } },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                workoutViewModel = workoutViewModel,
                onAddWorkoutClick = { navController.navigate(Screen.AddWorkout.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
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
                workoutViewModel = workoutViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
