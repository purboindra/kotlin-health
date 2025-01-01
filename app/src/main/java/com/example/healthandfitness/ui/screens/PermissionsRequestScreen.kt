package com.example.healthandfitness.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthandfitness.ui.component.PermissionRequestContent
import com.example.healthandfitness.ui.viewmodel.PermissionsViewModel
import com.example.healthandfitness.utils.HealthConnectManager


@Composable
fun PermissionsRequestScreen(
    permissionsViewModel: PermissionsViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val healthConnectManager = remember { HealthConnectManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    val permissionsGranted by permissionsViewModel.permissionsGranted.collectAsState()
    
    LaunchedEffect(Unit) {
        permissionsViewModel.checkPermissions(context)
    }
    
    if (permissionsGranted) Column  (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to ExerciseMate App", style = MaterialTheme.typography.titleLarge)
    } else PermissionRequestContent(
        coroutineScope,
        healthConnectManager,
        context
    )
    
    
}
