package com.edts.components.input.field

data class InputFieldConfig(
    val type: InputFieldType,
    val title: String,
    val description: String? = null,
    val hint: String? = null,
    val isRequired: Boolean = false,
    val options: List<String>? = null,
    val maxLines: Int = 4,
    val minLines: Int = 3,
    val maxLength: Int = 0,
    val minLength: Int = 0,
    val supportingText: String? = null
)