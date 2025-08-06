package com.example.desklabv3.features.home.model

import java.util.Date
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val image: String,
    val category: EventCategory,
    val type: EventType,
    val date: Date,
    val status: EventStatus,
    val description: String? = null,
    val needRsvp: Boolean,
    val rsvp: EventRSVP,
    val quota: Int,
    val attendanceType: String?
)

