package com.flexora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flexora.data.local.FlexoraDatabase
import com.flexora.data.repository.AuthRepository
import com.flexora.data.repository.WorkoutRepository
import com.flexora.ui.navigation.BottomNavigationBar
import com.flexora.ui.navigation.Screen
import com.flexora.ui.navigation.SetupNavGraph
import com.flexora.ui.theme.FlexoraTheme
import com.flexora.ui.viewmodel.AuthViewModel
import com.flexora.ui.viewmodel.AuthViewModelFactory
import com.flexora.ui.viewmodel.WorkoutViewModel
import com.flexora.ui.viewmodel.WorkoutViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Firebase
        val firebaseAuth = FirebaseAuth.getInstance()
        val authRepository = AuthRepository(firebaseAuth)
        val authViewModel = ViewModelProvider(this, AuthViewModelFactory(authRepository))[AuthViewModel::class.java]

        // Initialize Database and Repository
        val database = FlexoraDatabase.getDatabase(this)
        val workoutRepository = WorkoutRepository(database.workoutDao())
        val workoutViewModel = ViewModelProvider(this, WorkoutViewModelFactory(workoutRepository))[WorkoutViewModel::class.java]

        setContent {
            FlexoraTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                val user by authViewModel.user.collectAsState()
                
                // Routes that should NOT show the bottom bar
                val noBottomBarRoutes = listOf(
                    Screen.Welcome.route,
                    Screen.Login.route,
                    Screen.Register.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute !in noBottomBarRoutes) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { padding ->
                    SetupNavGraph(
                        navController = navController,
                        workoutViewModel = workoutViewModel,
                        authViewModel = authViewModel,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
