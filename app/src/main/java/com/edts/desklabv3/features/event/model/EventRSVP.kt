package com.edts.desklabv3.features.event.model

import java.util.UUID

data class EventRSVP(
    val id: UUID = UUID.randomUUID(),
    val form: EventForm,
    val quota: Int
)

