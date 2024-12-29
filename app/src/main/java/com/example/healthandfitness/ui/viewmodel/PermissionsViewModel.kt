package com.example.healthandfitness.ui.viewmodel

import android.content.Context
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthandfitness.utils.HealthConnectManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PermissionsViewModel @Inject constructor() : ViewModel() {
    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()
    
    fun setPermissionsGranted(granted: Boolean) {
        _permissionsGranted.value = granted
    }
    
    private fun healthConnectManager(context: Context) = HealthConnectManager(context)
    
    fun permissionsLauncher(context: Context): ActivityResultContract<Set<String>, Set<String>> {
        val healthConnectManager = healthConnectManager(context)
        return healthConnectManager.requestPermissionsActivityContext()
    }
    
     fun checkPermissions(context: Context) {
        val healthConnectManager = healthConnectManager(context)
        viewModelScope.launch {
            _permissionsGranted.value = healthConnectManager.hasAllPermissions()
        }
    }
}