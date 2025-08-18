package com.example.components.checkbox

import android.view.View

interface CustomCheckboxDelegate {
    fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean)
}