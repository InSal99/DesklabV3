package com.example.components.input.field

sealed class InputFieldType {
    object TextInput : InputFieldType()
    object TextArea : InputFieldType()
    object Dropdown : InputFieldType()
    object RadioGroup : InputFieldType()
    object CheckboxGroup : InputFieldType()
}