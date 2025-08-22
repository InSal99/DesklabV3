package com.edts.components.notification

/**
 * Delegate interface to handle click events from the CustomNotificationCard component.
 */
interface CustomNotificationCardDelegate {
    /**
     * Called when the button on the notification card is clicked.
     * @param notificationCard The card whose button was clicked.
     */
    fun onButtonClick(notificationCard: CustomNotificationCard)

    /**
     * Called when the entire notification card is clicked.
     * @param notificationCard The card that was clicked.
     */
    fun onCardClick(notificationCard: CustomNotificationCard)
}