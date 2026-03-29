package com.flexora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Double, // in kg
    val duration: Long, // in seconds
    val date: Long, // timestamp
    val userId: String? = null // For cloud sync later
)
