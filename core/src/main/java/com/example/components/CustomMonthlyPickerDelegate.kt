package com.example.components

/**
 * Delegate interface to handle click events for the CustomMonthlyPicker.
 */
interface CustomMonthlyPickerDelegate {
    /**
     * Called when a CustomMonthlyPicker instance is clicked.
     * @param picker The view that was clicked.
     */
    fun onMonthClicked(picker: CustomMonthlyPicker)
}