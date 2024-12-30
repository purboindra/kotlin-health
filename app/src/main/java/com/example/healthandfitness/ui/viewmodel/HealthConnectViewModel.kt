package com.example.healthandfitness.ui.viewmodel

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthandfitness.utils.HealthConnectManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime
import javax.inject.Inject

class HealthConnectViewModel @Inject constructor(
    private val healthConnectClient: HealthConnectClient,
    private val healthConnectManager: HealthConnectManager,
) : ViewModel() {

    private val _exerciseData = MutableStateFlow<List<ExerciseSessionRecord>>(emptyList())
    val exerciseData = _exerciseData.asStateFlow()

    private val _stepRecords = MutableStateFlow<List<StepsRecord>>(emptyList())
    val stepRecords = _stepRecords.asStateFlow()

    fun writeExerciseSession(start: ZonedDateTime, end: ZonedDateTime) {
        viewModelScope.launch {
            healthConnectManager.writeExerciseSession(start, end)

        }
    }

    fun fetchSteps(startTime: Instant, endTime: Instant) {
        viewModelScope.launch {
            val steps = healthConnectManager.readStepsByTimeRange(startTime, endTime)
            _stepRecords.value = steps
        }
    }


}