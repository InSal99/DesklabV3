package com.edts.desklabv3.features.event.model

import java.util.UUID

sealed class FormField {
    abstract val id: UUID
    abstract val question: String
    abstract val description: String?
    abstract val isRequired: Boolean
    abstract val placeholder: String?
}

// Common properties for all fields that have options
interface HasOptions {
    val options: List<String>
}

// Text Area (multi-line)
data class TextAreaFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
) : FormField()

// Number Field
data class NumberFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
) : FormField()

// Radio Button (single selection)
data class RadioFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
    override val options: List<String>
) : FormField(), HasOptions

// Checkbox (multiple selection)
data class CheckboxFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
    override val options: List<String>,
) : FormField(), HasOptions

// Dropdown (single selection)
data class DropdownFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
    override val options: List<String>,
) : FormField(), HasOptions