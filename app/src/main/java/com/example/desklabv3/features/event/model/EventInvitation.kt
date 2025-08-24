package com.example.desklabv3.features.event.model

import com.example.components.notification.CustomNotificationCard

data class EventInvitation(
    val id: Int,
    val title: String,
    val description: String,
    val eventType: CustomNotificationCard.EventType,
    val buttonText: String = "Terima Undangan",
    val isButtonVisible: Boolean = true
)
