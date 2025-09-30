package com.edts.components.selection

interface ChipDelegate {
    fun onChipClick(chip: Chip, newState: Chip.ChipState)
    fun onChipIconClick(chip: Chip)
}