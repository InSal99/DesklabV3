package com.example.desklabv3.features.event.model

import com.example.components.notification.EventNotificationCard

data class EventInvitation(
    val id: Int,
    val title: String,
    val description: String,
    val eventType: EventNotificationCard.EventType,
    val buttonText: String = "Terima Undangan",
    val isButtonVisible: Boolean = true
)
