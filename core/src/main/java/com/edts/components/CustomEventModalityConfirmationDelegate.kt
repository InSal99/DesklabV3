package com.edts.components

/**
 * Delegate interface to handle click events from the CustomEventModalityConfirmation component.
 */
interface CustomEventModalityConfirmationDelegate {
    /**
     * Called when the confirmation button is clicked.
     * @param modality The confirmation modality view that was clicked.
     */
    fun onConfirmClick(modality: CustomEventModalityConfirmation)

    /**
     * Called when the close button is clicked.
     * @param modality The confirmation modality view that was clicked.
     */
    fun onCloseClick(modality: CustomEventModalityConfirmation)
}
