package com.edts.desklabv3.features.event.model

import com.edts.components.event.card.EventCardBadge
import com.edts.components.event.card.EventCardStatus
import java.util.Date

data class EventSample(
    val eventTitle: String,
    val eventImage: String,
    val eventCategory: EventCategory,
    val eventType: EventType,
    val eventDate: Date,
    val statusText: String? = null,
    val statusType: EventCardStatus.StatusType? = null,
    val badgeType: EventCardBadge.BadgeType? = null,
    val badgeText: String? = null
)