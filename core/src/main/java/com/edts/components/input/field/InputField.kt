package com.edts.components.input.field

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.edts.components.R
import com.edts.components.checkbox.CustomCheckBox
import com.edts.components.checkbox.CustomCheckboxDelegate
import com.edts.components.option.card.OptionCard
import com.edts.components.option.card.OptionCardDelegate
import com.edts.components.radiobutton.CustomRadioGroup
import com.edts.components.radiobutton.CustomRadioGroupDelegate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.MaterialAutoCompleteTextView
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
    private val errorTextView: TextView
    private val inputContainer: FrameLayout
    private var currentInputComponent: View? = null
    private var currentConfig: InputFieldConfig? = null

    private var requiredColor: Int = R.color.colorRed30
    private var errorColor: Int = R.color.colorRed50

    private var isFieldEnabled: Boolean = true
    private var currentErrorText: String? = null
    private var showErrorFromXml: Boolean = false

    private val density = context.resources.displayMetrics.density
    private val colorCache = mutableMapOf<Int, Int>()
    private val drawableCache = mutableMapOf<String, Drawable>()

    private val matchParentWrapParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    )

    private val padding4dp = (4 * density).toInt()
    private val padding8dp = (8 * density).toInt()
    private val padding10dp = (10 * density).toInt()
    private val padding12dp = (12 * density).toInt()
    private val padding16dp = (16 * density).toInt()
    private val padding24dp = (24 * density).toInt()
    private val cornerRadius = 12f * density
    private val strokeWidth1dp = (1 * density).toInt()
    private val strokeWidth2dp = (2 * density).toInt()
    private val minHeight40dp = (40 * density).toInt()
    private val minHeight48dp = (48 * density).toInt()
    private val iconSize24dp = (24 * density).toInt()

    private var textInputContainer: LinearLayout? = null
    private var textInputEditText: EditText? = null
    private var dropdownContainer: LinearLayout? = null
    private var dropdownTextView: TextView? = null
    private var radioGroupComponent: CustomRadioGroup? = null
    private var checkboxContainer: LinearLayout? = null

    init {
        orientation = VERTICAL
        cacheCommonColors()

        titleTextView = TextView(context).apply {
            setTextAppearance(R.style.TextMedium_Label2)
            setTextColor(getCachedColor(R.attr.colorForegroundSecondary, R.color.colorNeutral60))
            setPadding(0, 0, 0, padding4dp)
        }

        descriptionTextView = TextView(context).apply {
            setTextAppearance(R.style.TextRegular_Label4)
            setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
            setPadding(0, 0, 0, padding8dp)
            visibility = View.GONE
        }

        errorTextView = TextView(context).apply {
            setTextAppearance(R.style.TextRegular_Label4)
            setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
            setPadding(0, padding4dp, 0, 0)
            visibility = View.GONE
            textSize = 12f
        }

        inputContainer = FrameLayout(context)

        addView(titleTextView)
        addView(descriptionTextView)
        addView(inputContainer)
        addView(errorTextView)

        attrs?.let { parseAttributes(it, defStyleAttr) }
    }

    private fun cacheCommonColors() {
        getCachedColor(R.attr.colorForegroundPrimary, R.color.color000)
        getCachedColor(R.attr.colorForegroundSecondary, R.color.colorNeutral60)
        getCachedColor(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40)
        getCachedColor(R.attr.colorForegroundTertiary, R.color.colorNeutral50)
        getCachedColor(R.attr.colorForegroundDisabled, R.color.colorNeutral30)
        getCachedColor(R.attr.colorBackgroundPrimary, R.color.colorFFF)
        getCachedColor(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
        getCachedColor(R.attr.colorStrokeAccent, R.color.colorPrimary30)
        getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50)
    }

    private fun getCachedColor(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
        val key = attrRes
        return colorCache.getOrPut(key) {
            resolveColorAttribute(attrRes, fallbackColor)
        }
    }

    private fun getCachedDrawable(key: String, creator: () -> Drawable): Drawable {
        return drawableCache.getOrPut(key, creator)
    }

    private fun parseAttributes(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputField, defStyleAttr, 0)

        try {
            val title = typedArray.getString(R.styleable.InputField_fieldTitle) ?: ""
            val description = typedArray.getString(R.styleable.InputField_fieldDescription)
            val hint = typedArray.getString(R.styleable.InputField_fieldHint)
            val required = typedArray.getBoolean(R.styleable.InputField_fieldRequired, false)
            val inputTypeValue = typedArray.getInt(R.styleable.InputField_inputType, 0)
            val maxLength = typedArray.getInt(R.styleable.InputField_maxLength, 0)
            val minLength = typedArray.getInt(R.styleable.InputField_minLength, 0)
            val maxLines = typedArray.getInt(R.styleable.InputField_maxLines, 4)
            val minLines = typedArray.getInt(R.styleable.InputField_minLines, 3)

            isFieldEnabled = typedArray.getBoolean(R.styleable.InputField_fieldEnabled, true)
            currentErrorText = typedArray.getString(R.styleable.InputField_errorText)
            showErrorFromXml = typedArray.getBoolean(R.styleable.InputField_showError, false)

            val optionsArrayId = typedArray.getResourceId(R.styleable.InputField_options, -1)
            val options = if (optionsArrayId != -1) {
                context.resources.getStringArray(optionsArrayId).toList()
            } else null

            requiredColor = getCachedColor(
                typedArray.getResourceId(R.styleable.InputField_requiredColor, -1),
                R.color.colorRed30
            )

            errorColor = getCachedColor(
                typedArray.getResourceId(R.styleable.InputField_errorColor, -1),
                R.color.colorRed50
            )

            val titleTextAppearance = typedArray.getResourceId(R.styleable.InputField_titleTextAppearance, -1)
            if (titleTextAppearance != -1) {
                titleTextView.setTextAppearance(titleTextAppearance)
            }

            val descriptionTextAppearance = typedArray.getResourceId(R.styleable.InputField_descriptionTextAppearance, -1)
            if (descriptionTextAppearance != -1) {
                descriptionTextView.setTextAppearance(descriptionTextAppearance)
            }

            val inputFieldType = when (inputTypeValue) {
                0 -> InputFieldType.TextInput
                1 -> InputFieldType.TextArea
                2 -> InputFieldType.Dropdown
                3 -> InputFieldType.RadioGroup
                4 -> InputFieldType.CheckboxGroup
                else -> InputFieldType.TextInput
            }

            if (title.isNotEmpty()) {
                val config = InputFieldConfig(
                    type = inputFieldType,
                    title = title,
                    description = description,
                    hint = hint,
                    isRequired = required,
                    options = options,
                    maxLines = maxLines,
                    minLines = minLines,
                    maxLength = maxLength,
                    minLength = minLength
                )
                configure(config)
            }

            post {
                setEnabled(isFieldEnabled)
                if (showErrorFromXml && !currentErrorText.isNullOrEmpty()) {
                    setError(currentErrorText)
                }
            }

        } finally {
            typedArray.recycle()
        }
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
                val spannableTitle = SpannableString("$title *")
                spannableTitle.setSpan(
                    ForegroundColorSpan(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.colorRed50)),
                    title.length + 1,
                    spannableTitle.length,
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

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isFieldEnabled = enabled

        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.TextArea -> {
                textInputEditText?.let { editText ->
                    textInputContainer?.let { container ->
                        editText.isEnabled = enabled
                        updateTextInputColors(editText, enabled)
                        updateInputBackground(container, editText, editText.hasFocus() && enabled)
                    }
                } ?: run {
                    // Fallback to old TextInputLayout structure
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    textInputLayout?.isEnabled = enabled
                    textInputLayout?.editText?.isEnabled = enabled
                }
            }
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                if (textInputLayout != null) {
                    textInputLayout.isEnabled = enabled
                    textInputLayout.editText?.isEnabled = enabled
                } else {
                    dropdownContainer?.let { container ->
                        dropdownTextView?.let { textView ->
                            container.isEnabled = enabled
                            container.isClickable = enabled
                            updateDropdownColors(container, textView, enabled)
                        }
                    }
                }
            }
            is InputFieldType.RadioGroup -> {
                radioGroupComponent?.let { radioGroup ->
                    radioGroup.isEnabled = enabled
                    for (i in 0 until radioGroup.childCount) {
                        radioGroup.getChildAt(i)?.isEnabled = enabled
                    }
                }
            }
            is InputFieldType.CheckboxGroup -> {
                checkboxContainer?.let { container ->
                    container.isEnabled = enabled
                    for (i in 0 until container.childCount) {
                        val checkbox = container.getChildAt(i) as? CustomCheckBox
                        checkbox?.isEnabled = enabled
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun updateTextInputColors(editText: EditText, enabled: Boolean) {
        if (enabled) {
            editText.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.color000))
            editText.setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
        } else {
            val disabledColor = getCachedColor(R.attr.colorForegroundDisabled, R.color.colorNeutral30)
            editText.setTextColor(disabledColor)
            editText.setHintTextColor(disabledColor)
        }
    }

    private fun updateDropdownColors(container: LinearLayout, textView: TextView, enabled: Boolean) {
        if (enabled) {
            textView.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.color000))
            container.background = getCachedDrawable("rounded_normal") { createRoundedBackground() }
        } else {
            textView.setTextColor(getCachedColor(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
            container.background = getCachedDrawable("rounded_disabled") { createDisabledBackground() }
        }
    }

    fun setError(errorText: String?) {
        currentErrorText = errorText

        if (!errorText.isNullOrEmpty()) {
            when (currentConfig?.type) {
                is InputFieldType.TextInput,
                is InputFieldType.TextArea -> {
                    val tagMap = textInputEditText?.tag as? Map<String, TextView>
                    val inlineErrorText = tagMap?.get("errorText")
                    val counterText = tagMap?.get("counterText")

                    if (inlineErrorText != null) {
                        inlineErrorText.text = errorText
                        inlineErrorText.visibility = View.VISIBLE
                        errorTextView.visibility = View.GONE
                        counterText?.setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
                    } else {
                        errorTextView.text = errorText
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
                    }

                    textInputContainer?.background = (getCachedDrawable("rounded_error") { createErrorBackground() }
                        ?: run {
                            // Fallback to old TextInputLayout structure
                            val textInputLayout = currentInputComponent as? TextInputLayout
                            textInputLayout?.error = errorText
                            textInputLayout?.isErrorEnabled = true
                        }) as Drawable?
                }
                is InputFieldType.Dropdown -> {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    if (textInputLayout != null) {
                        textInputLayout.error = errorText
                        textInputLayout.isErrorEnabled = true
                    } else {
                        errorTextView.text = errorText
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
                        dropdownContainer?.background = getCachedDrawable("rounded_error") { createErrorBackground() }
                    }
                }
                is InputFieldType.RadioGroup,
                is InputFieldType.CheckboxGroup -> {
                    errorTextView.text = errorText
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
                }
                else -> {
                }
            }
        } else {
            clearError()
        }
    }

    fun clearError() {
        currentErrorText = null
        errorTextView.visibility = View.GONE
        val tagMap = textInputEditText?.tag as? Map<String, TextView>
        val inlineErrorText = tagMap?.get("errorText")
        val counterText = tagMap?.get("counterText")

        inlineErrorText?.visibility = View.GONE
        counterText?.setTextColor(getCachedColor(R.attr.colorForegroundTertiary, R.color.colorNeutral50))

        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.TextArea -> {
                textInputEditText?.let { editText ->
                    textInputContainer?.let { container ->
                        updateInputBackground(container, editText, editText.hasFocus())
                    }
                } ?: run {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    textInputLayout?.error = null
                    textInputLayout?.isErrorEnabled = false
                }
            }
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                if (textInputLayout != null) {
                    textInputLayout.error = null
                    textInputLayout.isErrorEnabled = false
                } else {
                    dropdownContainer?.let { container ->
                        dropdownTextView?.let { textView ->
                            container.background = if (isFieldEnabled) {
                                getCachedDrawable("rounded_normal") { createRoundedBackground() }
                            } else {
                                getCachedDrawable("rounded_disabled") { createDisabledBackground() }
                            }
                        }
                    }
                }
            }
            is InputFieldType.RadioGroup,
            is InputFieldType.CheckboxGroup -> {
                titleTextView.setTextColor(getCachedColor(R.attr.colorForegroundSecondary, R.color.colorNeutral60))
            }
            else -> {
            }
        }
    }

    fun getErrorText(): String? = currentErrorText
    fun isFieldEnabled(): Boolean = isFieldEnabled

    private fun setupInputComponent(config: InputFieldConfig) {
        inputContainer.removeAllViews()
        currentInputComponent = null
        textInputContainer = null
        textInputEditText = null
        dropdownContainer = null
        dropdownTextView = null
        radioGroupComponent = null
        checkboxContainer = null

        currentInputComponent = when (config.type) {
            is InputFieldType.TextInput -> createTextInput(config)
            is InputFieldType.TextArea -> createTextArea(config)
            is InputFieldType.Dropdown -> createCustomDropdownView(config)
            is InputFieldType.RadioGroup -> createRadioGroup(config)
            is InputFieldType.CheckboxGroup -> createCheckboxGroup(config)
        }

        currentInputComponent?.let { component ->
            inputContainer.addView(component, matchParentWrapParams)
        }
    }

    private fun createTextInput(config: InputFieldConfig): View {
        val container = LinearLayout(context).apply {
            background = getCachedDrawable("rounded_normal") { createRoundedBackground() }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(padding12dp, (2 * density).toInt(), padding12dp, (2 * density).toInt())
            minimumHeight = minHeight48dp
            isFocusable = false
            isClickable = false
        }
        textInputContainer = container

        val editText = EditText(context).apply {
            hint = config.hint
            setTextAppearance(R.style.TextRegular_Paragraph1)
            setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.color000))
            setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
            background = null
            textSize = 16f

            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
            imeOptions = EditorInfo.IME_ACTION_NEXT

            if (config.maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
            }

            setOnFocusChangeListener { _, hasFocus ->
                updateInputBackground(container, this, hasFocus)
            }

            doAfterTextChanged { editable ->
                notifyValueChange(editable?.toString())
                notifyValidationChange()
            }

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        textInputEditText = editText

        if (config.maxLength > 0) {
            val outerContainer = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
            }

            val bottomRowContainer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = padding4dp
                }
            }

            val errorTextForCounter = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
                textSize = 12f
                visibility = View.GONE

                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }

            val counterText = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
                textSize = 12f
                gravity = Gravity.END

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            var lastLength = 0
            val updateCounter = {
                val currentLength = editText.text?.length ?: 0
                if (currentLength != lastLength) {
                    counterText.text = "($currentLength/${config.maxLength})"
                    lastLength = currentLength
                }
            }

            updateCounter()

            editText.doAfterTextChanged {
                updateCounter()
            }

            editText.tag = mapOf("errorText" to errorTextForCounter, "counterText" to counterText)

            bottomRowContainer.addView(errorTextForCounter)
            bottomRowContainer.addView(counterText)

            outerContainer.addView(container)
            outerContainer.addView(bottomRowContainer)
            container.addView(editText)
            return outerContainer
        }

        container.addView(editText)
        return container
    }

    private fun createTextArea(config: InputFieldConfig): View {
        val container = LinearLayout(context).apply {
            background = getCachedDrawable("rounded_normal") { createRoundedBackground() }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.TOP
            setPadding(padding12dp, padding8dp, padding12dp, padding8dp)
            minimumHeight = (config.minLines * 24 * density).toInt()
            isFocusable = false
            isClickable = false
        }
        textInputContainer = container

        val editText = EditText(context).apply {
            hint = config.hint
            setTextAppearance(R.style.TextRegular_Paragraph1)
            setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.color000))
            setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
            background = null
            textSize = 16f
            gravity = Gravity.TOP

            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                    android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            imeOptions = EditorInfo.IME_ACTION_DONE
            setLines(config.minLines)
            maxLines = config.maxLines

            if (config.maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
            }

            setOnFocusChangeListener { _, hasFocus ->
                updateInputBackground(container, this, hasFocus)
            }

            doAfterTextChanged { editable ->
                notifyValueChange(editable?.toString())
                notifyValidationChange()
            }

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        textInputEditText = editText

        if (config.maxLength > 0) {
            val outerContainer = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
            }

            val bottomRowContainer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = padding4dp
                }
            }

            val errorTextForCounter = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
                textSize = 12f
                visibility = View.GONE

                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }

            val counterText = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
                textSize = 12f
                gravity = Gravity.END

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            var lastLength = 0
            val updateCounter = {
                val currentLength = editText.text?.length ?: 0
                if (currentLength != lastLength) {
                    counterText.text = "($currentLength/${config.maxLength})"
                    lastLength = currentLength
                }
            }

            updateCounter()

            editText.doAfterTextChanged {
                updateCounter()
            }

            editText.tag = mapOf("errorText" to errorTextForCounter, "counterText" to counterText)

            bottomRowContainer.addView(errorTextForCounter)
            bottomRowContainer.addView(counterText)

            outerContainer.addView(container)
            outerContainer.addView(bottomRowContainer)
            container.addView(editText)
            return outerContainer
        }

        container.addView(editText)
        return container
    }

    private fun createFocusedBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = cornerRadius
        shape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.colorFFF))
        shape.setStroke(strokeWidth2dp, getCachedColor(R.attr.colorStrokeAccent, R.color.colorPrimary30))
        return shape
    }

    private fun createDisabledBackground(): Drawable {
        val baseShape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            this.cornerRadius = this@InputField.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundDisabled, R.color.color000Opacity5))
            setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeSubtle, R.color.colorNeutral30))
        }

        val overlayShape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            this.cornerRadius = this@InputField.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated, R.color.colorOpacityWhite5))
        }

        return LayerDrawable(arrayOf(baseShape, overlayShape))
    }

//    private fun updateInputBackground(container: LinearLayout, editText: EditText, hasFocus: Boolean) {
//        container.background = when {
//            !currentErrorText.isNullOrEmpty() -> getCachedDrawable("rounded_error") { createErrorBackground() }
//            !editText.isEnabled -> getCachedDrawable("rounded_disabled") { createDisabledBackground() }
//            hasFocus -> getCachedDrawable("rounded_focused") { createFocusedBackground() }
//            else -> getCachedDrawable("rounded_normal") { createRoundedBackground() }
//        }
//    }

    private fun updateInputBackground(container: LinearLayout, editText: EditText, hasFocus: Boolean) {
        container.background = when {
            // FIXED: Prioritize disabled state over error state
            !editText.isEnabled -> getCachedDrawable("rounded_disabled") { createDisabledBackground() }
            !currentErrorText.isNullOrEmpty() && editText.isEnabled -> getCachedDrawable("rounded_error") { createErrorBackground() }
            hasFocus -> getCachedDrawable("rounded_focused") { createFocusedBackground() }
            else -> getCachedDrawable("rounded_normal") { createRoundedBackground() }
        }
    }

    private fun createErrorBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = cornerRadius
        shape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.colorFFF))
        shape.setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
        return shape
    }

    private fun createCustomDropdownView(config: InputFieldConfig): View {
        val container = LinearLayout(context).apply {
            background = getCachedDrawable("rounded_normal") { createRoundedBackground() }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(padding12dp, padding10dp, padding12dp, padding10dp)
            minimumHeight = minHeight40dp
            isFocusable = true
            isClickable = true
        }
        dropdownContainer = container

        val textView = TextView(context).apply {
            text = config.hint ?: "Select option"
            setTextAppearance(R.style.TextRegular_Paragraph1)
            setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
            gravity = Gravity.CENTER_VERTICAL
            textSize = 16f

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        dropdownTextView = textView

        val iconView = ImageView(context).apply {
            setImageResource(R.drawable.placeholder)
            setColorFilter(getCachedColor(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
            scaleType = ImageView.ScaleType.CENTER

            layoutParams = FrameLayout.LayoutParams(
                iconSize24dp, iconSize24dp,
                Gravity.CENTER_VERTICAL or Gravity.END
            )
        }

        container.addView(textView)
        container.addView(iconView)

        container.setOnClickListener {
            showOptionDrawer(config.options) { selectedOption ->
                textView.text = selectedOption
                textView.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.color000))

                notifyValueChange(selectedOption)
                notifyValidationChange()
            }
        }

        return container
    }

    private fun createRoundedBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = cornerRadius
        shape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.colorFFF))
        shape.setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeSubtle, R.color.colorNeutral30))

        val focusedShape = GradientDrawable()
        focusedShape.shape = GradientDrawable.RECTANGLE
        focusedShape.cornerRadius = cornerRadius
        focusedShape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.colorFFF))
        focusedShape.setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeAccent, R.color.colorPrimary30))

        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusedShape)
        stateListDrawable.addState(intArrayOf(), shape)

        return stateListDrawable
    }

    private fun showOptionDrawer(options: List<String>?, onSelection: ((String) -> Unit)? = null) {
        if (options.isNullOrEmpty()) return

        val bottomSheetDialog = BottomSheetDialog(context)
        val bottomSheetBinding = createOptionDrawerLayout(options) { selectedOption ->
            if (onSelection != null) {
                onSelection(selectedOption)
            } else {
                val textInputLayout = currentInputComponent as? TextInputLayout
                val autoCompleteTextView = textInputLayout?.editText as? MaterialAutoCompleteTextView
                autoCompleteTextView?.setText(selectedOption, false)
            }

            notifyValueChange(selectedOption)
            notifyValidationChange()

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding)
        bottomSheetDialog.show()
    }

    private fun createOptionDrawerLayout(
        options: List<String>,
        onOptionSelected: (String) -> Unit
    ): View {
        val mainContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(padding16dp, padding24dp, padding16dp, padding16dp)
        }

        val titleTextView = TextView(context).apply {
            text = currentConfig?.hint
            setTextAppearance(R.style.TextSemiBold_Heading1)
            setPadding(padding16dp, padding8dp, padding16dp, padding24dp)
        }
        mainContainer.addView(titleTextView)

        val scrollView = ScrollView(context)
        val optionsContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        options.forEach { option ->
            val optionCard = OptionCard(context).apply {
                titleText = option

                delegate = object : OptionCardDelegate {
                    override fun onClick(card: OptionCard) {
                        Log.d("OptionCard", "${card.titleText} selected")
                        onOptionSelected(option)
                    }
                }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = padding8dp
                }
                this.layoutParams = layoutParams
            }

            optionsContainer.addView(optionCard)
        }

        scrollView.addView(optionsContainer)
        mainContainer.addView(scrollView)

        return mainContainer
    }

    private fun createRadioGroup(config: InputFieldConfig): View {
        return CustomRadioGroup(context).apply {
            radioGroupComponent = this
            config.options?.let { options ->
                setData(options) { option -> option }

                for (i in 0 until childCount) {
                    val child = getChildAt(i)
                    val layoutParams = child.layoutParams as? MarginLayoutParams
                    layoutParams?.let { params ->
                        if (i > 0) {
                            params.topMargin = padding8dp
                        }
                        if (i < childCount - 1) {
                            params.bottomMargin = padding8dp / 2
                        }
                        child.layoutParams = params
                    }
                }

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
            checkboxContainer = this
            orientation = LinearLayout.VERTICAL

            config.options?.forEach { option ->
                val checkbox = CustomCheckBox(context).apply {
                    text = option
                    setCustomCheckBoxDelegate(object : CustomCheckboxDelegate {
                        override fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean) {
                            notifyValueChange(getCheckboxGroupValue(this@InputField))
                            notifyValidationChange()
                        }
                    })
                }
                addView(checkbox)
            }
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

    fun getValue(): Any? {
        return when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.TextArea -> {
                textInputEditText?.text?.toString() ?: run {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    textInputLayout?.editText?.text?.toString()
                }
            }
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                if (textInputLayout != null) {
                    val autoComplete = textInputLayout.editText as? AutoCompleteTextView
                    autoComplete?.text?.toString()?.takeIf { it.isNotEmpty() }
                } else {
                    dropdownTextView?.text?.toString()?.takeIf {
                        it != currentConfig?.hint && it.isNotEmpty()
                    }
                }
            }
            is InputFieldType.RadioGroup -> {
                radioGroupComponent?.getSelectedData()?.toString()
            }
            is InputFieldType.CheckboxGroup -> {
                getCheckboxGroupValue(checkboxContainer)
            }
            else -> null
        }
    }

    fun setValue(value: Any?) {
        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.TextArea -> {
                // Use direct reference for better performance
                textInputEditText?.setText(value?.toString()) ?: run {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    textInputLayout?.editText?.setText(value?.toString())
                }
            }
            is InputFieldType.Dropdown -> {
                val textInputLayout = currentInputComponent as? TextInputLayout
                if (textInputLayout != null) {
                    val autoComplete = textInputLayout.editText as? AutoCompleteTextView
                    autoComplete?.setText(value?.toString(), false)
                } else {
                    // Use direct reference
                    dropdownTextView?.let { textView ->
                        if (value != null) {
                            textView.text = value.toString()
                            textView.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.color000))
                        }
                    }
                }
            }
            is InputFieldType.RadioGroup -> {
                val valueStr = value?.toString()
                if (valueStr != null) {
                    radioGroupComponent?.selectItemByData(valueStr)
                }
            }
            is InputFieldType.CheckboxGroup -> {
                val selectedValues = value as? List<String> ?: emptyList()
                checkboxContainer?.let { container ->
                    for (i in 0 until container.childCount) {
                        val checkbox = container.getChildAt(i) as? CustomCheckBox
                        checkbox?.isChecked = selectedValues.contains(checkbox?.text?.toString())
                    }
                }
            }
            else -> {
                // Handle null case - do nothing
            }
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

    private fun notifyValueChange(value: Any?) {
        delegate?.onValueChange(fieldId, value)
    }

    private fun notifyValidationChange() {
        delegate?.onValidationChange(fieldId, isValid())
    }

    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            ContextCompat.getColor(context, fallbackColor)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        colorCache.clear()
        drawableCache.clear()
    }
}




//class InputField @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : LinearLayout(context, attrs, defStyleAttr) {
//
//    var fieldId: String = ""
//    var delegate: InputFieldDelegate? = null
//    var isFieldRequired = false
//        private set
//
//    private val titleTextView: TextView
//    private val descriptionTextView: TextView
//    private val errorTextView: TextView
//    private val inputContainer: FrameLayout
//    private var currentInputComponent: View? = null
//    private var currentConfig: InputFieldConfig? = null
//
//    // XML attributes
//    private var requiredColor: Int = R.color.colorRed30
//    private var errorColor: Int = R.color.colorRed50
//
//    // State variables
//    private var isFieldEnabled: Boolean = true
//    private var currentErrorText: String? = null
//    private var showErrorFromXml: Boolean = false
//
//    init {
//        orientation = VERTICAL
//
//        // Title TextView
//        titleTextView = TextView(context).apply {
//            setTextAppearance(R.style.TextMedium_Label2)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundSecondary, R.color.colorNeutral60))
//            setPadding(0, 0, 0, (4 * context.resources.displayMetrics.density).toInt())
//        }
//
//        // Description TextView
//        descriptionTextView = TextView(context).apply {
//            setTextAppearance(R.style.TextRegular_Label4)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            setPadding(0, 0, 0, (8 * context.resources.displayMetrics.density).toInt())
//            visibility = View.GONE
//        }
//
//        // Error TextView
//        errorTextView = TextView(context).apply {
//            setTextAppearance(R.style.TextRegular_Label4)
//            setTextColor(resolveColorAttribute(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
//            setPadding(0, (4 * context.resources.displayMetrics.density).toInt(), 0, 0)
//            visibility = View.GONE
//            textSize = 12f
//        }
//
//        // Input Container
//        inputContainer = FrameLayout(context)
//
//        // Add views to layout
//        addView(titleTextView)
//        addView(descriptionTextView)
//        addView(inputContainer)
//        addView(errorTextView)
//
//        // Parse XML attributes if provided
//        attrs?.let { parseAttributes(it, defStyleAttr) }
//    }
//
//    private fun parseAttributes(attrs: AttributeSet, defStyleAttr: Int) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputField, defStyleAttr, 0)
//
//        try {
//            // Get field configuration from XML
//            val title = typedArray.getString(R.styleable.InputField_fieldTitle) ?: ""
//            val description = typedArray.getString(R.styleable.InputField_fieldDescription)
//            val hint = typedArray.getString(R.styleable.InputField_fieldHint)
//            val required = typedArray.getBoolean(R.styleable.InputField_fieldRequired, false)
//            val inputTypeValue = typedArray.getInt(R.styleable.InputField_inputType, 0)
//            val maxLength = typedArray.getInt(R.styleable.InputField_maxLength, 0)
//            val minLength = typedArray.getInt(R.styleable.InputField_minLength, 0)
//            val maxLines = typedArray.getInt(R.styleable.InputField_maxLines, 4)
//            val minLines = typedArray.getInt(R.styleable.InputField_minLines, 3)
//
//            // Get new state attributes
//            isFieldEnabled = typedArray.getBoolean(R.styleable.InputField_fieldEnabled, true)
//            currentErrorText = typedArray.getString(R.styleable.InputField_errorText)
//            showErrorFromXml = typedArray.getBoolean(R.styleable.InputField_showError, false)
//
//            // Get options array if provided
//            val optionsArrayId = typedArray.getResourceId(R.styleable.InputField_options, -1)
//            val options = if (optionsArrayId != -1) {
//                context.resources.getStringArray(optionsArrayId).toList()
//            } else null
//
//            // Get styling attributes
//            requiredColor = resolveColorAttribute(
//                typedArray.getResourceId(R.styleable.InputField_requiredColor, -1),
//                R.color.colorRed30
//            )
//
//            errorColor = resolveColorAttribute(
//                typedArray.getResourceId(R.styleable.InputField_errorColor, -1),
//                R.color.colorRed50
//            )
//
//            // Apply text appearances if specified
//            val titleTextAppearance = typedArray.getResourceId(R.styleable.InputField_titleTextAppearance, -1)
//            if (titleTextAppearance != -1) {
//                titleTextView.setTextAppearance(titleTextAppearance)
//            }
//
//            val descriptionTextAppearance = typedArray.getResourceId(R.styleable.InputField_descriptionTextAppearance, -1)
//            if (descriptionTextAppearance != -1) {
//                descriptionTextView.setTextAppearance(descriptionTextAppearance)
//            }
//
//            // Convert inputType enum to InputFieldType
//            val inputFieldType = when (inputTypeValue) {
//                0 -> InputFieldType.TextInput
//                1 -> InputFieldType.TextArea
//                2 -> InputFieldType.Dropdown
//                3 -> InputFieldType.RadioGroup
//                4 -> InputFieldType.CheckboxGroup
//                else -> InputFieldType.TextInput
//            }
//
//            // Create config and configure the field
//            if (title.isNotEmpty()) {
//                val config = InputFieldConfig(
//                    type = inputFieldType,
//                    title = title,
//                    description = description,
//                    hint = hint,
//                    isRequired = required,
//                    options = options,
//                    maxLines = maxLines,
//                    minLines = minLines,
//                    maxLength = maxLength,
//                    minLength = minLength
//                )
//                configure(config)
//            }
//
//            // Apply initial states after configuration
//            post {
//                setEnabled(isFieldEnabled)
//                if (showErrorFromXml && !currentErrorText.isNullOrEmpty()) {
//                    setError(currentErrorText)
//                }
//            }
//
//        } finally {
//            typedArray.recycle()
//        }
//    }
//
//    fun configure(config: InputFieldConfig, id: String = "") {
//        this.fieldId = id
//        this.currentConfig = config
//        this.isFieldRequired = config.isRequired
//
//        setTitle(config.title)
//        config.description?.let { setDescription(it) }
//
//        setupInputComponent(config)
//    }
//
//    private fun setTitle(title: String) {
//        if (!isInEditMode) {
//            titleTextView.text = if (isFieldRequired) {
//                val spannableTitle = SpannableString("$title *")
//                spannableTitle.setSpan(
//                    ForegroundColorSpan(resolveColorAttribute(R.attr.colorForegroundAttentionIntense, R.color.colorRed50)),
//                    title.length + 1,
//                    spannableTitle.length,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                spannableTitle
//            } else {
//                title
//            }
//        } else {
//            titleTextView.text = title
//        }
//    }
//
//    private fun setDescription(description: String) {
//        descriptionTextView.text = description
//        descriptionTextView.visibility = if (description.isNotEmpty()) View.VISIBLE else View.GONE
//    }
//
//    override fun setEnabled(enabled: Boolean) {
//        super.setEnabled(enabled)
//        isFieldEnabled = enabled
//
//        when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> {
//                val (container, editText) = getTextInputComponents()
//                if (editText != null && container != null) {
//                    editText.isEnabled = enabled
//
//                    // Update text colors based on enabled state
//                    if (enabled) {
//                        editText.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//                        editText.setHintTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//                    } else {
//                        editText.setTextColor(resolveColorAttribute(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
//                        editText.setHintTextColor(resolveColorAttribute(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
//                    }
//
//                    // Update container background
//                    updateInputBackground(container, editText, editText.hasFocus() && enabled)
//                } else {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.isEnabled = enabled
//                    textInputLayout?.editText?.isEnabled = enabled
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                // Handle both old TextInputLayout and new custom dropdown
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    textInputLayout.isEnabled = enabled
//                    textInputLayout.editText?.isEnabled = enabled
//                } else {
//                    // Handle custom dropdown
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    container?.isEnabled = enabled
//                    container?.isClickable = enabled
//
//                    if (textView != null) {
//                        if (enabled) {
//                            textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//                            container.background = createRoundedBackground()
//                        } else {
//                            textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
//                            container.background = createDisabledBackground()
//                        }
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup -> {
//                val radioGroup = currentInputComponent as? CustomRadioGroup
//                radioGroup?.isEnabled = enabled
//                for (i in 0 until (radioGroup?.childCount ?: 0)) {
//                    radioGroup?.getChildAt(i)?.isEnabled = enabled
//                }
//            }
//            is InputFieldType.CheckboxGroup -> {
//                val container = currentInputComponent as? LinearLayout
//                container?.isEnabled = enabled
//                for (i in 0 until (container?.childCount ?: 0)) {
//                    val checkbox = container?.getChildAt(i) as? CustomCheckBox
//                    checkbox?.isEnabled = enabled
//                }
//            }
//            else -> {
//                // Handle null case - do nothing
//            }
//        }
//    }
//
//    fun setError(errorText: String?) {
//        currentErrorText = errorText
//
//        if (!errorText.isNullOrEmpty()) {
//            // Show error text
//            errorTextView.text = errorText
//            errorTextView.visibility = View.VISIBLE
//            errorTextView.setTextColor(resolveColorAttribute(R.attr.colorStrokeAttentionIntense, R.color.colorRed50))
//
//            when (currentConfig?.type) {
//                is InputFieldType.TextInput,
//                is InputFieldType.TextArea -> {
//                    val (container, editText) = getTextInputComponents()
//                    if (editText != null && container != null) {
//                        // Create error background
//                        container.background = createErrorBackground()
//                    } else {
//                        // Fallback to old TextInputLayout structure
//                        val textInputLayout = currentInputComponent as? TextInputLayout
//                        textInputLayout?.error = errorText
//                        textInputLayout?.isErrorEnabled = true
//                    }
//                }
//                is InputFieldType.Dropdown -> {
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    if (textInputLayout != null) {
//                        textInputLayout.error = errorText
//                        textInputLayout.isErrorEnabled = true
//                    } else {
//                        // Handle custom dropdown error state
//                        val container = currentInputComponent as? LinearLayout
//                        container?.background = createErrorBackground()
//                    }
//                }
//                is InputFieldType.RadioGroup,
//                is InputFieldType.CheckboxGroup -> {
//                    // For radio/checkbox, we just show the error text (already handled above)
//                }
//                else -> {
//                    // Handle null case - do nothing
//                }
//            }
//        } else {
//            clearError()
//        }
//    }
//
//    fun clearError() {
//        currentErrorText = null
//        errorTextView.visibility = View.GONE
//
//        when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> {
//                val (container, editText) = getTextInputComponents()
//                if (editText != null && container != null) {
//                    // Restore appropriate background based on current state
//                    updateInputBackground(container, editText, editText.hasFocus())
//                } else {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.error = null
//                    textInputLayout?.isErrorEnabled = false
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    textInputLayout.error = null
//                    textInputLayout.isErrorEnabled = false
//                } else {
//                    // Handle custom dropdown
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    if (container != null && textView != null) {
//                        // Restore appropriate background based on enabled state
//                        container.background = if (isFieldEnabled) {
//                            createRoundedBackground()
//                        } else {
//                            createDisabledBackground()
//                        }
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup,
//            is InputFieldType.CheckboxGroup -> {
//                // Reset title color to default
//                titleTextView.setTextColor(resolveColorAttribute(R.attr.colorForegroundSecondary, R.color.colorNeutral60))
//            }
//            else -> {
//                // Handle null case - do nothing
//            }
//        }
//    }
//
//    // Helper method to get text input components (container and EditText)
//    private fun getTextInputComponents(): Pair<LinearLayout?, EditText?> {
//        val outerContainer = currentInputComponent as? LinearLayout ?: return Pair(null, null)
//
//        // Check if it's a counter container (has 2 children)
//        if (outerContainer.childCount == 2) {
//            val innerContainer = outerContainer.getChildAt(0) as? LinearLayout
//            val editText = innerContainer?.tag as? EditText
//            return Pair(innerContainer, editText)
//        } else {
//            // Direct container
//            val editText = outerContainer.tag as? EditText
//            return Pair(outerContainer, editText)
//        }
//    }
//
//    // Add getter methods for XML attributes
//    fun getErrorText(): String? = currentErrorText
//    fun isFieldEnabled(): Boolean = isFieldEnabled
//
//    private fun setupInputComponent(config: InputFieldConfig) {
//        // Remove existing component
//        inputContainer.removeAllViews()
//        currentInputComponent = null
//
//        // Create new component based on type
//        currentInputComponent = when (config.type) {
//            is InputFieldType.TextInput -> createTextInput(config)
//            is InputFieldType.TextArea -> createTextArea(config)
//            is InputFieldType.Dropdown -> createCustomDropdownView(config)
//            is InputFieldType.RadioGroup -> createRadioGroup(config)
//            is InputFieldType.CheckboxGroup -> createCheckboxGroup(config)
//        }
//
//        // Add to container
//        currentInputComponent?.let { component ->
//            inputContainer.addView(component, FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT
//            ))
//        }
//    }
//
//    private fun createTextInput(config: InputFieldConfig): View {
//        // Create a container that matches the dropdown design
//        val container = LinearLayout(context).apply {
//            background = createRoundedBackground()
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.CENTER_VERTICAL
//            setPadding(
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (2 * context.resources.displayMetrics.density).toInt(),
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (2 * context.resources.displayMetrics.density).toInt()
//            )
//            minimumHeight = (48 * context.resources.displayMetrics.density).toInt()
//            isFocusable = false
//            isClickable = false
//        }
//
//        // Create the EditText
//        val editText = EditText(context).apply {
//            hint = config.hint
//            setTextAppearance(R.style.TextRegular_Paragraph1)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//            setHintTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            background = null
//            textSize = 16f
//
//            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
//            imeOptions = EditorInfo.IME_ACTION_NEXT
//
//            if (config.maxLength > 0) {
//                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
//            }
//
//            // Handle focus changes to update container background
//            setOnFocusChangeListener { _, hasFocus ->
//                updateInputBackground(container, this, hasFocus)
//            }
//
//            doAfterTextChanged { editable ->
//                notifyValueChange(editable?.toString())
//                notifyValidationChange()
//            }
//
//            layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f
//            )
//        }
//
//        // Add character counter if needed
//        if (config.maxLength > 0) {
//            val counterContainer = LinearLayout(context).apply {
//                orientation = LinearLayout.VERTICAL
//            }
//
//            val counterText = TextView(context).apply {
//                setTextAppearance(R.style.TextRegular_Label4)
//                setTextColor(resolveColorAttribute(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
//                textSize = 12f
//                gravity = Gravity.END
//
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    topMargin = (4 * context.resources.displayMetrics.density).toInt()
//                }
//            }
//
//            // Update counter text
//            val updateCounter = {
//                val currentLength = editText.text?.length ?: 0
//                counterText.text = "$currentLength/${config.maxLength}"
//            }
//
//            updateCounter()
//
//            editText.doAfterTextChanged {
//                updateCounter()
//            }
//
//            counterContainer.addView(container)
//            counterContainer.addView(counterText)
//            container.addView(editText)
//            return counterContainer
//        }
//
//        container.addView(editText)
//        container.tag = editText
//        return container
//    }
//
//    private fun createTextArea(config: InputFieldConfig): View {
//        val container = LinearLayout(context).apply {
//            background = createRoundedBackground()
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.TOP
//            setPadding(
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (8 * context.resources.displayMetrics.density).toInt(),
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (8 * context.resources.displayMetrics.density).toInt()
//            )
//            minimumHeight = (config.minLines * 24 * context.resources.displayMetrics.density).toInt()
//            isFocusable = false
//            isClickable = false
//        }
//
//        val editText = EditText(context).apply {
//            hint = config.hint
//            setTextAppearance(R.style.TextRegular_Paragraph1)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//            setHintTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            background = null
//            textSize = 16f
//            gravity = Gravity.TOP
//
//            inputType = android.text.InputType.TYPE_CLASS_TEXT or
//                    android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE or
//                    android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
//
//            imeOptions = EditorInfo.IME_ACTION_DONE
//            setLines(config.minLines)
//            maxLines = config.maxLines
//
//            if (config.maxLength > 0) {
//                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
//            }
//
//            setOnFocusChangeListener { _, hasFocus ->
//                updateInputBackground(container, this, hasFocus)
//            }
//
//            doAfterTextChanged { editable ->
//                notifyValueChange(editable?.toString())
//                notifyValidationChange()
//            }
//
//            layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f
//            )
//        }
//
//        if (config.maxLength > 0) {
//            val counterContainer = LinearLayout(context).apply {
//                orientation = LinearLayout.VERTICAL
//            }
//
//            val counterText = TextView(context).apply {
//                setTextAppearance(R.style.TextRegular_Label4)
//                setTextColor(resolveColorAttribute(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
//                textSize = 12f
//                gravity = Gravity.END
//
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    topMargin = (4 * context.resources.displayMetrics.density).toInt()
//                }
//            }
//
//            val updateCounter = {
//                val currentLength = editText.text?.length ?: 0
//                counterText.text = "$currentLength/${config.maxLength}"
//            }
//
//            updateCounter()
//
//            editText.doAfterTextChanged {
//                updateCounter()
//            }
//
//            counterContainer.addView(container)
//            counterContainer.addView(counterText)
//            container.addView(editText)
//            return counterContainer
//        }
//
//        container.addView(editText)
//        container.tag = editText
//        return container
//    }
//
//    private fun createFocusedBackground(): Drawable {
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.cornerRadius = 12f * context.resources.displayMetrics.density
//        shape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        shape.setStroke(
//            (2 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeAccent, R.color.colorPrimary30)
//        )
//        return shape
//    }
//
//    private fun createDisabledBackground(): Drawable {
//        val cornerRadius = 12f * context.resources.displayMetrics.density
//
//        val baseShape = GradientDrawable().apply {
//            shape = GradientDrawable.RECTANGLE
//            this.cornerRadius = cornerRadius
//            setColor(resolveColorAttribute(R.attr.colorBackgroundDisabled, R.color.color000Opacity5))
//            setStroke(
//                (1 * context.resources.displayMetrics.density).toInt(),
//                resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
//            )
//        }
//
//        val overlayShape = GradientDrawable().apply {
//            shape = GradientDrawable.RECTANGLE
//            this.cornerRadius = cornerRadius
//            setColor(resolveColorAttribute(R.attr.colorBackgroundModifierCardElevated, R.color.colorOpacityWhite5))
//        }
//
//        val layerDrawable = LayerDrawable(arrayOf(baseShape, overlayShape))
//        return layerDrawable
//    }
//
//    private fun updateInputBackground(container: LinearLayout, editText: EditText, hasFocus: Boolean) {
//        container.background = when {
//            !currentErrorText.isNullOrEmpty() -> createErrorBackground()
//            !editText.isEnabled -> createDisabledBackground()
//            hasFocus -> createFocusedBackground()
//            else -> createRoundedBackground()
//        }
//    }
//
//    private fun createErrorBackground(): Drawable {
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.cornerRadius = 12f * context.resources.displayMetrics.density
//        shape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        shape.setStroke(
//            (1 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeAttentionIntense, R.color.colorRed50)
//        )
//        return shape
//    }
//
////    private fun createStrokeColorStateList(): ColorStateList {
////        val states = arrayOf(
////            intArrayOf(android.R.attr.state_focused),
////            intArrayOf(-android.R.attr.state_focused)
////        )
////
////        val colors = intArrayOf(
////            ContextCompat.getColor(context, android.R.color.darker_gray),
////            ContextCompat.getColor(context, android.R.color.darker_gray)
////        )
////
////        return ColorStateList(states, colors)
////    }
//
//    private fun createCustomDropdownView(config: InputFieldConfig): View {
//        val container = LinearLayout(context).apply {
//            background = createRoundedBackground()
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.CENTER_VERTICAL
//            setPadding(
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (10 * context.resources.displayMetrics.density).toInt(),
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (10 * context.resources.displayMetrics.density).toInt()
//            )
//
//            minimumHeight = (40 * context.resources.displayMetrics.density).toInt()
//            isFocusable = true
//            isClickable = true
//        }
//
//        val textView = TextView(context).apply {
//            text = config.hint ?: "Select option"
//            setTextAppearance(R.style.TextRegular_Paragraph1)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            gravity = Gravity.CENTER_VERTICAL
//            textSize = 16f
//
//            layoutParams = LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f
//            )
//        }
//
//        val iconView = ImageView(context).apply {
//            setImageResource(R.drawable.placeholder)
//            setColorFilter(resolveColorAttribute(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
//            scaleType = ImageView.ScaleType.CENTER
//
//            layoutParams = FrameLayout.LayoutParams(
//                (24 * context.resources.displayMetrics.density).toInt(),
//                (24 * context.resources.displayMetrics.density).toInt(),
//                Gravity.CENTER_VERTICAL or Gravity.END
//            )
//        }
//
//        container.addView(textView)
//        container.addView(iconView)
//
//        container.setOnClickListener {
//            showOptionDrawer(config.options) { selectedOption ->
//                textView.text = selectedOption
//                textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//
//                notifyValueChange(selectedOption)
//                notifyValidationChange()
//            }
//        }
//
//        container.tag = textView
//        return container
//    }
//
//    private fun createRoundedBackground(): Drawable {
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.cornerRadius = 12f * context.resources.displayMetrics.density
//        shape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        shape.setStroke(
//            (1 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
//        )
//
//        val focusedShape = GradientDrawable()
//        focusedShape.shape = GradientDrawable.RECTANGLE
//        focusedShape.cornerRadius = 12f * context.resources.displayMetrics.density
//        focusedShape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        focusedShape.setStroke(
//            (1 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeAccent, R.color.colorPrimary30)
//        )
//
//        val stateListDrawable = StateListDrawable()
//        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusedShape)
//        stateListDrawable.addState(intArrayOf(), shape)
//
//        return stateListDrawable
//    }
//
//    private fun showOptionDrawer(options: List<String>?, onSelection: ((String) -> Unit)? = null) {
//        if (options.isNullOrEmpty()) return
//
//        val bottomSheetDialog = BottomSheetDialog(context)
//        val bottomSheetBinding = createOptionDrawerLayout(options) { selectedOption ->
//            if (onSelection != null) {
//                onSelection(selectedOption)
//            } else {
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                val autoCompleteTextView = textInputLayout?.editText as? MaterialAutoCompleteTextView
//                autoCompleteTextView?.setText(selectedOption, false)
//            }
//
//            notifyValueChange(selectedOption)
//            notifyValidationChange()
//
//            bottomSheetDialog.dismiss()
//        }
//
//        bottomSheetDialog.setContentView(bottomSheetBinding)
//        bottomSheetDialog.show()
//    }
//
//    private fun createOptionDrawerLayout(
//        options: List<String>,
//        onOptionSelected: (String) -> Unit
//    ): View {
//        val mainContainer = LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//            setPadding(16, 24, 16, 16)
//        }
//
//        val titleTextView = TextView(context).apply {
//            text = currentConfig?.hint
//            setTextAppearance(R.style.TextSemiBold_Heading1)
//            setPadding(16, 8, 16, 24)
//        }
//        mainContainer.addView(titleTextView)
//
//        val scrollView = ScrollView(context)
//        val optionsContainer = LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//        }
//
//        options.forEach { option ->
//            val optionCard = OptionCard(context).apply {
//                titleText = option
//
//                delegate = object : OptionCardDelegate {
//                    override fun onClick(card: OptionCard) {
//                        Log.d("OptionCard", "${card.titleText} selected")
//                        onOptionSelected(option)
//                    }
//                }
//
//                val layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    bottomMargin = (8 * context.resources.displayMetrics.density).toInt()
//                }
//                this.layoutParams = layoutParams
//            }
//
//            optionsContainer.addView(optionCard)
//        }
//
//        scrollView.addView(optionsContainer)
//        mainContainer.addView(scrollView)
//
//        return mainContainer
//    }
//
//    private fun createRadioGroup(config: InputFieldConfig): View {
//        return CustomRadioGroup(context).apply {
//            config.options?.let { options ->
//                setData(options) { option -> option }
//
//                val spacingInDp = 8
//                val spacingInPx = (spacingInDp * context.resources.displayMetrics.density).toInt()
//
//                for (i in 0 until childCount) {
//                    val child = getChildAt(i)
//                    val layoutParams = child.layoutParams as? MarginLayoutParams
//                    layoutParams?.let { params ->
//                        if (i > 0) {
//                            params.topMargin = spacingInPx
//                        }
//                        if (i < childCount - 1) {
//                            params.bottomMargin = spacingInPx / 2
//                        }
//                        child.layoutParams = params
//                    }
//                }
//
//                setOnItemSelectedListener(object : CustomRadioGroupDelegate {
//                    override fun onItemSelected(position: Int, data: Any?) {
//                        notifyValueChange(data?.toString())
//                        notifyValidationChange()
//                    }
//                })
//            }
//        }
//    }
//
//    private fun createCheckboxGroup(config: InputFieldConfig): View {
//        return LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//
//            config.options?.forEach { option ->
//                val checkbox = CustomCheckBox(context).apply {
//                    text = option
//                    setCustomCheckBoxDelegate(object : CustomCheckboxDelegate {
//                        override fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean) {
//                            notifyValueChange(getCheckboxGroupValue(this@InputField))
//                            notifyValidationChange()
//                        }
//                    })
//                }
//                addView(checkbox)
//            }
//        }
//    }
//
//    private fun getCheckboxGroupValue(container: LinearLayout?): List<String> {
//        if (container == null) return emptyList()
//
//        val selectedValues = mutableListOf<String>()
//        for (i in 0 until container.childCount) {
//            val checkbox = container.getChildAt(i) as? CustomCheckBox
//            if (checkbox?.isChecked == true) {
//                selectedValues.add(checkbox.text.toString())
//            }
//        }
//        return selectedValues
//    }
//
//    fun getValue(): Any? {
//        return when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> {
//                val (_, editText) = getTextInputComponents()
//                editText?.text?.toString() ?: run {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.editText?.text?.toString()
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    val autoComplete = textInputLayout.editText as? AutoCompleteTextView
//                    autoComplete?.text?.toString()?.takeIf { it.isNotEmpty() }
//                } else {
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    textView?.text?.toString()?.takeIf {
//                        it != currentConfig?.hint && it.isNotEmpty()
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup -> {
//                val radioGroup = currentInputComponent as? CustomRadioGroup
//                radioGroup?.getSelectedData()?.toString()
//            }
//            is InputFieldType.CheckboxGroup -> {
//                getCheckboxGroupValue(currentInputComponent as? LinearLayout)
//            }
//            else -> null
//        }
//    }
//
//    fun setValue(value: Any?) {
//        when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> {
//                val (_, editText) = getTextInputComponents()
//                if (editText != null) {
//                    editText.setText(value?.toString())
//                } else {
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.editText?.setText(value?.toString())
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    val autoComplete = textInputLayout.editText as? AutoCompleteTextView
//                    autoComplete?.setText(value?.toString(), false)
//                } else {
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    if (textView != null && value != null) {
//                        textView.text = value.toString()
//                        textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup -> {
//                val radioGroup = currentInputComponent as? CustomRadioGroup
//                val valueStr = value?.toString()
//                if (valueStr != null) {
//                    radioGroup?.selectItemByData(valueStr)
//                }
//            }
//            is InputFieldType.CheckboxGroup -> {
//                val container = currentInputComponent as? LinearLayout
//                val selectedValues = value as? List<String> ?: emptyList()
//                for (i in 0 until (container?.childCount ?: 0)) {
//                    val checkbox = container?.getChildAt(i) as? CustomCheckBox
//                    checkbox?.isChecked = selectedValues.contains(checkbox?.text?.toString())
//                }
//            }
//            else -> {
//                // Handle null case - do nothing
//            }
//        }
//    }
//
//    fun isValid(): Boolean {
//        if (!isFieldRequired) return true
//
//        return when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> !getValue().toString().isNullOrBlank()
//            is InputFieldType.Dropdown,
//            is InputFieldType.RadioGroup -> getValue() != null
//            is InputFieldType.CheckboxGroup -> (getValue() as? List<*>)?.isNotEmpty() == true
//            else -> false
//        }
//    }
//
//    private fun notifyValueChange(value: Any?) {
//        delegate?.onValueChange(fieldId, value)
//    }
//
//    private fun notifyValidationChange() {
//        delegate?.onValidationChange(fieldId, isValid())
//    }
//
//    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
//        val typedValue = TypedValue()
//        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
//            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
//                ContextCompat.getColor(context, typedValue.resourceId)
//            } else {
//                typedValue.data
//            }
//        } else {
//            ContextCompat.getColor(context, fallbackColor)
//        }
//    }
//}




//class InputField @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : LinearLayout(context, attrs, defStyleAttr) {
//
//    var fieldId: String = ""
//    var delegate: InputFieldDelegate? = null
//    var isFieldRequired = false
//        private set
//
//    private val titleTextView: TextView
//    private val descriptionTextView: TextView
//    private val inputContainer: FrameLayout
//    private var currentInputComponent: View? = null
//    private var currentConfig: InputFieldConfig? = null
//
//    // XML attributes
//    private var requiredColor: Int = R.color.colorRed30
//    private var errorColor: Int = R.color.colorRed50
//
//    init {
//        orientation = VERTICAL
//
//        // Title TextView
//        titleTextView = TextView(context).apply {
//            setTextAppearance(R.style.TextMedium_Label2)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundSecondary, R.color.colorNeutral60))
//            setPadding(0, 0, 0, (4 * context.resources.displayMetrics.density).toInt())
//        }
//
//        // Description TextView
//        descriptionTextView = TextView(context).apply {
//            setTextAppearance(R.style.TextRegular_Label4)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            setPadding(0, 0, 0, (8 * context.resources.displayMetrics.density).toInt())
//            visibility = View.GONE
//        }
//
//        // Input Container
//        inputContainer = FrameLayout(context)
//
//        // Add views to layout
//        addView(titleTextView)
//        addView(descriptionTextView)
//        addView(inputContainer)
//
//        // Parse XML attributes if provided
//        attrs?.let { parseAttributes(it, defStyleAttr) }
//    }
//
//    private fun parseAttributes(attrs: AttributeSet, defStyleAttr: Int) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputField, defStyleAttr, 0)
//
//        try {
//            // Get field configuration from XML
//            val title = typedArray.getString(R.styleable.InputField_fieldTitle) ?: ""
//            val description = typedArray.getString(R.styleable.InputField_fieldDescription)
//            val hint = typedArray.getString(R.styleable.InputField_fieldHint)
//            val required = typedArray.getBoolean(R.styleable.InputField_fieldRequired, false)
//            val inputTypeValue = typedArray.getInt(R.styleable.InputField_inputType, 0)
//            val maxLength = typedArray.getInt(R.styleable.InputField_maxLength, 0)
//            val minLength = typedArray.getInt(R.styleable.InputField_minLength, 0)
//            val maxLines = typedArray.getInt(R.styleable.InputField_maxLines, 4)
//            val minLines = typedArray.getInt(R.styleable.InputField_minLines, 3)
//
//            // Get options array if provided
//            val optionsArrayId = typedArray.getResourceId(R.styleable.InputField_options, -1)
//            val options = if (optionsArrayId != -1) {
//                context.resources.getStringArray(optionsArrayId).toList()
//            } else null
//
//            // Get styling attributes
////            requiredColor = resolveColorAttribute(
////                typedArray.getResourceId(R.styleable.InputField_requiredColor, -1),
////                R.color.colorRed30
////            )
////
////            errorColor = resolveColorAttribute(
////                typedArray.getResourceId(R.styleable.InputField_errorColor, -1),
////                R.color.colorRed50
////            )
//
//            // Apply text appearances if specified
////            val titleTextAppearance = typedArray.getResourceId(R.styleable.InputField_titleTextAppearance, -1)
////            if (titleTextAppearance != -1) {
////                titleTextView.setTextAppearance(titleTextAppearance)
////            }
////
////            val descriptionTextAppearance = typedArray.getResourceId(R.styleable.InputField_descriptionTextAppearance, -1)
////            if (descriptionTextAppearance != -1) {
////                descriptionTextView.setTextAppearance(descriptionTextAppearance)
////            }
//
//            // Convert inputType enum to InputFieldType
//            val inputFieldType = when (inputTypeValue) {
//                0 -> InputFieldType.TextInput
//                1 -> InputFieldType.TextArea
//                2 -> InputFieldType.Dropdown
//                3 -> InputFieldType.RadioGroup
//                4 -> InputFieldType.CheckboxGroup
//                else -> InputFieldType.TextInput
//            }
//
//            // Create config and configure the field
//            if (title.isNotEmpty()) {
//                val config = InputFieldConfig(
//                    type = inputFieldType,
//                    title = title,
//                    description = description,
//                    hint = hint,
//                    isRequired = required,
//                    options = options,
//                    maxLines = maxLines,
//                    minLines = minLines,
//                    maxLength = maxLength,
//                    minLength = minLength
//                )
//                configure(config)
//            }
//
//        } finally {
//            typedArray.recycle()
//        }
//    }
//
//    fun configure(config: InputFieldConfig, id: String = "") {
//        this.fieldId = id
//        this.currentConfig = config
//        this.isFieldRequired = config.isRequired
//
//        setTitle(config.title)
//        config.description?.let { setDescription(it) }
//
//        setupInputComponent(config)
//    }
//
//    private fun setTitle(title: String) {
//        if (!isInEditMode) {
//            titleTextView.text = if (isFieldRequired) {
//                // Simple approach: append red asterisk using SpannableString
//                val spannableTitle = SpannableString("$title *")
//                spannableTitle.setSpan(
//                    ForegroundColorSpan(resources.getColor(requiredColor)),
//                    title.length + 1, // start of asterisk
//                    spannableTitle.length, // end of string
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                spannableTitle
//            } else {
//                title
//            }
//        } else {
//            titleTextView.text = title
//        }
//    }
//
//    private fun setDescription(description: String) {
//        descriptionTextView.text = description
//        descriptionTextView.visibility = if (description.isNotEmpty()) View.VISIBLE else View.GONE
//    }
//
//    private fun setupInputComponent(config: InputFieldConfig) {
//        // Remove existing component
//        inputContainer.removeAllViews()
//        currentInputComponent = null
//
//        // Create new component based on type
//        currentInputComponent = when (config.type) {
//            is InputFieldType.TextInput -> createTextInput(config)
//            is InputFieldType.TextArea -> createTextArea(config)
//            is InputFieldType.Dropdown -> createCustomDropdownView(config)
//            is InputFieldType.RadioGroup -> createRadioGroup(config)
//            is InputFieldType.CheckboxGroup -> createCheckboxGroup(config)
//        }
//
//        // Add to container
//        currentInputComponent?.let { component ->
//            inputContainer.addView(component, FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT
//            ))
//        }
//    }
//
//    private fun createTextInput(config: InputFieldConfig): View {
//        // Create a container that matches the dropdown design
//        val container = LinearLayout(context).apply {
//            background = createRoundedBackground()
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.CENTER_VERTICAL
//            setPadding(
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (2 * context.resources.displayMetrics.density).toInt(),
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (2 * context.resources.displayMetrics.density).toInt()
//            )
//            minimumHeight = (48 * context.resources.displayMetrics.density).toInt()
//            isFocusable = false
//            isClickable = false
//        }
//
//        // Create the EditText
//        val editText = EditText(context).apply {
//            hint = config.hint
//            setTextAppearance(R.style.TextRegular_Paragraph1)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//            setHintTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            background = null // Remove default background
//            textSize = 16f
//
//            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
//            imeOptions = EditorInfo.IME_ACTION_NEXT
//
//            if (config.maxLength > 0) {
//                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
//            }
//
//            // Handle focus changes to update container background
//            setOnFocusChangeListener { _, hasFocus ->
//                updateInputBackground(container, this, hasFocus)
//            }
//
//            doAfterTextChanged { editable ->
//                notifyValueChange(editable?.toString())
//                notifyValidationChange()
//            }
//
//            layoutParams = LinearLayout.LayoutParams(
//                0, // Use weight
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f // Take remaining space
//            )
//        }
//
//        // Add character counter if needed
//        if (config.maxLength > 0) {
//            val counterContainer = LinearLayout(context).apply {
//                orientation = LinearLayout.VERTICAL
//            }
//
//            val counterText = TextView(context).apply {
//                setTextAppearance(R.style.TextRegular_Label4)
//                setTextColor(resolveColorAttribute(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
//                textSize = 12f
//                gravity = Gravity.END
//
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    topMargin = (4 * context.resources.displayMetrics.density).toInt()
//                }
//            }
//
//            // Update counter text
//            val updateCounter = {
//                val currentLength = editText.text?.length ?: 0
//                counterText.text = "$currentLength/${config.maxLength}"
//            }
//
//            updateCounter() // Initial update
//
//            editText.doAfterTextChanged {
//                updateCounter()
//            }
//
//            counterContainer.addView(container)
//            counterContainer.addView(counterText)
//            container.addView(editText)
//            return counterContainer
//        }
//
//        container.addView(editText)
//
//        // Store reference for getting/setting value
//        container.tag = editText
//
//        return container
//    }
//
//    private fun createTextArea(config: InputFieldConfig): View {
//        // Create a container that matches the dropdown design but taller
//        val container = LinearLayout(context).apply {
//            background = createRoundedBackground()
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.TOP // Align to top for text area
//            setPadding(
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (8 * context.resources.displayMetrics.density).toInt(),
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (8 * context.resources.displayMetrics.density).toInt()
//            )
//            minimumHeight = (config.minLines * 24 * context.resources.displayMetrics.density).toInt()
//            isFocusable = false
//            isClickable = false
//        }
//
//        // Create the EditText
//        val editText = EditText(context).apply {
//            hint = config.hint
//            setTextAppearance(R.style.TextRegular_Paragraph1)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//            setHintTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            background = null // Remove default background
//            textSize = 16f
//            gravity = Gravity.TOP
//
//            inputType = android.text.InputType.TYPE_CLASS_TEXT or
//                    android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE or
//                    android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
//
//            imeOptions = EditorInfo.IME_ACTION_DONE
//            setLines(config.minLines)
//            maxLines = config.maxLines
//
//            if (config.maxLength > 0) {
//                filters = arrayOf(InputFilter.LengthFilter(config.maxLength))
//            }
//
//            // Handle focus changes to update container background
//            setOnFocusChangeListener { _, hasFocus ->
//                updateInputBackground(container, this, hasFocus)
//            }
//
//            doAfterTextChanged { editable ->
//                notifyValueChange(editable?.toString())
//                notifyValidationChange()
//            }
//
//            layoutParams = LinearLayout.LayoutParams(
//                0, // Use weight
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f // Take remaining space
//            )
//        }
//
//        // Add character counter if needed
//        if (config.maxLength > 0) {
//            val counterContainer = LinearLayout(context).apply {
//                orientation = LinearLayout.VERTICAL
//            }
//
//            val counterText = TextView(context).apply {
//                setTextAppearance(R.style.TextRegular_Label4)
//                setTextColor(resolveColorAttribute(R.attr.colorForegroundTertiary, R.color.colorNeutral50))
//                textSize = 12f
//                gravity = Gravity.END
//
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    topMargin = (4 * context.resources.displayMetrics.density).toInt()
//                }
//            }
//
//            // Update counter text
//            val updateCounter = {
//                val currentLength = editText.text?.length ?: 0
//                counterText.text = "$currentLength/${config.maxLength}"
//            }
//
//            updateCounter() // Initial update
//
//            editText.doAfterTextChanged {
//                updateCounter()
//            }
//
//            counterContainer.addView(container)
//            counterContainer.addView(counterText)
//            container.addView(editText)
//            return counterContainer
//        }
//
//        container.addView(editText)
//
//        // Store reference for getting/setting value
//        container.tag = editText
//
//        return container
//    }
//
//    // Add this helper method for focused state
//    private fun createFocusedBackground(): Drawable {
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.cornerRadius = 12f * context.resources.displayMetrics.density
//        shape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        shape.setStroke(
//            (2 * context.resources.displayMetrics.density).toInt(), // Slightly thicker stroke when focused
//            resolveColorAttribute(R.attr.colorStrokeAccent, R.color.colorPrimary30)
//        )
//        return shape
//    }
//
//    // Add helper method for disabled state
//    private fun createDisabledBackground(): Drawable {
//        val cornerRadius = 12f * context.resources.displayMetrics.density
//
//        // Base layer - the card-elevated background
//        val baseShape = GradientDrawable().apply {
//            shape = GradientDrawable.RECTANGLE
//            this.cornerRadius = cornerRadius
//            setColor(resolveColorAttribute(R.attr.colorBackgroundDisabled, R.color.color000Opacity5))
//            setStroke(
//                (1 * context.resources.displayMetrics.density).toInt(),
//                resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
//            )
//        }
//
//        // Overlay layer - the disabled overlay
//        val overlayShape = GradientDrawable().apply {
//            shape = GradientDrawable.RECTANGLE
//            this.cornerRadius = cornerRadius
//            setColor(resolveColorAttribute(R.attr.colorBackgroundModifierCardElevated, R.color.colorOpacityWhite5))
//        }
//
//        // Create LayerDrawable with both layers
//        val layerDrawable = LayerDrawable(arrayOf(baseShape, overlayShape))
//        return layerDrawable
//    }
//    // Add helper method to update input background based on state
//    private fun updateInputBackground(container: LinearLayout, editText: EditText, hasFocus: Boolean) {
//        container.background = when {
//            !editText.isEnabled -> createDisabledBackground()
//            hasFocus -> createFocusedBackground()
//            else -> createRoundedBackground()
//        }
//    }
//
//    // Add enabled/disabled state management
//    private var isEnabled: Boolean = true
//
//    override fun setEnabled(enabled: Boolean) {
//        isEnabled = enabled
//
//        when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> {
//                // Check if it's the new custom styled input
//                val outerContainer = currentInputComponent as? LinearLayout
//                val container = if (outerContainer?.childCount == 2) { // Has counter
//                    outerContainer.getChildAt(0) as? LinearLayout
//                } else {
//                    outerContainer
//                }
//
//                val editText = container?.tag as? EditText
//                if (editText != null) {
//                    editText.isEnabled = enabled
//
//                    // Update text colors based on enabled state
//                    if (enabled) {
//                        editText.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//                        editText.setHintTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//                    } else {
//                        editText.setTextColor(resolveColorAttribute(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
//                        editText.setHintTextColor(resolveColorAttribute(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
//                    }
//
//                    // Update container background
//                    updateInputBackground(container, editText, editText.hasFocus() && enabled)
//                } else {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.isEnabled = enabled
//                    textInputLayout?.editText?.isEnabled = enabled
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                // Handle both old TextInputLayout and new custom dropdown
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    textInputLayout.isEnabled = enabled
//                    textInputLayout.editText?.isEnabled = enabled
//                } else {
//                    // Handle custom dropdown
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    container?.isEnabled = enabled
//                    container?.isClickable = enabled
//
//                    if (textView != null) {
//                        if (enabled) {
//                            textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//                            container.background = createRoundedBackground()
//                        } else {
//                            textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundDisabled, R.color.colorNeutral30))
//                            container.background = createDisabledBackground()
//                        }
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup -> {
//                val radioGroup = currentInputComponent as? CustomRadioGroup
//                // Assuming CustomRadioGroup has its own enabled state handling
//                for (i in 0 until (radioGroup?.childCount ?: 0)) {
//                    radioGroup?.getChildAt(i)?.isEnabled = enabled
//                }
//            }
//            is InputFieldType.CheckboxGroup -> {
//                val container = currentInputComponent as? LinearLayout
//                for (i in 0 until (container?.childCount ?: 0)) {
//                    val checkbox = container?.getChildAt(i) as? CustomCheckBox
//                    checkbox?.isEnabled = enabled
//                }
//            }
//            else -> {
//                // Handle null case - do nothing
//            }
//        }
//    }
//
//    // Add error state handling with disabled state consideration
//    fun setError(errorText: String?) {
//        if (!errorText.isNullOrEmpty()) {
//            when (currentConfig?.type) {
//                is InputFieldType.TextInput,
//                is InputFieldType.TextArea -> {
//                    // Check if it's the new custom styled input
//                    val outerContainer = currentInputComponent as? LinearLayout
//                    val container = if (outerContainer?.childCount == 2) { // Has counter
//                        outerContainer.getChildAt(0) as? LinearLayout
//                    } else {
//                        outerContainer
//                    }
//
//                    val editText = container?.tag as? EditText
//                    if (editText != null && container != null) {
//                        // Create error background
//                        container.background = createErrorBackground()
//
//                        // You could also show error text below the field
//                        // This would require adding error TextView to your layout
//                    } else {
//                        // Fallback to old TextInputLayout structure
//                        val textInputLayout = currentInputComponent as? TextInputLayout
//                        textInputLayout?.error = errorText
//                        textInputLayout?.isErrorEnabled = true
//                    }
//                }
//                is InputFieldType.Dropdown -> {
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    if (textInputLayout != null) {
//                        textInputLayout.error = errorText
//                        textInputLayout.isErrorEnabled = true
//                    } else {
//                        // Handle custom dropdown error state
//                        val container = currentInputComponent as? LinearLayout
//                        container?.background = createErrorBackground()
//                    }
//                }
//                is InputFieldType.RadioGroup,
//                is InputFieldType.CheckboxGroup -> {
//                    titleTextView.setTextColor(resources.getColor(errorColor))
//                }
//                else -> {
//                    // Handle null case - do nothing
//                }
//            }
//        } else {
//            clearError()
//        }
//    }
//
//    // Add error background creation
//    private fun createErrorBackground(): Drawable {
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.cornerRadius = 12f * context.resources.displayMetrics.density
//        shape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        shape.setStroke(
//            (1 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeAttentionIntense, R.color.colorRed50)
//        )
//        return shape
//    }
//
//    // Update clearError to handle disabled state
//    fun clearError() {
//        when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> {
//                // Check if it's the new custom styled input
//                val outerContainer = currentInputComponent as? LinearLayout
//                val container = if (outerContainer?.childCount == 2) { // Has counter
//                    outerContainer.getChildAt(0) as? LinearLayout
//                } else {
//                    outerContainer
//                }
//
//                val editText = container?.tag as? EditText
//                if (editText != null && container != null) {
//                    // Restore appropriate background based on current state
//                    updateInputBackground(container, editText, editText.hasFocus())
//                } else {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.error = null
//                    textInputLayout?.isErrorEnabled = false
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    textInputLayout.error = null
//                    textInputLayout.isErrorEnabled = false
//                } else {
//                    // Handle custom dropdown
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    if (container != null && textView != null) {
//                        // Restore appropriate background based on enabled state
//                        container.background = if (isEnabled) {
//                            createRoundedBackground()
//                        } else {
//                            createDisabledBackground()
//                        }
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup,
//            is InputFieldType.CheckboxGroup -> {
//                // Reset title color to default
//                titleTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
//            }
//            else -> {
//                // Handle null case - do nothing
//            }
//        }
//    }
//
//    // Update the getValue method to handle the new structure
//    fun getValue(): Any? {
//        return when (currentConfig?.type) {
//            is InputFieldType.TextInput -> {
//                // Check if it's the new custom styled input
//                val container = currentInputComponent as? LinearLayout
//                val editText = container?.tag as? EditText
//                editText?.text?.toString() ?: run {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.editText?.text?.toString()
//                }
//            }
//            is InputFieldType.TextArea -> {
//                // Check if it's the new custom styled input
//                val outerContainer = currentInputComponent as? LinearLayout
//                val container = outerContainer?.getChildAt(0) as? LinearLayout
//                val editText = container?.tag as? EditText
//                editText?.text?.toString() ?: run {
//                    // If it's a counter container, get the inner container
//                    val innerContainer = outerContainer?.getChildAt(0) as? LinearLayout
//                    val innerEditText = innerContainer?.tag as? EditText
//                    innerEditText?.text?.toString() ?: run {
//                        // Fallback to old TextInputLayout structure
//                        val textInputLayout = currentInputComponent as? TextInputLayout
//                        textInputLayout?.editText?.text?.toString()
//                    }
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                // Handle both old TextInputLayout and new custom dropdown
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    val autoComplete = textInputLayout.editText as? AutoCompleteTextView
//                    autoComplete?.text?.toString()?.takeIf { it.isNotEmpty() }
//                } else {
//                    // Handle custom dropdown
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    textView?.text?.toString()?.takeIf {
//                        it != currentConfig?.hint && it.isNotEmpty()
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup -> {
//                val radioGroup = currentInputComponent as? CustomRadioGroup
//                radioGroup?.getSelectedData()?.toString()
//            }
//            is InputFieldType.CheckboxGroup -> {
//                getCheckboxGroupValue(currentInputComponent as? LinearLayout)
//            }
//            else -> null
//        }
//    }
//
//    // Update the setValue method to handle the new structure
//    fun setValue(value: Any?) {
//        when (currentConfig?.type) {
//            is InputFieldType.TextInput -> {
//                // Check if it's the new custom styled input
//                val container = currentInputComponent as? LinearLayout
//                val editText = container?.tag as? EditText
//                if (editText != null) {
//                    editText.setText(value?.toString())
//                } else {
//                    // Fallback to old TextInputLayout structure
//                    val textInputLayout = currentInputComponent as? TextInputLayout
//                    textInputLayout?.editText?.setText(value?.toString())
//                }
//            }
//            is InputFieldType.TextArea -> {
//                // Check if it's the new custom styled input
//                val outerContainer = currentInputComponent as? LinearLayout
//                val container = outerContainer?.getChildAt(0) as? LinearLayout
//                val editText = container?.tag as? EditText
//                if (editText != null) {
//                    editText.setText(value?.toString())
//                } else {
//                    // If it's a counter container, get the inner container
//                    val innerContainer = outerContainer?.getChildAt(0) as? LinearLayout
//                    val innerEditText = innerContainer?.tag as? EditText
//                    if (innerEditText != null) {
//                        innerEditText.setText(value?.toString())
//                    } else {
//                        // Fallback to old TextInputLayout structure
//                        val textInputLayout = currentInputComponent as? TextInputLayout
//                        textInputLayout?.editText?.setText(value?.toString())
//                    }
//                }
//            }
//            is InputFieldType.Dropdown -> {
//                // Handle both old TextInputLayout and new custom dropdown
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                if (textInputLayout != null) {
//                    val autoComplete = textInputLayout.editText as? AutoCompleteTextView
//                    autoComplete?.setText(value?.toString(), false)
//                } else {
//                    // Handle custom dropdown
//                    val container = currentInputComponent as? LinearLayout
//                    val textView = container?.tag as? TextView
//                    if (textView != null && value != null) {
//                        textView.text = value.toString()
//                        textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//                    }
//                }
//            }
//            is InputFieldType.RadioGroup -> {
//                val radioGroup = currentInputComponent as? CustomRadioGroup
//                val valueStr = value?.toString()
//                if (valueStr != null) {
//                    radioGroup?.selectItemByData(valueStr)
//                }
//            }
//            is InputFieldType.CheckboxGroup -> {
//                val container = currentInputComponent as? LinearLayout
//                val selectedValues = value as? List<String> ?: emptyList()
//                for (i in 0 until (container?.childCount ?: 0)) {
//                    val checkbox = container?.getChildAt(i) as? CustomCheckBox
//                    checkbox?.isChecked = selectedValues.contains(checkbox?.text?.toString())
//                }
//            }
//            else -> {
//                // Handle null case - do nothing
//            }
//        }
//    }
//
//    private fun createStrokeColorStateList(): ColorStateList {
//        val states = arrayOf(
//            intArrayOf(android.R.attr.state_focused), // focused
//            intArrayOf(-android.R.attr.state_focused) // not focused
//        )
//
//        val colors = intArrayOf(
//            ContextCompat.getColor(context, android.R.color.darker_gray), // focused color
//            ContextCompat.getColor(context, android.R.color.darker_gray)  // normal color
//        )
//
//        return ColorStateList(states, colors)
//    }
//
//    private fun createCustomDropdownView(config: InputFieldConfig): View {
//        // Create a container that mimics the design
//        val container = LinearLayout(context).apply {
//            background = createRoundedBackground()
//            orientation = LinearLayout.HORIZONTAL
//            gravity = Gravity.CENTER_VERTICAL
//            setPadding(
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (10 * context.resources.displayMetrics.density).toInt(),
//                (12 * context.resources.displayMetrics.density).toInt(),
//                (10 * context.resources.displayMetrics.density).toInt()
//            )
//
//            minimumHeight = (40 * context.resources.displayMetrics.density).toInt()
//            isFocusable = true
//            isClickable = true
////            isFocusableInTouchMode = true
//        }
//
//        // Create the text view
//        val textView = TextView(context).apply {
//            text = config.hint ?: "Select option"
//            setTextAppearance(R.style.TextRegular_Paragraph1)
//            setTextColor(resolveColorAttribute(R.attr.colorForegroundPlaceholder, R.color.colorNeutral40))
//            gravity = Gravity.CENTER_VERTICAL
//            textSize = 16f
//
//            layoutParams = LinearLayout.LayoutParams(
//                0, // Use weight
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1f // Take remaining space
//            )
//        }
//
//        // Create dropdown icon
//        val iconView = ImageView(context).apply {
//            setImageResource(R.drawable.placeholder) // Use your dropdown arrow icon
//            setColorFilter(resolveColorAttribute(R.attr.colorForegroundTertiary, R.color.colorNeutral50)) // Match text color
//            scaleType = ImageView.ScaleType.CENTER
//
//            layoutParams = FrameLayout.LayoutParams(
//                (24 * context.resources.displayMetrics.density).toInt(),
//                (24 * context.resources.displayMetrics.density).toInt(),
//                Gravity.CENTER_VERTICAL or Gravity.END
//            )
//        }
//
//        container.addView(textView)
//        container.addView(iconView)
//
//        // Handle click
//        container.setOnClickListener {
//            showOptionDrawer(config.options) { selectedOption ->
//                textView.text = selectedOption
//                textView.setTextColor(resolveColorAttribute(R.attr.colorForegroundPrimary, R.color.color000))
//
//                // Update validation and notify changes
//                notifyValueChange(selectedOption)
//                notifyValidationChange()
//            }
//        }
//
//        // Store reference for getting/setting value
//        container.tag = textView
//
//        return container
//    }
//
//    private fun createRoundedBackground(): Drawable {
//        // Create normal state shape
//        val shape = GradientDrawable()
//        shape.shape = GradientDrawable.RECTANGLE
//        shape.cornerRadius = 12f * context.resources.displayMetrics.density
//        shape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        shape.setStroke(
//            (1 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
//        )
//
//        // Create focused state shape
//        val focusedShape = GradientDrawable()
//        focusedShape.shape = GradientDrawable.RECTANGLE
//        focusedShape.cornerRadius = 12f * context.resources.displayMetrics.density
//        focusedShape.setColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))
//        focusedShape.setStroke(
//            (1 * context.resources.displayMetrics.density).toInt(),
//            resolveColorAttribute(R.attr.colorStrokeAccent, R.color.colorPrimary30)
//        )
//
//        // Create state list drawable
//        val stateListDrawable = StateListDrawable()
//        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusedShape)
//        stateListDrawable.addState(intArrayOf(), shape)
//
//        return stateListDrawable
//    }
//
//    // Update the showOptionDrawer method to accept a callback for custom view
//    private fun showOptionDrawer(options: List<String>?, onSelection: ((String) -> Unit)? = null) {
//        if (options.isNullOrEmpty()) return
//
//        val bottomSheetDialog = BottomSheetDialog(context)
//        val bottomSheetBinding = createOptionDrawerLayout(options) { selectedOption ->
//            // Handle option selection for TextInputLayout version
//            if (onSelection != null) {
//                onSelection(selectedOption)
//            } else {
//                val textInputLayout = currentInputComponent as? TextInputLayout
//                val autoCompleteTextView = textInputLayout?.editText as? MaterialAutoCompleteTextView
//                autoCompleteTextView?.setText(selectedOption, false)
//            }
//
//            notifyValueChange(selectedOption)
//            notifyValidationChange()
//
//            bottomSheetDialog.dismiss()
//        }
//
//        bottomSheetDialog.setContentView(bottomSheetBinding)
//        bottomSheetDialog.show()
//    }
//
//    private fun createOptionDrawerLayout(
//        options: List<String>,
//        onOptionSelected: (String) -> Unit
//    ): View {
//        // Create main container
//        val mainContainer = LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//            setPadding(16, 24, 16, 16)
//        }
//
//        // Add title
//        val titleTextView = TextView(context).apply {
//            text = currentConfig?.hint
//            setTextAppearance(R.style.TextSemiBold_Heading1)
//            setPadding(16, 8, 16, 24)
//        }
//        mainContainer.addView(titleTextView)
//
//        // Create ScrollView for options
//        val scrollView = ScrollView(context)
//        val optionsContainer = LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//        }
//
//        // Add option cards
//        options.forEach { option ->
//            val optionCard = OptionCard(context).apply {
//                titleText = option
//                // Set icon if needed - you can customize this based on your requirements
//                // iconResource = R.drawable.your_icon
//
//                delegate = object : OptionCardDelegate {
//                    override fun onClick(card: OptionCard) {
//                        Log.d("OptionCard", "${card.titleText} selected")
//                        onOptionSelected(option)
//                    }
//                }
//
//                // Add some margin between cards
//                val layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    bottomMargin = (8 * context.resources.displayMetrics.density).toInt()
//                }
//                this.layoutParams = layoutParams
//            }
//
//            optionsContainer.addView(optionCard)
//        }
//
//        scrollView.addView(optionsContainer)
//        mainContainer.addView(scrollView)
//
//        return mainContainer
//    }
//
//    private fun createRadioGroup(config: InputFieldConfig): View {
//        return CustomRadioGroup(context).apply {
//            config.options?.let { options ->
//                setData(options) { option -> option }
//
//                // Set spacing after data is set
//                val spacingInDp = 8 // adjust as needed
//                val spacingInPx = (spacingInDp * context.resources.displayMetrics.density).toInt()
//
//                // Apply spacing to each child radio button
//                for (i in 0 until childCount) {
//                    val child = getChildAt(i)
//                    val layoutParams = child.layoutParams as? MarginLayoutParams
//                    layoutParams?.let { params ->
//                        if (i > 0) { // Don't add top margin to first item
//                            params.topMargin = spacingInPx
//                        }
//                        if (i < childCount - 1) { // Don't add bottom margin to last item
//                            params.bottomMargin = spacingInPx / 2
//                        }
//                        child.layoutParams = params
//                    }
//                }
//
//                setOnItemSelectedListener(object : CustomRadioGroupDelegate {
//                    override fun onItemSelected(position: Int, data: Any?) {
//                        notifyValueChange(data?.toString())
//                        notifyValidationChange()
//                    }
//                })
//            }
//        }
//    }
//
//    private fun createCheckboxGroup(config: InputFieldConfig): View {
//        return LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//
//            config.options?.forEach { option ->
//                val checkbox = CustomCheckBox(context).apply {
//                    text = option
//                    setCustomCheckBoxDelegate(object : CustomCheckboxDelegate {
//                        override fun onCheckChanged(checkBox: CustomCheckBox, isChecked: Boolean) {
//                            notifyValueChange(getCheckboxGroupValue(this@InputField))
//                            notifyValidationChange()
//                        }
//                    })
//                }
//                addView(checkbox)
//            }
//        }
//    }
//
//    private fun getCheckboxGroupValue(container: LinearLayout?): List<String> {
//        if (container == null) return emptyList()
//
//        val selectedValues = mutableListOf<String>()
//        for (i in 0 until container.childCount) {
//            val checkbox = container.getChildAt(i) as? CustomCheckBox
//            if (checkbox?.isChecked == true) {
//                selectedValues.add(checkbox.text.toString())
//            }
//        }
//        return selectedValues
//    }
//
//    fun isValid(): Boolean {
//        if (!isFieldRequired) return true
//
//        return when (currentConfig?.type) {
//            is InputFieldType.TextInput,
//            is InputFieldType.TextArea -> !getValue().toString().isNullOrBlank()
//            is InputFieldType.Dropdown,
//            is InputFieldType.RadioGroup -> getValue() != null
//            is InputFieldType.CheckboxGroup -> (getValue() as? List<*>)?.isNotEmpty() == true
//            else -> false
//        }
//    }
//
//    private fun notifyValueChange(value: Any?) {
//        delegate?.onValueChange(fieldId, value)
//    }
//
//    private fun notifyValidationChange() {
//        delegate?.onValidationChange(fieldId, isValid())
//    }
//
//    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
//        val typedValue = TypedValue()
//        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
//            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
//                ContextCompat.getColor(context, typedValue.resourceId)
//            } else {
//                typedValue.data
//            }
//        } else {
//            ContextCompat.getColor(context, fallbackColor)
//        }
//    }
//}