package com.example.healthandfitness.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.feature.ExperimentalFeatureAvailabilityApi
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
import java.time.Instant
import java.time.ZonedDateTime
import kotlin.random.Random

val PERMISSIONS =
    setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

const val MIN_SUPPORTED_SDK = Build.VERSION_CODES.O_MR1

class HealthConnectManager(private val context: Context) {

    private val healthConnectClient: HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    var availability = mutableStateOf(HealthConnectAvailability.NOT_SUPPORTED)
        private set

    private fun isSupported() = Build.VERSION.SDK_INT >= MIN_SUPPORTED_SDK

    init {
        checkAvailability()
    }

    fun openSettingsHealtConnect(context: Context) {
        val settingsIntent = Intent()
        settingsIntent.action = HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
        context.startActivity(settingsIntent)
    }

    fun checkAvailability() {
        availability.value = when {
            HealthConnectClient.getSdkStatus(context) == SDK_AVAILABLE -> HealthConnectAvailability.INSTALLED
            isSupported() -> HealthConnectAvailability.NOT_INSTALLED
            else -> HealthConnectAvailability.NOT_SUPPORTED
        }
    }

    @OptIn(ExperimentalFeatureAvailabilityApi::class)
    fun isFeatureAvailable(feature: Int): Boolean {
        return healthConnectClient.features.getFeatureStatus(feature) == HealthConnectFeatures.FEATURE_STATUS_AVAILABLE
    }

    suspend fun hasAllPermissions(): Boolean =
        healthConnectClient.permissionController.getGrantedPermissions().containsAll(PERMISSIONS)

    fun requestPermissionsActivityContext(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    private fun buildHeartRateSeries(
        sessionStartTime: ZonedDateTime,
        sessionEndTime: ZonedDateTime,
    ): HeartRateRecord {
        val samples = mutableListOf<HeartRateRecord.Sample>()
        var time = sessionStartTime
        while (time.isBefore(sessionEndTime)) {
            samples.add(
                HeartRateRecord.Sample(
                    time = time.toInstant(),
                    beatsPerMinute = (80 + Random.nextInt(80)).toLong()
                )
            )
            time = time.plusSeconds(30)
        }
        return HeartRateRecord(
            startTime = sessionStartTime.toInstant(),
            startZoneOffset = sessionStartTime.offset,
            endTime = sessionEndTime.toInstant(),
            endZoneOffset = sessionEndTime.offset,
            samples = samples
        )
    }

    suspend fun writeExerciseSession(start: ZonedDateTime, end: ZonedDateTime) {
        healthConnectClient.insertRecords(
            listOf(
                ExerciseSessionRecord(
                    startTime = start.toInstant(),
                    startZoneOffset = start.offset,
                    endTime = end.toInstant(),
                    endZoneOffset = end.offset,
                    exerciseType = ExerciseSessionRecord.EXERCISE_TYPE_RUNNING,
                    title = "My Run #${Random.nextInt(0, 60)}"
                ),
                StepsRecord(
                    startTime = start.toInstant(),
                    startZoneOffset = start.offset,
                    endTime = end.toInstant(),
                    endZoneOffset = end.offset,
                    count = (1000 + 1000 * Random.nextInt(3)).toLong()
                ),
                TotalCaloriesBurnedRecord(
                    startTime = start.toInstant(),
                    startZoneOffset = start.offset,
                    endTime = end.toInstant(),
                    endZoneOffset = end.offset,
                    energy = Energy.calories((140 + Random.nextInt(20)) * 0.01)
                )
            ) + buildHeartRateSeries(start, end)
        )
    }

    suspend fun readStepsByTimeRange(
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )

            for (stepRecord in response.records) {
                println("stepRecord: $stepRecord")
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

}


enum class HealthConnectAvailability {
    INSTALLED,
    NOT_INSTALLED,
    NOT_SUPPORTED
}