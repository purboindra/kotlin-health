package com.example.healthandfitness.utils

import android.content.Context
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord

val PERMISSIONS =
    setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

class HealthConnectManager(private val context: Context) {
    
    private val healthConnectClient: HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }
    
    suspend fun hasAllPermissions(): Boolean =
        healthConnectClient.permissionController.getGrantedPermissions().containsAll(PERMISSIONS)
    
    fun requestPermissionsActivityContext(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }
}