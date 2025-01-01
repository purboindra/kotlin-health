package com.example.healthandfitness.ui.screens

/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.healthandfitness.utils.HealthConnectAvailability

/**
 * Welcome screen shown when the app is first launched.
 */
@Composable
fun WelcomeScreen(
    healthConnectAvailability: HealthConnectAvailability,
    onResumeAvailabilityCheck: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val currentOnAvailabilityCheck by rememberUpdatedState(onResumeAvailabilityCheck)
    
DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnAvailabilityCheck()
            }
        }
        
        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)
        
        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Image(
//            modifier = Modifier.fillMaxWidth(0.5f),
//            painter = painterResource(id = R.drawable.ic_health_connect_logo),
//            contentDescription = stringResource(id = R.string.health_connect_logo)
//        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Welcome Screen",
            color = MaterialTheme.colorScheme.background
        )
        Spacer(modifier = Modifier.height(32.dp))
        when (healthConnectAvailability) {
            HealthConnectAvailability.INSTALLED -> Text("Installed")
            HealthConnectAvailability.NOT_INSTALLED -> Text("Not installed")
            HealthConnectAvailability.NOT_SUPPORTED -> Text("Not supported")
        }
    }
}
