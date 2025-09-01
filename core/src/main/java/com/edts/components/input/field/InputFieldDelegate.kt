package com.edts.components.input.field

interface InputFieldDelegate {
    fun onValueChange(fieldId: String, value: Any?)
    fun onValidationChange(fieldId: String, isValid: Boolean)
}