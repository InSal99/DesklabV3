package com.edts.components.checkbox

interface CustomCheckboxDelegate {
    fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean)
}