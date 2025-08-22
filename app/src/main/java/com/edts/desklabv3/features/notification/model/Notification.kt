package com.edts.desklabv3.features.notification.model

import com.edts.desklabv3.R
import com.edts.desklabv3.core.DatePattern
import com.edts.desklabv3.core.util.ActionType
import com.edts.desklabv3.core.util.Utils

data class Notification(
    val id: Int,
    val actionId: String,
    val detailId: String,
    val body: String?,
    val dateFrom: String?,
    val dateTo: String?,
    val week: Int?,
    val readFlag: Boolean?,
    val type: String?,
    val status: String?,
    val subtitle: String?,
    val title: String?,
    val notifDate: String?,
    val requestId: Int?,
    val approved: Boolean?,
) {
    var isApprover: Boolean? = null
    val notificationType =
        when {
            type.toString().contains("ACTIVITY") -> ActionType.Activities
            type.toString().contains("LEAVE") -> ActionType.Leave
            type.toString().contains("KERJA") -> ActionType.SpecialWork
            type.toString().contains("DELEGATE") -> ActionType.Delegate
            type.toString().contains("GENERAL") -> ActionType.GeneralEvent
            type.toString().contains("PEOPLE") -> ActionType.PeopleDevelopment
            type.toString().contains("EMPLOYEE") -> ActionType.EmployeeBenefit
            else -> null
        }

    val notificationIcon =
        when {
            type.toString().contains("ACTIVITY") -> com.edts.components.R.drawable.ic_notification_activities_circle
            type.toString().contains("LEAVE") -> com.edts.components.R.drawable.ic_notification_leave_circle
            type.toString().contains("KERJA") -> com.edts.components.R.drawable.ic_notification_special_work_circle
            type.toString().contains("DELEGATE") -> com.edts.components.R.drawable.ic_notification_delegate_circle
            type.toString().contains("GENERAL") -> com.edts.components.R.drawable.ic_notification_event_circle
            type.toString().contains("PEOPLE") -> com.edts.components.R.drawable.ic_notification_event_circle
            type.toString().contains("EMPLOYEE") -> com.edts.components.R.drawable.ic_notification_event_circle
            else -> null
        }

    fun isCancelled() = "CANCELLED" == status
    fun isSubmitted() = "SUBMITTED" == status
    fun isApproved() = "APPROVED" == status
    fun isRejected() = "REJECTED" == status
    fun isWaiting() = "WAITING" == status

    fun getNotificationDate() =
        if (dateFrom == null || dateTo == null) {
            null
        } else
            if (dateFrom == dateTo) {
                String.format(
                    "Untuk tanggal %s", Utils.convertFullFormatDate(dateFrom, false)
                )
            } else {
                String.format(
                    "Untuk tanggal %s - %s", Utils.convertFullFormatDate(dateFrom, false),
                    Utils.convertFullFormatDate(dateTo, false)
                )
            }

    fun getNotificationDateWithDay() =
        Utils.convertFullFormatDateToUTC7(notifDate, DatePattern.WithDayFullMonth)
}
