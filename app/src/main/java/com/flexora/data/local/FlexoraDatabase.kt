package com.flexora.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flexora.data.local.dao.WorkoutDao
import com.flexora.data.local.entity.Workout

@Database(entities = [Workout::class], version = 1, exportSchema = false)
abstract class FlexoraDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: FlexoraDatabase? = null

        fun getDatabase(context: Context): FlexoraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlexoraDatabase::class.java,
                    "flexora_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
