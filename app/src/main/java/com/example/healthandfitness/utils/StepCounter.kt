package com.example.healthandfitness.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TAG = "StepTracker"

class StepTracker(private val context: Context) {
    
    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val stepCounterSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }
    
    private var initialSteps: Long? = null
    private var stepsDuringSession: Long = 0
    var simulatedSteps = 0L
    
    fun isStepCounterAvailable(): Boolean {
        return sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
    }
    
    suspend fun getStepsDuringSession() = suspendCancellableCoroutine { continuation ->
        Log.d(TAG, "Registering sensor listener... ")
        
        val listener: SensorEventListener by lazy {
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return
                    
                    Log.d(
                        TAG,
                        "Event sensor: ${event.sensor} -- ${event.values[0]} -- ${event.timestamp} -- ${event.accuracy}"
                    )
                    
                    simulatedSteps += 1
                    val stepsSinceLastReboot = event.values[0].toLong()
                    Log.d(
                        TAG,
                        "Steps since last reboot: $stepsSinceLastReboot (Simulated: $simulatedSteps)"
                    )
                    Log.d(
                        TAG,
                        "Continuation is ${continuation.isActive} -- ${continuation.isCancelled} -- ${continuation.isCompleted}"
                    )
                    Log.d(TAG, "Initial Steps: $initialSteps")
                    
                    if (initialSteps == null) {
                        initialSteps = stepsSinceLastReboot
                    }
                    
                    val stepsDuringSession = stepsSinceLastReboot - (initialSteps ?: 0L)
                    Log.d(TAG, "Steps during session: $stepsDuringSession")
                    
                    if (continuation.isActive) {
                        continuation.resume(stepsSinceLastReboot) {
                            Log.e(TAG, "Failed to resume continuation: $it")
                        }
                        sensorManager.unregisterListener(this)
                    }
                }
                
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    Log.d(TAG, "Accuracy changed to: $accuracy")
                }
            }
        }
        
        val supportedAndEnabled = sensorManager.registerListener(
            listener,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_UI
        )
        
        println("Supported and enabled: $supportedAndEnabled")
        
        if (!supportedAndEnabled) {
            Log.e(TAG, "Failed to register step counter listener")
            continuation.resumeWith(Result.failure(IllegalStateException("Failed to register step counter listener")))
        }
    }
    
}
