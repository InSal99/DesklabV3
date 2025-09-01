package com.edts.components.checkbox

interface CheckboxDelegate {
    fun onCheckChanged(checkBox: CheckBox, isChecked: Boolean)
}