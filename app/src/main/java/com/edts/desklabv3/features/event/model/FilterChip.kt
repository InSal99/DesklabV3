package com.edts.desklabv3.features.event.model

/**
 * Represents the state of a single filter chip in the RecyclerView.
 *
 * @param text The text displayed on the chip.
 * @param isSelected Whether the chip is currently in the active state.
 */
data class FilterChip(
    val text: String,
    var isSelected: Boolean = false
)