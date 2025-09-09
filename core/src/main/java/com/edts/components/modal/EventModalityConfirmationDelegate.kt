package com.edts.components.modal

/**
 * Delegate interface to handle click events from the CustomEventModalityConfirmation component.
 */
interface EventModalityConfirmationDelegate {
    /**
     * Called when the confirmation button is clicked.
     * @param modality The confirmation modality view that was clicked.
     */
    fun onConfirmClick(modality: EventModalityConfirmation)

    /**
     * Called when the close button is clicked.
     * @param modality The confirmation modality view that was clicked.
     */
    fun onCloseClick(modality: EventModalityConfirmation)
}