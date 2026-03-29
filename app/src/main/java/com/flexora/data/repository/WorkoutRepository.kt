package com.flexora.data.repository

import com.flexora.data.local.dao.WorkoutDao
import com.flexora.data.local.entity.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    val allWorkouts: Flow<List<Workout>> = workoutDao.getAllWorkouts()
    val totalReps: Flow<Int?> = workoutDao.getTotalReps()

    suspend fun insert(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun delete(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }
    
    fun getWorkoutsForToday(startOfDay: Long): Flow<List<Workout>> {
        return workoutDao.getWorkoutsForToday(startOfDay)
    }
}
