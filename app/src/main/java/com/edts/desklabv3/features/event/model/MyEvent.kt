package com.edts.desklabv3.features.event.model

import com.edts.components.event.card.EventCardBadge
import java.util.UUID

data class MyEvent(
    val id: String = UUID.randomUUID().toString(),
    val status: MyEventStatus,
    val date: String,
    val day: String,
    val month: String,
    val time: String,
    val title: String,
    val eventType: String,
    val badgeText: String,
    val badgeType: EventCardBadge.BadgeType,
    val isBadgeVisible: Boolean = true,
    val badgeSize: EventCardBadge.BadgeSize = EventCardBadge.BadgeSize.SMALL
)

