package com.example.healthandfitness.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthandfitness.data.StepCount

@Database(entities = [StepCount::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stepsDao(): StepsDao
}