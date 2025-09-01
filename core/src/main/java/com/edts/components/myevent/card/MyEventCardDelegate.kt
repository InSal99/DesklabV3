package com.edts.components.myevent.card

import com.edts.components.myevent.card.MyEventCard

/**
 * Delegate interface to handle click events from the CustomMyEventCard component.
 */
interface MyEventCardDelegate {
    /**
     * Called when the CustomMyEventCard is clicked.
     * @param eventCard The card view that was clicked.
     */
    fun onClick(eventCard: MyEventCard)
}
