package com.edts.components.dropdown.filter

/**
 * Delegate interface to handle click events for the CustomMonthlyPicker.
 */
interface MonthlyPickerDelegate {
    /**
     * Called when a CustomMonthlyPicker instance is clicked.
     * @param picker The view that was clicked.
     */
    fun onMonthClicked(picker: MonthlyPicker)
}