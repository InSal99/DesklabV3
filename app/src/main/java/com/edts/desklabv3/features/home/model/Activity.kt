package com.edts.desklabv3.features.home.model

data class Activity(
    val id: Int?,
    val date: String?,
    val title: String?,
    val type: String?,
    val code: String?,
    val divisionName: String?,
    val departmentName: String?
) {
    var isFirst: Boolean? = null
    fun isDiff(data: Activity) = date != data.date ||
            title != data.title || type != data.type

    override fun toString() = code!!
}