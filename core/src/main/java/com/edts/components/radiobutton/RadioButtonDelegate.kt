package com.edts.components.radiobutton

import android.view.View

interface RadioButtonDelegate {
    fun onCheckChanged(view: View, isChecked: Boolean)
}