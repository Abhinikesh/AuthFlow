package com.flexora.data.local.dao

import androidx.room.*
import com.flexora.data.local.entity.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT SUM(reps) FROM workouts")
    fun getTotalReps(): Flow<Int?>

    @Query("SELECT * FROM workouts WHERE date >= :startOfDay")
    fun getWorkoutsForToday(startOfDay: Long): Flow<List<Workout>>
}
