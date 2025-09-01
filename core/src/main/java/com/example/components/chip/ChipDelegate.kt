package com.example.components.chip

interface ChipDelegate {
    fun onChipClick(chip: Chip, newState: Chip.ChipState)
}