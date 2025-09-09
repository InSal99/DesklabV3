package com.edts.desklabv3.features.home.model

data class ActivityItem(
    val title: String,
    val desc: String,
    val haveSummary: Boolean,
    val type: ActivityType,
    val date: String
)
