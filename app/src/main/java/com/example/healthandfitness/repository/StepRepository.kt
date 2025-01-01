package com.example.healthandfitness.repository

import android.util.Log
import com.example.healthandfitness.dao.StepsDao
import com.example.healthandfitness.data.StepCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import javax.inject.Inject

private val TAG = "StepTrackerRepository"

class StepRepository @Inject constructor(
    private val stepsDao: StepsDao,
) {
    suspend fun storeSteps(stepsSinceLastReboot: Long) = withContext(Dispatchers.IO) {
        val currentMillis = Instant.now().toEpochMilli()
        
        val stepCount = StepCount(
            steps = stepsSinceLastReboot,
            createdAt = Instant.now().toString(),
            id = currentMillis.toInt(),
        )
        
        Log.d(TAG, "Storing steps: $stepCount")
        stepsDao.insertAll(stepCount)
    }
    
    suspend fun loadTodaySteps(): Long = withContext(Dispatchers.IO) {
//        printTheWholeStepsTable() // DEBUG
        
        val todayAtMidnight = (LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toString())
        val todayDataPoints = stepsDao.loadAllStepsFromToday(startDateTime = todayAtMidnight)
        when {
            todayDataPoints.isEmpty() -> 0
            else -> {
                val firstDataPointOfTheDay = todayDataPoints.first()
                val latestDataPointSoFar = todayDataPoints.last()
                
                val todaySteps = latestDataPointSoFar.steps - firstDataPointOfTheDay.steps
                Log.d(TAG, "Today Steps: $todaySteps")
                todaySteps
            }
        }
    }
}

