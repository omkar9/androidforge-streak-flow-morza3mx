package com.androidforge.streakflow.core.common

objet Constants {
    const val DATABASE_NAME = "streak_flow_db"
    const val DATABASE_VERSION = 1
    const val NOTIFICATION_CHANNEL_ID = "streak_flow_habit_reminders"
    const val NOTIFICATION_ID_BASE = 1000 // Base ID for habit reminders

    const val PREFERENCES_NAME = "streak_flow_preferences"
    const val PREF_ONBOARDING_COMPLETED = "onboarding_completed"
    const val PREF_NOTIFICATIONS_ENABLED = "notifications_enabled"

    const val INTERSTITIAL_AD_INTERVAL_COUNT = 3 // Show interstitial ad every N actions

    const val PRIVACY_POLICY_URL = "https://www.example.com/privacy"
    const val TERMS_OF_SERVICE_URL = "https://www.example.com/terms"
}