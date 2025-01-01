package com.example.healthandfitness.ui.screens

const val UID_NAV_ARGUMENT = "uid"

enum class Screen(val route: String, val title: String) {
    WelcomeScreen("welcome", "Welcome Screen"),
    MainScreen("main", "Main Screen"),
    PermissionRequest("permission_request", "Permission Request"),
    PrivacyAndPolicyScreen(route = "privacy_and_policy", title = "Privacy and Policy"),
    SettingsScreen(route = "settings", title = "Settings"),
    StepTrackerScreen(route = "step_tracker", title = "Step Tracker"),
}