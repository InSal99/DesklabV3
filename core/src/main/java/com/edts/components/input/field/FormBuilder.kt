package com.edts.components.input.field

import android.view.ViewGroup

class FormBuilder(private val container: ViewGroup) : InputFieldDelegate {

    private val fields = mutableMapOf<String, InputField>()
    private val fieldValidation = mutableMapOf<String, Boolean>()
    private var onFormValidationChanged: ((Boolean) -> Unit)? = null

    fun addField(id: String, config: InputFieldConfig): FormBuilder {
        val field = InputField(container.context).apply {
            configure(config, id)
            delegate = this@FormBuilder
        }

        fields[id] = field
        fieldValidation[id] = field.isValid()

        container.addView(field)
        notifyFormValidationChanged()

        return this
    }

    fun removeField(id: String): FormBuilder {
        fields[id]?.let { field ->
            container.removeView(field)
            fields.remove(id)
            fieldValidation.remove(id)
            notifyFormValidationChanged()
        }
        return this
    }

    fun getFieldValue(id: String): Any? = fields[id]?.getValue()

    fun setFieldValue(id: String, value: Any?) {
        fields[id]?.setValue(value)
    }

    fun setFieldError(id: String, error: String?) {
        fields[id]?.setError(error)
    }

    fun validateForm(): Boolean = fieldValidation.values.all { it }

    fun getFormData(): Map<String, Any?> {
        return fields.mapValues { (_, field) -> field.getValue() }
    }

    fun setOnFormValidationChanged(callback: (Boolean) -> Unit) {
        onFormValidationChanged = callback
    }

    override fun onValueChange(fieldId: String, value: Any?) {
        // Handle value changes if needed
    }

    override fun onValidationChange(fieldId: String, isValid: Boolean) {
        fieldValidation[fieldId] = isValid
        notifyFormValidationChanged()
    }

    private fun notifyFormValidationChanged() {
        onFormValidationChanged?.invoke(validateForm())
    }
}