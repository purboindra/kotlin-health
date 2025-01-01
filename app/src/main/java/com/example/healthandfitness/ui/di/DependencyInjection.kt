package com.example.healthandfitness.ui.di

import android.content.Context
import androidx.room.Room
import com.example.healthandfitness.dao.AppDatabase
import com.example.healthandfitness.dao.StepsDao
import com.example.healthandfitness.repository.StepRepository
import com.example.healthandfitness.utils.HealthConnectManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HealthModule {
    
    @Provides
    fun provideHealtConnectManager(@ApplicationContext context: Context): HealthConnectManager {
        return HealthConnectManager(context)
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "step-tracker-db"
        ).fallbackToDestructiveMigration()
            .build()
    }
    
    
    @Provides
    fun provideStepsDao(appDatabase: AppDatabase): StepsDao {
        return appDatabase.stepsDao()
    }
    
    @Provides
    fun provideStepRepository(stepsDao: StepsDao): StepRepository {
        return StepRepository(stepsDao)
    }
}