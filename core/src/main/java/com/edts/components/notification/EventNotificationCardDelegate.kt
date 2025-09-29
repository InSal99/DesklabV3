package com.edts.components.notification

interface EventNotificationCardDelegate {
    fun onButtonClick(notificationCard: EventNotificationCard)
    fun onCardClick(notificationCard: EventNotificationCard)
}