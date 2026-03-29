package com.flexora.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flexora.data.local.entity.Workout
import com.flexora.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {

    val allWorkouts: Flow<List<Workout>> = repository.allWorkouts
    val totalReps: Flow<Int?> = repository.totalReps

    fun addWorkout(name: String, sets: Int, reps: Int, weight: Double, duration: Long) {
        viewModelScope.launch {
            val workout = Workout(
                exerciseName = name,
                sets = sets,
                reps = reps,
                weight = weight,
                duration = duration,
                date = System.currentTimeMillis()
            )
            repository.insert(workout)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.delete(workout)
        }
    }

    fun getWorkoutsForToday(): Flow<List<Workout>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return repository.getWorkoutsForToday(calendar.timeInMillis)
    }
}

class WorkoutViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
