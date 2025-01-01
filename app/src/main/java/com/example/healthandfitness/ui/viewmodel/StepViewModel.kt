package com.example.healthandfitness.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthandfitness.repository.StepRepository
import com.example.healthandfitness.utils.HealthConnectManager
import com.example.healthandfitness.utils.StepTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(
    private val context: Application,
    private val healthConnectManager: HealthConnectManager,
    private val stepRepository: StepRepository,
) : ViewModel() {
    private val stepTracker = StepTracker(context)
    
    private val _stepState = MutableStateFlow<Long>(0)
    val stepState = _stepState.asStateFlow()
    
    private var sessionStartTime: Instant? = null
    private var sessionEndTime: Instant? = null
    
    fun stopSession() {
        sessionEndTime = Instant.now()
        
        viewModelScope.launch {
            try {
                val steps = stepTracker.getStepsDuringSession()
                _stepState.value = steps
                
                healthConnectManager.writeStepsRecord(
                    steps = steps,
                    startTime = sessionStartTime ?: Instant.now(),
                    endTime = sessionEndTime ?: Instant.now()
                )
                
            } catch (e: Exception) {
                println("Error stopSession: ${e.message}")
            }
        }
    }
    
    fun startStepTracking() {
        viewModelScope.launch {
            try {
                val steps = stepTracker.getStepsDuringSession()
                stepRepository.storeSteps(steps)
                println("Steps startStepTracking: $steps")
                _stepState.value = steps
            } catch (e: Exception) {
                println("Error startStepTracking: ${e.message}")
            }
        }
    }
    
    fun resetSteps() {
        _stepState.value = 0
    }
}