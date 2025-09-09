package com.edts.desklabv3.features.home.model

data class DateGroupItem(
    val date: String,
    val dayOfMonth: String,
    val monthName: String,
    val activities: List<ActivityItem>
)
