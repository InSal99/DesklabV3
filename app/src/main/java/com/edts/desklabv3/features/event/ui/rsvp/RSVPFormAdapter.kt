package com.edts.desklabv3.features.event.ui.rsvp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldDelegate

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
            val editText = findEditText(inputField)

            if (editText != null) {
                editText.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        onFieldFocused?.invoke(position)
                    }
                }
            } else {
                inputField.setOnClickListener {
                    hideKeyboard(inputField)
                    onFieldFocused?.invoke(position)
                }

                inputField.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        hideKeyboard(inputField)
                        onFieldFocused?.invoke(position)
                    }
                }
            }

            inputField.delegate = object : InputFieldDelegate {
                override fun onValueChange(fieldId: String, value: Any?) {
                    responses[fieldId] = value
                    onResponseChange?.invoke(responses.toMap())

                    val isValid = validateAllFields()
                    onValidationChange?.invoke(isValid)
                }

                override fun onValidationChange(fieldId: String, isValid: Boolean) { }
            }
        }

        private fun hideKeyboard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        private fun findEditText(view: View): EditText? {
            if (view is EditText) {
                return view
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val result = findEditText(view.getChildAt(i))
                    if (result != null) return result
                }
            }
            return null
        }

    }

    private fun validateAllFields(): Boolean {
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
        for ((index, config) in formConfigs.withIndex()) {
            val holder = recyclerView?.findViewHolderForAdapterPosition(index) as? ViewHolder
            val field = holder?.inputField
            val isValid = field?.isValid() ?: false

            if (!isValid && config.isRequired) {
                field?.setError("Mohon lengkapi field ini terlebih dahulu")
                allValid = false
            }
        }
        return allValid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inputField = InputField(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  // ← This is the key fix
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

    fun getResponses(): Map<String, Any?> {
        return responses.toMap()
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        recyclerView = null
    }
}