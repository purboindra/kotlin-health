package com.example.healthandfitness.ui.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.healthandfitness.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val activity = LocalContext.current
    
    ModalDrawerSheet {
        Column {
            Screen.entries.filter { it.route !== "welcome" }.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                if (item.route == "settings") {
                                    val settingsIntent = Intent()
                                    settingsIntent.action =
                                        HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
                                    activity.startActivity(settingsIntent)
                                } else {
                                    navController.navigate(item.route) {
                                        // See: https://developer.android.com/jetpack/compose/navigation#nav-to-composable
                                        navController.graph.startDestinationRoute?.let { route ->
                                            popUpTo(route) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                        .height(48.dp)
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable(
//                        onClick = {
//                            // TODO
//                        }
//                    )
//                    .height(48.dp)
//                    .padding(start = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Run",
//                    style = MaterialTheme.typography.labelMedium,
//                )
//            }
        }
    }
    
}