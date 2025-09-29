package com.edts.components.chip

interface ChipDelegate {
    fun onChipClick(chip: Chip, newState: Chip.ChipState)
    fun onChipIconClick(chip: Chip)
}