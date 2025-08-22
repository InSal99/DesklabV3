package com.edts.desklabv3.features.event.model

import java.util.UUID

data class EventForm(
    val id: UUID = UUID.randomUUID(),
    val fields: List<FormField>
)