package com.edts.components.input.field

import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.setPadding
import androidx.core.widget.doAfterTextChanged
import com.edts.components.R
import com.edts.components.checkbox.CheckBox
import com.edts.components.checkbox.CheckboxDelegate
import com.edts.components.option.card.OptionCard
import com.edts.components.option.card.OptionCardDelegate
import com.edts.components.radiobutton.RadioGroup
import com.edts.components.radiobutton.RadioGroupDelegate
import com.edts.components.tray.BottomTray
import com.edts.components.tray.BottomTrayDelegate
import com.edts.components.utils.pxToDp
import com.edts.components.utils.resolveColorAttr
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

    private var requiredColor: Int = R.color.kitColorRed40
    private var errorColor: Int = R.color.kitColorRed40

    private var isFieldEnabled: Boolean = true
    private var currentErrorText: String? = null
    private var showErrorFromXml: Boolean = false

    private var supportingText: String? = null
    private var minLength: Int = 0
    private var maxLength: Int = 0

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
    private var radioGroupComponent: RadioGroup? = null
    private var checkboxContainer: LinearLayout? = null

    init {
        orientation = VERTICAL
        cacheCommonColors()

        titleTextView = TextView(context).apply {
            setTextAppearance(R.style.TextMedium_Label2)
            setTextColor(getCachedColor(R.attr.colorForegroundSecondary, R.color.kitColorNeutralGrayLight60))
            setPadding(0, 0, 0, padding4dp)
        }

        descriptionTextView = TextView(context).apply {
            setTextAppearance(R.style.TextRegular_Label4)
            setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
            setPadding(0, 0, 0, padding8dp)
            visibility = View.GONE
        }

        errorTextView = TextView(context).apply {
            setTextAppearance(R.style.TextRegular_Label4)
            setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
            setPadding(0, padding4dp, 0, 0)
            visibility = View.GONE
        }

        inputContainer = FrameLayout(context)

        addView(titleTextView)
        addView(descriptionTextView)
        addView(inputContainer)
        addView(errorTextView)

        attrs?.let { parseAttributes(it, defStyleAttr) }
    }

    private fun cacheCommonColors() {
        getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack)
        getCachedColor(R.attr.colorForegroundSecondary, R.color.kitColorNeutralGrayLight60)
        getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40)
        getCachedColor(R.attr.colorForegroundTertiary, R.color.kitColorNeutralGrayLight50)
        getCachedColor(R.attr.colorForegroundDisabled, R.color.kitColorNeutralGrayLight20)
        getCachedColor(R.attr.colorBackgroundPrimary, R.color.kitColorNeutralWhite)
        getCachedColor(R.attr.colorStrokeSubtle, R.color.kitColorNeutralGrayLight30)
        getCachedColor(R.attr.colorStrokeAccent, R.color.kitColorBrandPrimary30)
        getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.kitColorRed50)
    }

    private fun getCachedColor(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
        val key = attrRes
        return colorCache.getOrPut(key) {
            context.resolveColorAttr(attrRes, fallbackColor)
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
            val inputTypeValue = typedArray.getInt(R.styleable.InputField_inputFieldType, 0)
            maxLength = typedArray.getInt(R.styleable.InputField_maxLength, 0)
            minLength = typedArray.getInt(R.styleable.InputField_minLength, 0)
            val maxLines = typedArray.getInt(R.styleable.InputField_maxLines, 4)
            val minLines = typedArray.getInt(R.styleable.InputField_minLines, 3)
            supportingText = typedArray.getString(R.styleable.InputField_supportingText)

            isFieldEnabled = typedArray.getBoolean(R.styleable.InputField_fieldEnabled, true)
            currentErrorText = typedArray.getString(R.styleable.InputField_errorText)
            showErrorFromXml = typedArray.getBoolean(R.styleable.InputField_showError, false)

            val optionsArrayId = typedArray.getResourceId(R.styleable.InputField_options, -1)
            val options = if (optionsArrayId != -1) {
                context.resources.getStringArray(optionsArrayId).toList()
            } else null

            requiredColor = getCachedColor(
                typedArray.getResourceId(R.styleable.InputField_requiredColor, -1),
                R.color.kitColorRed40
            )

            errorColor = getCachedColor(
                typedArray.getResourceId(R.styleable.InputField_errorColor, -1),
                R.color.kitColorRed40
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
                1 -> InputFieldType.NumberInput
                2 -> InputFieldType.TextArea
                3 -> InputFieldType.Dropdown
                4 -> InputFieldType.RadioGroup
                5 -> InputFieldType.CheckboxGroup
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
                if (!currentErrorText.isNullOrEmpty()) {
                    setError(currentErrorText)
                }
            }

        } finally {
            typedArray.recycle()
        }
    }

    private fun updateSupportingTextDisplay() {
        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.NumberInput,
            is InputFieldType.TextArea -> {
                val tagMap = textInputEditText?.tag as? MutableMap<String, TextView>
                val supportingTextView = tagMap?.get("supportingText")

                if (supportingTextView != null && !supportingText.isNullOrEmpty()) {
                    supportingTextView.text = supportingText
                    val color = when {
                        !isFieldEnabled -> getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40)
                        !currentErrorText.isNullOrEmpty() -> getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40)
                        else -> getCachedColor(R.attr.colorForegroundTertiary, R.color.kitColorNeutralGrayLight50)
                    }
                    supportingTextView.setTextColor(color)
                } else {
                    supportingTextView?.visibility = View.GONE
                }

                val bottomRowContainer = (currentInputComponent as? LinearLayout)?.getChildAt(1) as? LinearLayout
                (bottomRowContainer?.tag as? (() -> Unit))?.invoke()
            }
            else -> {
            }
        }
    }

    private fun validateCurrentInput() {
        val currentValue = getValue()?.toString() ?: ""
        val currentLength = currentValue.length

        val lengthError = when {
            minLength > 0 && currentLength < minLength -> "Minimum $minLength characters required"
            maxLength > 0 && currentLength > maxLength -> "Maximum $maxLength characters allowed"
            else -> null
        }

        if (lengthError != null) {
            setError(lengthError)
        } else if (currentErrorText?.contains("Minimum") == true ||
            currentErrorText?.contains("Maximum") == true) {
            clearError()
        }
    }

    private fun setTitle(title: String) {
        if (title.isEmpty()) {
            titleTextView.visibility = View.GONE
        } else {
            titleTextView.visibility = View.VISIBLE
            if (!isInEditMode) {
                titleTextView.text = if (isFieldRequired) {
                    val spannableTitle = SpannableString("$title *")
                    spannableTitle.setSpan(
                        ForegroundColorSpan(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40)),
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
    }

    private fun setDescription(description: String) {
        descriptionTextView.text = description
        descriptionTextView.visibility = if (description.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateTextInputColors(editText: EditText, enabled: Boolean) {
        if (enabled) {
            editText.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))
            editText.setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
        } else {
            val disabledColor = getCachedColor(R.attr.colorForegroundDisabled, R.color.kitColorNeutralGrayLight20)
            editText.setTextColor(disabledColor)
            editText.setHintTextColor(disabledColor)
        }
    }

    private fun updateDropdownColors(container: LinearLayout, textView: TextView, enabled: Boolean) {
        if (enabled) {
            textView.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))
            container.background = getCachedDrawable("rounded_normal") { createRoundedBackground() }
        } else {
            textView.setTextColor(getCachedColor(R.attr.colorForegroundDisabled, R.color.kitColorNeutralGrayLight20))
            container.background = getCachedDrawable("rounded_disabled") { createDisabledBackground() }
        }
    }

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
            is InputFieldType.NumberInput -> createNumberInput(config)
            is InputFieldType.TextArea -> createTextArea(config)
            is InputFieldType.Dropdown -> createCustomDropdownView(config)
            is InputFieldType.RadioGroup -> createRadioGroup(config)
            is InputFieldType.CheckboxGroup -> createCheckboxGroup(config)
            else -> { TODO() }
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
            setPadding(padding12dp, padding10dp, padding12dp, padding10dp)
//            minimumHeight = minHeight48dp
            isFocusable = false
            isClickable = false
        }
        textInputContainer = container

//        val editText = EditText(context).apply {
        val editText = EditText(ContextThemeWrapper(context, R.style.EditTextCursorStyle), null, 0).apply {
            hint = config.hint
            setTextAppearance(R.style.TextRegular_Paragraph1)
            setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))
            setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
            background = null
            setPadding(0, 0, 0, 0)

            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
            imeOptions = EditorInfo.IME_ACTION_NEXT

            if (maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }

            setOnFocusChangeListener { _, hasFocus ->
                updateInputBackground(container, this, hasFocus)
            }

            doAfterTextChanged { editable ->
                val text = editable?.toString() ?: ""
                notifyValueChange(text)

                val currentLength = text.length
                val lengthError = when {
                    minLength > 0 && currentLength > 0 && currentLength < minLength ->
                        "Minimum $minLength characters required"
                    maxLength > 0 && currentLength > maxLength ->
                        "Maximum $maxLength characters allowed"
                    else -> null
                }

                if (lengthError != null) {
                    setError(lengthError)
                } else if (currentErrorText?.contains("Minimum") == true ||
                    currentErrorText?.contains("Maximum") == true) {
                    clearError()
                }

                if (!text.isNullOrBlank() && currentErrorText != null) {
                    clearError()
                }

                notifyValidationChange()
            }

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        textInputEditText = editText

        if (maxLength > 0 || !supportingText.isNullOrEmpty()) {
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
                visibility = View.GONE
            }

            val leftContainer = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }

            val errorTextForCounter = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
                visibility = View.GONE
            }

            val supportingTextView = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
                text = supportingText
                visibility = if (supportingText.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            leftContainer.addView(errorTextForCounter)
            leftContainer.addView(supportingTextView)

            val counterText = if (maxLength > 0) {
                TextView(context).apply {
                    setTextAppearance(R.style.TextRegular_Label4)
                    setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
                    gravity = Gravity.END

                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
            } else null

            var lastLength = 0
            val updateCounter = {
                val currentLength = editText.text?.length ?: 0
                if (currentLength != lastLength && counterText != null) {
                    counterText.text = "($currentLength/$maxLength)"
                    lastLength = currentLength
                }
            }

            val updateBottomRowVisibility = {
                val hasError = errorTextForCounter.visibility == View.VISIBLE
                val hasSupporting = supportingTextView.visibility == View.VISIBLE
                val hasText = (editText.text?.length ?: 0) > 0

                val shouldShow = hasError || hasSupporting || (counterText != null && hasText)

                bottomRowContainer.visibility = if (shouldShow) View.VISIBLE else View.GONE
            }

            if (counterText != null) {
                updateCounter()
                editText.doAfterTextChanged { updateCounter() }
            }

            val tagMap = mutableMapOf<String, TextView>().apply {
                put("errorText", errorTextForCounter)
                put("supportingText", supportingTextView)
                counterText?.let { put("counterText", it) }
            }
            editText.tag = tagMap

            bottomRowContainer.tag = updateBottomRowVisibility

            bottomRowContainer.addView(leftContainer)
            counterText?.let { bottomRowContainer.addView(it) }

            outerContainer.addView(container)
            outerContainer.addView(bottomRowContainer)
            container.addView(editText)

            updateBottomRowVisibility()

            return outerContainer
        }

        container.addView(editText)
        return container
    }

    private fun createNumberInput(config: InputFieldConfig): View {
        val container = LinearLayout(context).apply {
            background = getCachedDrawable("rounded_normal") { createRoundedBackground() }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(padding12dp, padding10dp, padding12dp, padding10dp)
//            minimumHeight = minHeight48dp
            isFocusable = false
            isClickable = false
        }
        textInputContainer = container

//        val editText = EditText(context).apply {
        val editText = EditText(ContextThemeWrapper(context, R.style.EditTextCursorStyle), null, 0).apply {
            hint = config.hint
            setTextAppearance(R.style.TextRegular_Paragraph1)
            setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))
            setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
            background = null
            setPadding(0, 0, 0, 0)

            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
            imeOptions = EditorInfo.IME_ACTION_NEXT

            if (maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }

            setOnFocusChangeListener { _, hasFocus ->
                updateInputBackground(container, this, hasFocus)
            }

            doAfterTextChanged { editable ->
                val text = editable?.toString() ?: ""
                notifyValueChange(text)

                val currentLength = text.length
                val lengthError = when {
                    minLength > 0 && currentLength > 0 && currentLength < minLength ->
                        "Minimum $minLength digits required"
                    maxLength > 0 && currentLength > maxLength ->
                        "Maximum $maxLength digits allowed"
                    else -> null
                }

                if (lengthError != null) {
                    setError(lengthError)
                } else if (currentErrorText?.contains("Minimum") == true ||
                    currentErrorText?.contains("Maximum") == true) {
                    clearError()
                }

                if (!text.isNullOrBlank() && currentErrorText != null) {
                    clearError()
                }

                notifyValidationChange()
            }

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        textInputEditText = editText

        if (maxLength > 0 || !supportingText.isNullOrEmpty()) {
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
                visibility = View.GONE
            }

            val leftContainer = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }

            val errorTextForCounter = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
                visibility = View.GONE
            }

            val supportingTextView = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
                text = supportingText
                visibility = if (supportingText.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            leftContainer.addView(errorTextForCounter)
            leftContainer.addView(supportingTextView)

            val counterText = if (maxLength > 0) {
                TextView(context).apply {
                    setTextAppearance(R.style.TextRegular_Label4)
                    setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
                    gravity = Gravity.END

                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
            } else null

            var lastLength = 0
            val updateCounter = {
                val currentLength = editText.text?.length ?: 0
                if (currentLength != lastLength && counterText != null) {
                    counterText.text = "($currentLength/$maxLength)"
                    lastLength = currentLength
                }
            }

            val updateBottomRowVisibility = {
                val hasError = errorTextForCounter.visibility == View.VISIBLE
                val hasSupporting = supportingTextView.visibility == View.VISIBLE
                val hasText = (editText.text?.length ?: 0) > 0

                val shouldShow = hasError || hasSupporting || (counterText != null && hasText)

                bottomRowContainer.visibility = if (shouldShow) View.VISIBLE else View.GONE
            }

            if (counterText != null) {
                updateCounter()
                editText.doAfterTextChanged { updateCounter() }
            }

            val tagMap = mutableMapOf<String, TextView>().apply {
                put("errorText", errorTextForCounter)
                put("supportingText", supportingTextView)
                counterText?.let { put("counterText", it) }
            }
            editText.tag = tagMap

            bottomRowContainer.tag = updateBottomRowVisibility

            bottomRowContainer.addView(leftContainer)
            counterText?.let { bottomRowContainer.addView(it) }

            outerContainer.addView(container)
            outerContainer.addView(bottomRowContainer)
            container.addView(editText)

            updateBottomRowVisibility()

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
            setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))
            setHintTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
            background = null
            gravity = Gravity.TOP
            setPadding(0, 0, 0, 0)

            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                    android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            imeOptions = EditorInfo.IME_ACTION_DONE
            setLines(config.minLines)
            maxLines = config.maxLines

            if (maxLength > 0) {
                filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }

            setOnFocusChangeListener { _, hasFocus ->
                updateInputBackground(container, this, hasFocus)
            }

            doAfterTextChanged { editable ->
                val text = editable?.toString() ?: ""
                notifyValueChange(text)

                val currentLength = text.length
                val lengthError = when {
                    minLength > 0 && currentLength > 0 && currentLength < minLength ->
                        "Minimum $minLength karakter"
                    maxLength > 0 && currentLength > maxLength ->
                        "Maksimum $maxLength karakter"
                    else -> null
                }

                if (lengthError != null) {
                    setError(lengthError)
                } else if (currentErrorText?.contains("Minimum") == true ||
                    currentErrorText?.contains("Maksimum") == true) {
                    clearError()
                }

                if (!text.isNullOrBlank() && currentErrorText != null) {
                    clearError()
                }

                notifyValidationChange()
            }

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        textInputEditText = editText

        if (maxLength > 0 || !supportingText.isNullOrEmpty()) {
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
                visibility = View.GONE
            }

            val leftContainer = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                )
            }

            val errorTextForCounter = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
                visibility = View.GONE
            }

            val supportingTextView = TextView(context).apply {
                setTextAppearance(R.style.TextRegular_Label4)
                setTextColor(getCachedColor(R.attr.colorForegroundTertiary, R.color.kitColorNeutralGrayLight50))
                text = supportingText
                visibility = if (supportingText.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            leftContainer.addView(errorTextForCounter)
            leftContainer.addView(supportingTextView)

            val counterText = if (maxLength > 0) {
                TextView(context).apply {
                    setTextAppearance(R.style.TextRegular_Label4)
                    setTextColor(getCachedColor(R.attr.colorForegroundTertiary, R.color.kitColorNeutralGrayLight50))
                    gravity = Gravity.END

                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
            } else null

            var lastLength = 0
            val updateCounter = {
                val currentLength = editText.text?.length ?: 0
                if (currentLength != lastLength && counterText != null) {
                    counterText.text = "($currentLength/$maxLength)"
                    lastLength = currentLength
                }
            }

            val updateBottomRowVisibility = {
                val hasError = errorTextForCounter.visibility == View.VISIBLE
                val hasSupporting = supportingTextView.visibility == View.VISIBLE
                val hasText = (editText.text?.length ?: 0) > 0

                val shouldShow = hasError || hasSupporting || (counterText != null && hasText)

                bottomRowContainer.visibility = if (shouldShow) View.VISIBLE else View.GONE
            }

            if (counterText != null) {
                updateCounter()
                editText.doAfterTextChanged { updateCounter() }
            }

            val tagMap = mutableMapOf<String, TextView>().apply {
                put("errorText", errorTextForCounter)
                put("supportingText", supportingTextView)
                counterText?.let { put("counterText", it) }
            }
            editText.tag = tagMap

            bottomRowContainer.tag = updateBottomRowVisibility

            bottomRowContainer.addView(leftContainer)
            counterText?.let { bottomRowContainer.addView(it) }

            outerContainer.addView(container)
            outerContainer.addView(bottomRowContainer)
            container.addView(editText)

            updateBottomRowVisibility()

            return outerContainer
        }

        container.addView(editText)
        return container
    }

    private fun createFocusedBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = cornerRadius
        shape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.kitColorNeutralWhite))
        shape.setStroke(strokeWidth2dp, getCachedColor(R.attr.colorStrokeAccent, R.color.kitColorBrandPrimary30))
        return shape
    }

    private fun createCursorDrawable(): Drawable {
        val cursorDrawable = GradientDrawable()
        cursorDrawable.shape = GradientDrawable.RECTANGLE
        cursorDrawable.setSize((2.pxToDp), 0)
        cursorDrawable.setColor(getCachedColor(R.attr.colorStrokeAccent, R.color.kitColorBrandPrimary30))
        return cursorDrawable
    }

    private fun createDisabledBackground(): Drawable {
        val baseShape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            this.cornerRadius = this@InputField.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundDisabled, R.color.kitColorNeutralGrayDarkA5))
            setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeSubtle, R.color.kitColorNeutralGrayLight30))
        }

        val overlayShape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            this.cornerRadius = this@InputField.cornerRadius
            setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated, R.color.kitColorNeutralGrayLightA5))
        }

        return LayerDrawable(arrayOf(baseShape, overlayShape))
    }

    private fun updateInputBackground(container: LinearLayout, editText: EditText, hasFocus: Boolean) {
        container.background = when {
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
        shape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.kitColorNeutralWhite))
        shape.setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeAttentionIntense, R.color.kitColorRed50))
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
            setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0,0,0,0)

            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        dropdownTextView = textView

        val iconView = ImageView(context).apply {
            setImageResource(R.drawable.kit_ic_chevron_down)
            setColorFilter(getCachedColor(R.attr.colorForegroundTertiary, R.color.kitColorNeutralGrayLight50))
            scaleType = ImageView.ScaleType.CENTER

            layoutParams = FrameLayout.LayoutParams(
                iconSize24dp, iconSize24dp,
                Gravity.CENTER_VERTICAL or Gravity.END
            )
        }

        container.addView(textView)
        container.addView(iconView)

        container.setOnClickListener {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            rootView.findFocus()?.let { focused ->
                focused.clearFocus()
                imm.hideSoftInputFromWindow(focused.windowToken, 0)
            }

            showOptionDrawer(config.options) { selectedOption ->
                textView.text = selectedOption
                textView.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))

                notifyValueChange(selectedOption)
                notifyValidationChange()
                clearError()
            }
        }

        return container
    }

    private fun createRoundedBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = cornerRadius
        shape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.kitColorNeutralWhite))
        shape.setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeSubtle, R.color.kitColorNeutralGrayLight30))

        val focusedShape = GradientDrawable()
        focusedShape.shape = GradientDrawable.RECTANGLE
        focusedShape.cornerRadius = cornerRadius
        focusedShape.setColor(getCachedColor(R.attr.colorBackgroundPrimary, R.color.kitColorNeutralWhite))
        focusedShape.setStroke(strokeWidth1dp, getCachedColor(R.attr.colorStrokeAccent, R.color.kitColorBrandPrimary30))

        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusedShape)
        stateListDrawable.addState(intArrayOf(), shape)

        return stateListDrawable
    }

    private fun showOptionDrawer(options: List<String>?, onSelection: ((String) -> Unit)? = null) {
        if (options.isNullOrEmpty()) return
        var bottomTrayRef: BottomTray? = null
        val bottomTray = createOptionDrawerLayout(options) { selectedOption ->
            if (onSelection != null) {
                onSelection(selectedOption)
            } else {
                val textInputLayout = currentInputComponent as? TextInputLayout
                val autoCompleteTextView = textInputLayout?.editText as? MaterialAutoCompleteTextView
                autoCompleteTextView?.setText(selectedOption, false)
            }
            notifyValueChange(selectedOption)
            notifyValidationChange()
            bottomTrayRef?.dismiss()
        }
        bottomTrayRef = bottomTray
        val fragmentManager = getFragmentManager()
        bottomTray.show(fragmentManager, "OptionDrawer")
    }

    private fun getFragmentManager(): androidx.fragment.app.FragmentManager {
        return when {
            context is androidx.fragment.app.FragmentActivity -> {
                (context as androidx.fragment.app.FragmentActivity).supportFragmentManager
            }
            else -> {
                var contextTemp = context
                while (contextTemp is ContextWrapper) {
                    if (contextTemp is androidx.fragment.app.FragmentActivity) {
                        return contextTemp.supportFragmentManager
                    }
                    contextTemp = contextTemp.baseContext
                }
                throw IllegalStateException("Cannot find FragmentManager. Make sure InputField is used in a FragmentActivity context.")
            }
        }
    }

    private fun createOptionDrawerLayout(
        options: List<String>,
        onOptionSelected: (String) -> Unit
    ): BottomTray {
        val scrollView = ScrollView(context)
        val contentContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(padding16dp, 0, padding16dp, 0)
        }
        options.forEach { option ->
            val optionCard = OptionCard(context).apply {
                titleText = option
                delegate = object : OptionCardDelegate {
                    override fun onClick(card: OptionCard) {
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
            contentContainer.addView(optionCard)
        }
        scrollView.addView(contentContainer)
        val bottomTray = BottomTray.newInstance(
            title = currentConfig?.hint ?: "Select Option",
            showDragHandle = true,
            showFooter = false,
            hasShadow = true,
            hasStroke = true
        ).apply {
            setTrayContentView(scrollView)
            delegate = object : BottomTrayDelegate {
                override fun onShow(dialogInterface: DialogInterface) {
                }

                override fun onDismiss(dialogInterface: DialogInterface) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            }
        }
        return bottomTray
    }

    private fun createRadioGroup(config: InputFieldConfig): View {
        return RadioGroup(context).apply {
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
                setOnItemSelectedListener(object : RadioGroupDelegate {
                    override fun onItemSelected(position: Int, data: Any?) {
                        notifyValueChange(data?.toString())
                        notifyValidationChange()
                        clearError()
                    }
                })
            }
        }
    }

    private fun createCheckboxGroup(config: InputFieldConfig): View {
        return LinearLayout(context).apply {
            checkboxContainer = this
            orientation = LinearLayout.VERTICAL
            config.options?.forEachIndexed { index, option ->
                val checkbox = CheckBox(context).apply {
                    text = option
                    setCustomCheckBoxDelegate(object : CheckboxDelegate {
                        override fun onCheckChanged(checkBox: CheckBox, isChecked: Boolean) {
                            notifyValueChange(getCheckboxGroupValue(this@InputField.checkboxContainer))
                            notifyValidationChange()
                            clearError()
                        }
                    })
                }
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (index > 0) {
                        topMargin = resources.getDimensionPixelSize(R.dimen.margin_8dp)
                    }
                }
                addView(checkbox, layoutParams)
            }
        }
    }

    private fun getCheckboxGroupValue(container: LinearLayout?): List<String> {
        if (container == null) return emptyList()
        val selectedValues = mutableListOf<String>()
        for (i in 0 until container.childCount) {
            val checkbox = container.getChildAt(i) as? CheckBox
            if (checkbox?.isChecked == true) {
                selectedValues.add(checkbox.text.toString())
            }
        }
        return selectedValues
    }

    private fun notifyValueChange(value: Any?) {
        delegate?.onValueChange(fieldId, value)
    }

    private fun notifyValidationChange() {
        delegate?.onValidationChange(fieldId, isValid())
    }

    fun configure(config: InputFieldConfig, id: String = "") {
        this.fieldId = id
        this.currentConfig = config
        this.isFieldRequired = config.isRequired
        this.minLength = config.minLength
        this.maxLength = config.maxLength

        setTitle(config.title)
        config.description?.let { setDescription(it) }

        setupInputComponent(config)
    }

    fun setSupportingText(text: String?) {
        supportingText = text
        updateSupportingTextDisplay()
    }

    fun setMinLength(min: Int) {
        minLength = min
        validateCurrentInput()
    }

    fun setMaxLength(max: Int) {
        maxLength = max
        textInputEditText?.filters = if (max > 0) {
            arrayOf(InputFilter.LengthFilter(max))
        } else {
            arrayOf()
        }
        validateCurrentInput()
    }

    fun setError(errorText: String?) {
        currentErrorText = errorText

        if (!errorText.isNullOrEmpty()) {
            when (currentConfig?.type) {
                is InputFieldType.TextInput,
                is InputFieldType.NumberInput,
                is InputFieldType.TextArea -> {
                    val tagMap = textInputEditText?.tag as? MutableMap<String, TextView>
                    val inlineErrorText = tagMap?.get("errorText")
                    val counterText = tagMap?.get("counterText")
                    val supportingTextView = tagMap?.get("supportingText")
                    if (inlineErrorText != null) {
                        inlineErrorText.text = errorText
                        inlineErrorText.visibility = View.VISIBLE
                        errorTextView.visibility = View.GONE
                        supportingTextView?.visibility = View.GONE

                        if (errorText.contains("Minimum") || errorText.contains("Maximum")) {
                            counterText?.setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
                        }

                        textInputContainer?.background = getCachedDrawable("rounded_error") { createErrorBackground() }
                        val bottomRowContainer = (currentInputComponent as? LinearLayout)?.getChildAt(1) as? LinearLayout
                        (bottomRowContainer?.tag as? (() -> Unit))?.invoke()

                        updateSupportingTextDisplay()
                    } else {
                        val textInputLayout = currentInputComponent as? TextInputLayout
                        if (textInputLayout != null) {
                            textInputLayout.error = errorText
                            textInputLayout.isErrorEnabled = true
                        } else {
                            errorTextView.text = errorText
                            errorTextView.visibility = View.VISIBLE
                            errorTextView.setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))

                            textInputContainer?.background = getCachedDrawable("rounded_error") { createErrorBackground() }
                        }
                    }
                }
                is InputFieldType.Dropdown -> {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    if (textInputLayout != null) {
                        textInputLayout.error = errorText
                        textInputLayout.isErrorEnabled = true
                    } else {
                        errorTextView.text = errorText
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
                        dropdownContainer?.background = getCachedDrawable("rounded_error") { createErrorBackground() }
                    }
                }
                is InputFieldType.RadioGroup -> {
                    errorTextView.text = errorText
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))
                    (currentInputComponent as? RadioGroup)?.setErrorStateOnAll(true)
                }
                is InputFieldType.CheckboxGroup -> {
                    errorTextView.text = errorText
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.setTextColor(getCachedColor(R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40))

                    (currentInputComponent as? ViewGroup)?.let { container ->
                        for (i in 0 until container.childCount) {
                            val child = container.getChildAt(i)
                            if (child is CheckBox) {
                                child.setErrorState(true)
                            }
                        }
                    }
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
        val tagMap = textInputEditText?.tag as? MutableMap<String, TextView>
        val inlineErrorText = tagMap?.get("errorText")
        val counterText = tagMap?.get("counterText")
        val supportingTextView = tagMap?.get("supportingText")

        inlineErrorText?.visibility = View.GONE
        counterText?.setTextColor(getCachedColor(R.attr.colorForegroundPlaceholder, R.color.kitColorNeutralGrayLight40))
        updateSupportingTextDisplay()

        val bottomRowContainer = (currentInputComponent as? LinearLayout)?.getChildAt(1) as? LinearLayout
        (bottomRowContainer?.tag as? (() -> Unit))?.invoke()

        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.NumberInput,
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
            is InputFieldType.RadioGroup -> {
                errorTextView.visibility = View.GONE
                (currentInputComponent as? RadioGroup)?.setErrorStateOnAll(false)
            }
            is InputFieldType.CheckboxGroup -> {
                errorTextView.visibility = View.GONE

                (currentInputComponent as? ViewGroup)?.let { container ->
                    for (i in 0 until container.childCount) {
                        val child = container.getChildAt(i)
                        if (child is CheckBox) {
                            child.setErrorState(false)
                        }
                    }
                }
            }
            else -> {
            }
        }
    }

    fun getErrorText(): String? = currentErrorText
    fun isFieldEnabled(): Boolean = isFieldEnabled

    fun getValue(): Any? {
        return when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.NumberInput,
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
            is InputFieldType.NumberInput,
            is InputFieldType.TextArea -> {
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
                    dropdownTextView?.let { textView ->
                        if (value != null) {
                            textView.text = value.toString()
                            textView.setTextColor(getCachedColor(R.attr.colorForegroundPrimary, R.color.kitColorNeutralBlack))
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
                        val checkbox = container.getChildAt(i) as? CheckBox
                        checkbox?.isChecked = selectedValues.contains(checkbox?.text?.toString())
                    }
                }
            }
            else -> {
            }
        }
    }

    fun isValid(): Boolean {
        if (!isFieldRequired) return true
        val value = getValue()
        val isRequiredValid = when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.NumberInput,
            is InputFieldType.TextArea -> !value.toString().isNullOrBlank()
            is InputFieldType.Dropdown,
            is InputFieldType.RadioGroup -> value != null
            is InputFieldType.CheckboxGroup -> (value as? List<*>)?.isNotEmpty() == true
            else -> false
        }

        if (!isRequiredValid) {
            val fieldName = currentConfig?.title?.takeIf { it.isNotBlank() } ?: "Field"
            setError("$fieldName wajib diisi")
            return false
        }

        if (minLength > 0 || maxLength > 0) {
            val text = value?.toString() ?: ""
            val length = text.length

            if (minLength > 0 && length < minLength) {
                setError("Minimal $minLength karakter")
                return false
            }
            if (maxLength > 0 && length > maxLength) {
                setError("Maksimal $maxLength karakter")
                return false
            }
        }

        clearError()
        return true
    }

    fun getNumericValue(): Double? {
        return when (currentConfig?.type) {
            is InputFieldType.NumberInput -> {
                val text = getValue()?.toString()
                text?.toDoubleOrNull()
            }
            else -> null
        }
    }

    fun getIntValue(): Int? {
        return when (currentConfig?.type) {
            is InputFieldType.NumberInput -> {
                val text = getValue()?.toString()
                text?.toIntOrNull()
            }
            else -> null
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isFieldEnabled = enabled

        when (currentConfig?.type) {
            is InputFieldType.TextInput,
            is InputFieldType.NumberInput,
            is InputFieldType.TextArea -> {
                textInputEditText?.let { editText ->
                    textInputContainer?.let { container ->
                        editText.isEnabled = enabled
                        updateTextInputColors(editText, enabled)
                        updateInputBackground(container, editText, editText.hasFocus() && enabled)
                    }
                } ?: run {
                    val textInputLayout = currentInputComponent as? TextInputLayout
                    textInputLayout?.isEnabled = enabled
                    textInputLayout?.editText?.isEnabled = enabled
                }
                updateSupportingTextDisplay()
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
                        val checkbox = container.getChildAt(i) as? CheckBox
                        checkbox?.isEnabled = enabled
                    }
                }
            }
            else -> {}
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        colorCache.clear()
        drawableCache.clear()
    }
}