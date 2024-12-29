package com.example.healthandfitness.ui.screens

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthandfitness.ui.viewmodel.PermissionsViewModel
import com.example.healthandfitness.utils.HealthConnectManager
import com.example.healthandfitness.utils.PERMISSIONS
import kotlinx.coroutines.launch


@Composable
fun PermissionsRequestScreen(
    permissionsViewModel: PermissionsViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val healthConnectManager = remember { HealthConnectManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    val permissionsGranted by permissionsViewModel.permissionsGranted.collectAsState()
    
    val permissionsLauncher =
        rememberLauncherForActivityResult(permissionsViewModel.permissionsLauncher(context)) {
            permissionsViewModel.checkPermissions(context)
        }
    
    LaunchedEffect(Unit) {
        if (permissionsGranted) {
            // TODO
        } else {
            permissionsLauncher.launch(PERMISSIONS)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This app needs Health Connect permissions to track your fitness data.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                val result = healthConnectManager.hasAllPermissions()
                if (result) {
                    permissionsViewModel.setPermissionsGranted(result)
                } else {
//                    onPermissionsDenied()
                    permissionsLauncher.launch(PERMISSIONS)
                }
            }
        }) {
            Text("Grant Permissions")
        }
        if (!permissionsGranted) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val intent =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        // HCM is an import alias for HealthConnectManager from the Health Connect client
//                            Intent(HCM.ACTION_MANAGE_HEALTH_PERMISSIONS)
//                                .putExtra(
//                                    Intent.EXTRA_PACKAGE_NAME,
//                                    context.packageName
//                                )
                    } else {
                        Intent(
                            HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
                        )
                    }
//                startActivity(context, intent, null)
            }) {
                Text("Give permissions to Health Connect")
            }
        }
    }
}
