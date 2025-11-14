package com.edts.desklabv3.features.event.model

data class LeaveQuota(
    val title: String,
    val quota: String,
    val expiredDate: String,
    val used: String
)