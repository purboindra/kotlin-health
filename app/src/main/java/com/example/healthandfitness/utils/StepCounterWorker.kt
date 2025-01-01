package com.example.healthandfitness.utils

import android.content.Context
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = " StepCounterWorker"
//
//@HiltWorker
//class StepCounterWorker @AssistedInject constructor(
//    @Assisted appContext: Context,
//    @Assisted workerParams: WorkerParameters,
//    val repository: Repository,
//    val stepCounter: StepCounter
//) : CoroutineWorker(appContext, workerParams) {
//
//    override suspend fun doWork(): Result {
//        Log.d(TAG, "Starting worker...")
//
//        val stepsSinceLastReboot = stepCounter.steps().first()
//        if (stepsSinceLastReboot == 0L) return Result.success()
//
//        Log.d(TAG, "Received steps from step sensor: $stepsSinceLastReboot")
//        repository.storeSteps(stepsSinceLastReboot)
//
//        Log.d(TAG, "Stopping worker...")
//        return Result.success()
//    }
//}