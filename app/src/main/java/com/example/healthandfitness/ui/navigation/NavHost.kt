package com.example.healthandfitness.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.example.healthandfitness.ui.screens.MainScreen
import com.example.healthandfitness.ui.screens.PermissionsRequestScreen
import com.example.healthandfitness.ui.screens.PrivacyAndPolicyScreen
import com.example.healthandfitness.ui.screens.Screen
import com.example.healthandfitness.ui.screens.StepTrackerScreen
import com.example.healthandfitness.ui.screens.WelcomeScreen
import com.example.healthandfitness.utils.HealthConnectManager

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    healthConnectManager: HealthConnectManager,
) {
    val navGraph = remember(navHostController) {
        navHostController.createGraph(startDestination = Screen.WelcomeScreen.route) {
            composable(Screen.PermissionRequest.route) {
                PermissionsRequestScreen(navHostController = navHostController)
            }
            composable(Screen.WelcomeScreen.route) {
                val availability by healthConnectManager.availability
                
                WelcomeScreen(
                    healthConnectAvailability = availability,
                    onResumeAvailabilityCheck = {
                        healthConnectManager.checkAvailability()
                    }
                )
            }
            composable(Screen.PrivacyAndPolicyScreen.route) {
                PrivacyAndPolicyScreen()
            }
            composable(Screen.MainScreen.route) {
                MainScreen()
            }
            composable(Screen.StepTrackerScreen.route) {
                StepTrackerScreen()
            }
        }
    }
    NavHost(
        navController = navHostController,
        graph = navGraph,
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(top = 52.dp)
    )
}