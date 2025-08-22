package com.edts.components

/**
 * Delegate interface to handle click events from the CustomMyEventCard component.
 */
interface CustomMyEventCardDelegate {
    /**
     * Called when the CustomMyEventCard is clicked.
     * @param eventCard The card view that was clicked.
     */
    fun onClick(eventCard: CustomMyEventCard)
}
