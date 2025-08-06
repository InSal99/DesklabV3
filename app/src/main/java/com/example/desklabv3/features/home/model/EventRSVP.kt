package com.example.desklabv3.features.home.model

import java.util.UUID

data class EventRSVP(
    val id: UUID = UUID.randomUUID(),
    val form: EventForm,
    val quota: Int
)

