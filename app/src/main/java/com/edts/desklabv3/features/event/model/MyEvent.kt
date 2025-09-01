package com.edts.desklabv3.features.event.model

import androidx.annotation.AttrRes

/**
 * Data class to hold all the information for a single MyEventCard.
 *
 * @param badgeBackgroundColor The theme attribute for the badge's background color (e.g., R.attr.colorBackgroundSuccessIntense).
 * @param badgeTextColor The theme attribute for the badge's text color (e.g., R.attr.colorForegroundPrimaryInverse).
 */
data class MyEvent(
    val badgeText: String,
    @AttrRes val badgeBackgroundColor: Int,
    @AttrRes val badgeTextColor: Int,
    val date: String,
    val day: String,
    val month: String,
    val time: String,
    val title: String,
    val eventType: String,
    val isBadgeVisible: Boolean = true
)