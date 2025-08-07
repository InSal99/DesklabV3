package com.example.desklabv3.features.home.model

import java.util.UUID

data class EventForm(
    val id: UUID = UUID.randomUUID(),
    val fields: List<FormField>
)