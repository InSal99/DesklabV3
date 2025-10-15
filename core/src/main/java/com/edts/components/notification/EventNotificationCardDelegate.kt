package com.edts.components.notification

interface EventNotificationCardDelegate {
    fun onCardClick(card: EventNotificationCard)
    fun onPrimaryButtonClick(card: EventNotificationCard)
    fun onSecondaryButtonClick(card: EventNotificationCard)
}