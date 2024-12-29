package com.example.healthandfitness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.example.healthandfitness.ui.screens.PermissionsRequestScreen

@Composable
fun AppNavHost(
    navHostController: NavHostController,
) {
    val navGraph = remember(navHostController) {
        navHostController.createGraph(startDestination = PermissionRationale) {
            composable<PermissionRationale> {
                PermissionsRequestScreen(navHostController = navHostController)
            }
        }
    }
    NavHost(navController = navHostController, graph = navGraph)
}