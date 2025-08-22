package com.edts.components.radiobutton

import android.view.View

interface CustomRadioButtonDelegate {
    fun onCheckChanged(view: View, isChecked: Boolean)
}