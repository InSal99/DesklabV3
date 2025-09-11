package com.edts.desklabv3.features.event.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edts.components.input.field.InputField
import com.edts.components.input.field.InputFieldConfig
import com.edts.components.input.field.InputFieldDelegate
import java.util.UUID

class RSVPFormAdapter : RecyclerView.Adapter<RSVPFormAdapter.ViewHolder>() {

    private val formConfigs = mutableListOf<InputFieldConfig>()
    private val responses = mutableMapOf<String, Any?>()
    private var recyclerView: RecyclerView? = null

    var onResponseChange: ((Map<String, Any?>) -> Unit)? = null
    var onValidationChange: ((Boolean) -> Unit)? = null

    inner class ViewHolder(val inputField: InputField) : RecyclerView.ViewHolder(inputField) {
        fun bind(config: InputFieldConfig, position: Int) {
            val fieldId = "field_$position"

            inputField.configure(config, fieldId)

            inputField.delegate = object : InputFieldDelegate {
                override fun onValueChange(fieldId: String, value: Any?) {
                    responses[fieldId] = value
                    onResponseChange?.invoke(responses.toMap())

                    val isValid = validateAllFields()
                    onValidationChange?.invoke(isValid)
                }

                override fun onValidationChange(fieldId: String, isValid: Boolean) {
                    // Handle validation if needed
                }
            }
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




//class RSVPFormAdapter : RecyclerView.Adapter<RSVPFormAdapter.ViewHolder>() {
//
//    private val formConfigs = mutableListOf<InputFieldConfig>()
//    private val fieldResponses = mutableMapOf<String, Any?>()
//
//    var onFieldValueChange: ((String, Any?) -> Unit)? = null
//    var onFieldValidationChange: ((String, Boolean) -> Unit)? = null
//
//    inner class ViewHolder(val inputField: InputField) : RecyclerView.ViewHolder(inputField) {
//        fun bind(config: InputFieldConfig, fieldId: String) {
//            inputField.configure(config, fieldId)
//
//            // Set existing value if available
//            fieldResponses[fieldId]?.let { inputField.setValue(it) }
//
//            inputField.delegate = object : InputFieldDelegate {
//                override fun onValueChange(fieldId: String, value: Any?) {
//                    fieldResponses[fieldId] = value
//                    onFieldValueChange?.invoke(fieldId, value)
//                }
//
//                override fun onValidationChange(fieldId: String, isValid: Boolean) {
//                    onFieldValidationChange?.invoke(fieldId, isValid)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inputField = InputField(parent.context).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        }
//        return ViewHolder(inputField)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val config = formConfigs[position]
//        val fieldId = "field_$position" // Simple ID generation
//        holder.bind(config, fieldId)
//    }
//
//    override fun getItemCount(): Int = formConfigs.size
//
//    fun setFormConfigs(configs: List<InputFieldConfig>) {
//        formConfigs.clear()
//        formConfigs.addAll(configs)
//        notifyDataSetChanged()
//    }
//
//    fun getFieldValues(): Map<String, Any?> {
//        return fieldResponses.toMap()
//    }
//
//    fun clearAllValues() {
//        fieldResponses.clear()
//        notifyDataSetChanged()
//    }
//}



//class RSVPFormAdapter : RecyclerView.Adapter<RSVPFormAdapter.ViewHolder>() {
//
//    private val formItems = mutableListOf<RSVPFormItem>()
//
//    inner class ViewHolder(val inputField: InputField) : RecyclerView.ViewHolder(inputField) {
//        fun bind(item: RSVPFormItem) {
//            inputField.setValue(item)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        // Create your custom InputField programmatically
//        val inputField = InputField(parent.context).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        }
//        return ViewHolder(inputField)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(formItems[position])
//    }
//
//    override fun getItemCount(): Int = formItems.size
//
//    fun submitList(items: List<RSVPFormItem>) {
//        formItems.clear()
//        formItems.addAll(items)
//        notifyDataSetChanged()
//    }
//
//    // Method to collect all responses
//    fun getFormResponses(): Map<String, String> {
//        val responses = mutableMapOf<String, String>()
//        formItems.forEachIndexed { index, item ->
//            // You might need to track responses differently since we're using custom views
//            // This approach assumes you can access the InputField's response
//            val response = "get_response_from_view_holder" // Implement this properly
//            responses[item.id] = response
//        }
//        return responses
//    }
//}
//
//data class RSVPFormItem(
//    val id: String,
//    val question: String,
//    val isRequired: Boolean,
//    val inputType: InputType = InputType.TEXT,
//    val placeholder: String? = null
//)
//
//enum class InputType {
//    TEXT, NUMBER, EMAIL, PHONE, MULTILINE
//}