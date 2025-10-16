package com.edts.desklabv3.features.event.model

import com.edts.components.notification.NotificationCard
import java.util.UUID

data class EventInvitation(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val primaryButtonText: String = "Terima Undangan",
    val secondaryButtonText: String = "Tolak",
    val isPrimaryButtonVisible: Boolean = true,
    val isSecondaryButtonVisible: Boolean = false,
    val isBadgeVisible: Boolean = false,
    val notificationCategory: NotificationCard.NotificationCategory
)