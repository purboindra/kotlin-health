package com.example.healthandfitness.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrivacyAndPolicyScreen() {
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
            text = "Privacy Policy",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text("Privacy Policy Description", style = MaterialTheme.typography.titleMedium)
    }
    
}