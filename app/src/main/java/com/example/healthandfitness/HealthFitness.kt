package com.example.healthandfitness

import android.app.Application
import com.example.healthandfitness.utils.HealthConnectManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp() : Application(){
    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}