package com.example.healthandfitness.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthandfitness.ui.viewmodel.StepViewModel
import com.example.healthandfitness.utils.StepTracker


@Composable
fun StepTrackerScreen(stepViewModel: StepViewModel = hiltViewModel()) {
    
    val context = LocalContext.current
    
    val steps by stepViewModel.stepState.collectAsState()
    val stepTracker = StepTracker(context)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Steps: $steps",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = {
                if (!stepTracker.isStepCounterAvailable()) {
                    Toast.makeText(
                        context,
                        "Step Counter is not available on this device!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@Button
                }
                stepViewModel.startStepTracking()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Start Tracking")
        }
        if (steps > 0) {
            Button(
                onClick = {
                    stepViewModel.stopSession()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Stop Tracking")
            }
        }
    }
}
