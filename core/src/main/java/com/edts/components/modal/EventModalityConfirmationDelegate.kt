package com.edts.components.modal

interface EventModalityConfirmationDelegate {
    fun onConfirmClick(modality: EventModalityConfirmation)
    fun onCloseClick(modality: EventModalityConfirmation)
}