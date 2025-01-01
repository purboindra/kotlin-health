package com.example.healthandfitness

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.healthandfitness.ui.navigation.AppNavHost
import com.example.healthandfitness.ui.theme.HealthAndFitnessTheme
import com.example.healthandfitness.utils.HealthConnectManager
import com.example.healthandfitness.utils.PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val providerPackageName = "com.google.android.apps.healthdata"
        val packageName = this.packageName
        
        val healthConnectManager = (application as MyApp).healthConnectManager
        
        fun showPermissionExplanation(onOpen: () -> Unit) {
            AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage("This app needs additional permissions to function properly. Please enable them in settings.")
                .setPositiveButton("Open Settings") { _, _ ->
                    onOpen()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        
        fun startActivityHealthConnect() {
            val uriString =
                "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
            this.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", packageName)
                }
            )
        }
        
        val availabilityStatus = HealthConnectClient.getSdkStatus(this, providerPackageName)
        
        // If user device not support Health Connect
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return
        }
        
        // If user device support Health Connect but not installed
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            startActivityHealthConnect()
            return
        }
        
        val healthConnectClient = HealthConnectClient.getOrCreate(this)
        
        val requestPermissionActivityContract =
            PermissionController.createRequestPermissionResultContract()
        
        val requestPermissions =
            registerForActivityResult(requestPermissionActivityContract) { granted ->
                println("granted: $granted")
                if (granted.containsAll(PERMISSIONS)) {
                    // Permissions successfully granted
                    println("Permissions successfully granted")
                } else {
                    // Lack of required permissions
                    println("Lack of required permissions")
                    showPermissionExplanation {
                        startActivityHealthConnect()
                    }
                }
            }
        
        suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            println("granted: $granted")
            if (granted.containsAll(PERMISSIONS)) {
                println("Permissions already granted")
                // Permissions already granted; proceed with inserting or reading data
            } else {
                println("Permissions not granted yet")
                healthConnectManager.openSettingsHealtConnect(this)
            }
        }
        
        lifecycleScope.launch {
            checkPermissionsAndRun(healthConnectClient)
        }
        
        setContent {
            HealthAndFitnessTheme {
                HealthAndFitnessApp(healthConnectManager)
            }
        }
    }
}

@Composable
fun MainAppContent(healthConnectManager: HealthConnectManager, navController: NavHostController) {
    AppNavHost(navController, healthConnectManager)
}