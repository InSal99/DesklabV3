package com.example.components.input.field

import android.content.Context
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.example.components.R
import com.example.components.checkbox.CustomCheckBox
import com.example.components.checkbox.CustomCheckboxDelegate
import com.example.components.radiobutton.CustomRadioGroup
import com.example.components.radiobutton.CustomRadioGroupDelegate
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class InputField @JvmOverloads constructor(
context: Context,
attrs: AttributeSet? = null,
defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var fieldId: String = ""
    var delegate: InputFieldDelegate? = null
    var isFieldRequired = false
    private set

            private val titleTextView: TextView
    private val descriptionTextView: TextView
    private val inputContainer: FrameLayout
    private var currentInputComponent: View? = null
    private var currentConfig: InputFieldConfig? = null

    init {
        orientation = VERTICAL

        // Title TextView
        titleTextView = TextView(context).apply {
            setTextAppearance(R.style.TextMedium_Label2)
        }

        // Description TextView
        descriptionTextView = TextView(context).apply {
            setTextAppearance(R.style.TextRegular_Label4)
            visibility = View.GONE
        }

        // Input Container
        inputContainer = FrameLayout(context)

        // Add views to layout
        addView(titleTextView)
        addView(descriptionTextView)
        addView(inputContainer)
    }

    fun configure(config: InputFieldConfig, id: String = "") {
        this.fieldId = id
        this.currentConfig = config
        this.isFieldRequired = config.isRequired

        setTitle(config.title)
        config.description?.let { setDescription(it) }

        setupInputComponent(config)
    }

    private fun setTitle(title: String) {
        if (!isInEditMode) {
            titleTextView.text = if (isFieldRequired) {
                // Simple approach: append red asterisk using SpannableString
                val spannableTitle = SpannableString("$title *")
                spannableTitle.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorRed30)),
                    title.length + 1, // start of asterisk
                    spannableTitle.length, // end of string
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableTitle
            } else {
                title
            }
        } else {
            titleTextView.text = title
        }
    }

    private fun setDescription(description: String) {
        descriptionTextView.text = description
        descriptionTextView.visibility = if (description.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupInputComponent(config: InputFieldConfig) {
        // Remove existing component
        inputContainer.removeAllViews()
        currentInputComponent = null

        // Create new component based on type
        currentInputComponent = when (config.type) {
            is InputFieldType.TextInput -> createTextInput(config)
            is InputFieldType.TextArea -> createTextArea(config)
            is InputFieldType.Dropdown -> createDropdown(config)
            is InputFieldType.RadioGroup -> createRadioGroup(config)
            is InputFieldType.CheckboxGroup -> createCheckboxGroup(config)
        }

        // Add to container
        currentInputComponent?.let { component ->
            inputContainer.addView(component, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ))
        }
    }

    private fun createTextInput(config: InputFieldConfig): View {
        val textInputLayout = TextInputLayout(context, null, R.attr.textInputStyle).apply {
            boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            hint = config.hint
            isExpandedHintEnabled = false

            if (config.maxLength > 0) {
                isCounterEnabled = true
                counterMaxLength = config.maxLength
            }
        }

        val textInputEditText = TextInputEditText(textInputLayout.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Simple text input
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
            imeOptions = EditorInfo.IME_ACTION_NEXT

            if (config.maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
            }

            doAfterTextChanged { editable ->
                notifyValueChange(editable?.toString())
                notifyValidationChange()
            }
        }

        textInputLayout.addView(textInputEditText)
        return textInputLayout
    }

    private fun createTextArea(config: InputFieldConfig): View {
        val textInputLayout = TextInputLayout(context, null, R.attr.textInputStyle).apply {
            boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            hint = config.hint
            isExpandedHintEnabled = false

            if (config.maxLength > 0) {
                isCounterEnabled = true
                counterMaxLength = config.maxLength
            }
        }

        val textInputEditText = TextInputEditText(textInputLayout.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                    android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            imeOptions = EditorInfo.IME_ACTION_DONE
            setLines(config.minLines)
            maxLines = config.maxLines

            if (config.maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
            }

            doAfterTextChanged { editable ->
                notifyValueChange(editable?.toString())
                notifyValidationChange()
            }
        }

        textInputLayout.addView(textInputEditText)
        return textInputLayout
    }

    private fun createDropdown(config: InputFieldConfig): View {
        val textInputLayout = TextInputLayout(context, null, R.attr.textInputStyle).apply {
            boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
            hint = config.hint
        }

        val autoCompleteTextView = MaterialAutoCompleteTextView(textInputLayout.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            config.options?.let { options ->
                val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, options)
                setAdapter(adapter)
                setOnItemClickListener { _, _, position, _ ->
                    val selectedValue = options.getOrNull(position)
                    notifyValueChange(selectedValue)
                    notifyValidationChange()
                }
            }
        }

        textInputLayout.addView(autoCompleteTextView)
        return textInputLayout
    }

    private fun createRadioGroup(config: InputFieldConfig): View {
        return CustomRadioGroup(context).apply {
            config.options?.let { options ->
                // Use the setData method from CustomRadioGroup
                setData(options) { option -> option }

                // Set up the listener using the custom delegate
                setOnItemSelectedListener(object : CustomRadioGroupDelegate {
                    override fun onItemSelected(position: Int, data: Any?) {
                        notifyValueChange(data?.toString())
                        notifyValidationChange()
                    }
                })
            }
        }
    }

    private fun createCheckboxGroup(config: InputFieldConfig): View {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL

            config.options?.forEach { option ->
                val checkbox = CustomCheckBox(context).apply {
                    text = option
                    // Set custom delegate for individual checkbox changes
                    setCustomCheckBoxDelegate(object : CustomCheckboxDelegate {
                        override fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean) {
                            notifyValueChange(getCheckboxGroupValue(this@apply))
                            notifyValidationChange()
                        }
                    })
                }
                addView(checkbox)
            }
        }
    }

    // Value management methods
    fun getValue(): Any? {
        return when (currentConfig?.type) {
            is InputFieldType.TextInput -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                textInputLayout?.editText?.text?.toString()
            }
            is InputFieldType.TextArea -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                textInputLayout?.editText?.text?.toString()
            }
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                val autoComplete = textInputLayout?.editText as? AutoCompleteTextView
                autoComplete?.text?.toString()?.takeIf { it.isNotEmpty() }
            }
            is InputFieldType.RadioGroup -> {
                val radioGroup = currentInputComponent as? CustomRadioGroup
                radioGroup?.getSelectedData()?.toString()
            }
            is InputFieldType.CheckboxGroup -> {
                getCheckboxGroupValue(currentInputComponent as? LinearLayout)
            }
            else -> null
        }
    }

    private fun getCheckboxGroupValue(container: LinearLayout?): List<String> {
        if (container == null) return emptyList()

        val selectedValues = mutableListOf<String>()
        for (i in 0 until container.childCount) {
            val checkbox = container.getChildAt(i) as? CustomCheckBox
            if (checkbox?.isChecked == true) {
                selectedValues.add(checkbox.text.toString())
            }
        }
        return selectedValues
    }

    fun setValue(value: Any?) {
        when (currentConfig?.type) {
            is InputFieldType.TextInput -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                textInputLayout?.editText?.setText(value?.toString())
            }
            is InputFieldType.TextArea -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                textInputLayout?.editText?.setText(value?.toString())
            }
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                val autoComplete = textInputLayout?.editText as? AutoCompleteTextView
                autoComplete?.setText(value?.toString(), false)
            }
            is InputFieldType.RadioGroup -> {
                val radioGroup = currentInputComponent as? CustomRadioGroup
                val valueStr = value?.toString()
                if (valueStr != null) {
                    radioGroup?.selectItemByData(valueStr)
                }
            }
            is InputFieldType.CheckboxGroup -> {
                val container = currentInputComponent as? LinearLayout
                val selectedValues = value as? List<String> ?: emptyList()
                for (i in 0 until (container?.childCount ?: 0)) {
                    val checkbox = container?.getChildAt(i) as? CustomCheckBox
                    checkbox?.isChecked = selectedValues.contains(checkbox?.text?.toString())
                }
            }

            null -> TODO()
        }
    }

    fun isValid(): Boolean {
        if (!isFieldRequired) return true

        return when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.TextArea -> !getValue().toString().isNullOrBlank()
            is InputFieldType.Dropdown,
            is InputFieldType.RadioGroup -> getValue() != null
            is InputFieldType.CheckboxGroup -> (getValue() as? List<*>)?.isNotEmpty() == true
            else -> false
        }
    }

    fun setError(errorText: String?) {
        if (!errorText.isNullOrEmpty()) {
            when (currentConfig?.type) {
                is InputFieldType.TextInput,
                is InputFieldType.TextArea,
                is InputFieldType.Dropdown -> {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    textInputLayout?.error = errorText
                    textInputLayout?.isErrorEnabled = true
                }
                is InputFieldType.RadioGroup,
                is InputFieldType.CheckboxGroup -> {
                    // For radio/checkbox groups, you could show error in title or add error view
                    // One approach is to change the title color or add an error TextView
                    titleTextView.setTextColor(ContextCompat.getColor(context, R.color.colorRed30))
                }
            }

            // Add vibration animation if the extension functions exist
//            try {
//                this.vibrateAnimation().also {
//                    context.vibratePhone(HapticFeedback.ERROR)
//                }
//            } catch (e: Exception) {
//                // Handle case where vibration methods don't exist
//            }
        } else {
            clearError()
        }
    }

    fun clearError() {
        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.TextArea,
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                textInputLayout?.error = null
                textInputLayout?.isErrorEnabled = false
            }
            is InputFieldType.RadioGroup,
            is InputFieldType.CheckboxGroup -> {
                // Reset title color to default
                titleTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            }

            null -> TODO()
        }
    }

    private fun notifyValueChange(value: Any?) {
        delegate?.onValueChange(fieldId, value)
    }

    private fun notifyValidationChange() {
        delegate?.onValidationChange(fieldId, isValid())
    }
}