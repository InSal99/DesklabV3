package com.example.desklabv3.features.event.model

/**
 * Represents the data for a single leave quota card.
 */
data class LeaveQuota(
    val title: String,
    val quota: Int,
    val expiredDate: String,
    val used: Int
)