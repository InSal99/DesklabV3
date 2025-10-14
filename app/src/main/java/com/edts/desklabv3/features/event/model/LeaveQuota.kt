package com.edts.desklabv3.features.event.model

data class LeaveQuota(
    val title: String,
    val quota: Int,
    val expiredDate: String,
    val used: Int
)