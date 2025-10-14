package com.edts.desklabv3.features.event.model

import com.edts.components.notification.EventNotificationCard
import java.util.UUID

data class EventInvitation(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val buttonText: String = "Terima Undangan",
    val isButtonVisible: Boolean = true,
    val eventCategory: EventNotificationCard.EventCategory
)
