package com.example.healthandfitness.ui.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.healthandfitness.R
import com.example.healthandfitness.ui.theme.DarkGray
import com.example.healthandfitness.utils.HealthConnectManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PermissionRequestContent(
    coroutineScope: CoroutineScope,
    healthConnectManager: HealthConnectManager,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        
        Image(
            painter = painterResource(R.drawable.alarm_clock1),
            contentDescription = "Alarm Clock",
            modifier = Modifier.size(67.dp)
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            "Permission Required",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "For access full features of Health and Fitness App, please enable permissions. This app needs Health Connect permissions to track your fitness data.",
            style = MaterialTheme.typography.titleSmall.copy(
                color = DarkGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            coroutineScope.launch {
                healthConnectManager.openSettingsHealtConnect(context)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Grant Permissions")
        }
    }
}