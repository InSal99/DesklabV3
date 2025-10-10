package com.edts.desklabv3.features.event.model

import java.util.UUID

sealed class FormField {
    abstract val id: UUID
    abstract val question: String
    abstract val description: String?
    abstract val isRequired: Boolean
    abstract val placeholder: String?
}

interface HasOptions {
    val options: List<String>
}

data class TextAreaFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
) : FormField()

data class NumberFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
) : FormField()

data class RadioFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
    override val options: List<String>
) : FormField(), HasOptions

data class CheckboxFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
    override val options: List<String>,
) : FormField(), HasOptions

data class DropdownFormField(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    override val placeholder: String? = null,
    override val options: List<String>,
) : FormField(), HasOptions