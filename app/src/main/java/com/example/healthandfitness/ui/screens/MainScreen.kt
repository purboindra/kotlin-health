package com.example.healthandfitness.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.safeContentPadding()) { paddingValues ->
        Text("Main Screen", modifier = Modifier.padding(paddingValues))
    }
}