package com.flexora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.flexora.data.local.FlexoraDatabase
import com.flexora.data.repository.WorkoutRepository
import com.flexora.ui.navigation.SetupNavGraph
import com.flexora.ui.theme.FlexoraTheme
import com.flexora.ui.viewmodel.WorkoutViewModel
import com.flexora.ui.viewmodel.WorkoutViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Database and Repository
        val database = FlexoraDatabase.getDatabase(this)
        val repository = WorkoutRepository(database.workoutDao())
        val factory = WorkoutViewModelFactory(repository)
        val workoutViewModel = ViewModelProvider(this, factory)[WorkoutViewModel::class.java]

        setContent {
            FlexoraTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    workoutViewModel = workoutViewModel
                )
            }
        }
    }
}
