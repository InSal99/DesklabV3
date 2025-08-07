package com.example.desklabv3.features.home.model

import java.util.UUID

sealed class EventType {
    abstract val id: UUID
    abstract val name: String

    data class Online(
        override val id: UUID = UUID.randomUUID(),
        override val name: String,
        val meetingUrl: String,
        val platform: String
    ) : EventType()

    data class Offline(
        override val id: UUID = UUID.randomUUID(),
        override val name: String,
        val location: String,
        val room: String? = null
    ) : EventType()

    data class Hybrid(
        override val id: UUID = UUID.randomUUID(),
        override val name: String,
        val meetingUrl: String,
        val platform: String,
        val location: String,
        val room: String? = null
    ) : EventType()
}