package com.edts.desklabv3.features.event.ui.rsvp

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldDelegate
import com.edts.components.input.field.InputFieldType

class RSVPFormAdapter : RecyclerView.Adapter<RSVPFormAdapter.ViewHolder>() {
    private val formConfigs = mutableListOf<InputFieldConfig>()
    private val responses = mutableMapOf<String, Any?>()
    private var recyclerView: RecyclerView? = null

    var onResponseChange: ((Map<String, Any?>) -> Unit)? = null
    var onValidationChange: ((Boolean) -> Unit)? = null
    var onFieldFocused: ((Int) -> Unit)? = null

    inner class ViewHolder(val inputField: InputField) : RecyclerView.ViewHolder(inputField) {
        fun bind(config: InputFieldConfig, position: Int) {
            val fieldId = "field_$position"
            inputField.configure(config, fieldId)

            if (config.type == InputFieldType.CheckboxGroup && config.isRequired) {
                responses[fieldId] = emptySet<String>()
            }

            inputField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    onFieldFocused?.invoke(position)
                }
            }

            inputField.delegate = object : InputFieldDelegate {
                override fun onValueChange(fieldId: String, value: Any?) {
                    responses[fieldId] = value
                    onResponseChange?.invoke(responses.toMap())

                    val isValid = validateAllFields()
                    onValidationChange?.invoke(isValid)
                }

                override fun onValidationChange(fieldId: String, isValid: Boolean) {}
            }
        }
    }

    fun validateAllFields(): Boolean {
        for ((index, config) in formConfigs.withIndex()) {
            if (config.isRequired) {
                val fieldId = "field_$index"
                val value = responses[fieldId]
                if (value == null || value.toString().isBlank()) {
                    return false
                }
            }
        }
        return true
    }

    fun validateAllFieldsAndShowErrors(): Boolean {
        var allValid = true
        var firstInvalidIndex: Int? = null

        for ((index, config) in formConfigs.withIndex()) {
            val fieldId = "field_$index"
            val value = responses[fieldId]

            val isValid = isFieldValid(config, value)

            if (!isValid) {
                allValid = false
                if (firstInvalidIndex == null) firstInvalidIndex = index

                val holder = recyclerView?.findViewHolderForAdapterPosition(index) as? ViewHolder
                holder?.inputField?.setError("Mohon lengkapi field ini terlebih dahulu")
            }
        }
        return allValid
    }

    private fun isFieldValid(config: InputFieldConfig, value: Any?): Boolean {
        if (!config.isRequired) return true

        return when {
            value == null -> false
            value is Collection<*> -> value.isNotEmpty()
            value is Array<*> -> value.isNotEmpty()
            value is String -> value.isNotBlank()
            value is Boolean -> value
            value is Number -> value.toDouble() != 0.0
            else -> {
                val str = value.toString()
                str.isNotBlank() && str != "[]" && str != "{}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inputField = InputField(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ViewHolder(inputField)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(formConfigs[position], position)
    }

    override fun getItemCount(): Int = formConfigs.size

    fun submitList(configs: List<InputFieldConfig>) {
        formConfigs.clear()
        formConfigs.addAll(configs)
        responses.clear()
        notifyDataSetChanged()
    }

    fun getResponses(): Map<String, Any?> = responses.toMap()

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        recyclerView = null
    }
}