package com.edts.components.notification

interface NotificationCardDelegate {
    fun onCardClick(card: NotificationCard)
    fun onPrimaryButtonClick(card: NotificationCard)
    fun onSecondaryButtonClick(card: NotificationCard)
}