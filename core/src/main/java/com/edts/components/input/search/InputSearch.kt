package com.edts.components.input.search

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.edts.components.databinding.InputSearchBinding
import com.google.android.material.card.MaterialCardView
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.edts.components.R
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute

class InputSearch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: InputSearchBinding = InputSearchBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class State(val value: Int) {
        REST(0),
        FOCUS(1),
        DISABLE(2),
        ERROR(3);

        companion object {
            fun fromValue(value: Int): State =
                values().find { it.value == value } ?: REST
        }
    }

    private var _state: State = State.REST
    private var previousState: State = State.REST

    var state: State
        get() = _state
        set(value) {
            val oldState = _state
            if (value != State.FOCUS) {
                previousState = value
            }
            _state = value
            updateState()

            if (oldState != value) {
                delegate?.onStateChange(this, value, oldState)
            }
        }

    private var errorTextSnapshot: String? = null

    enum class CardState {
        REST,
        ON_PRESS
    }

    private var cardState: CardState = CardState.REST
        set(value) {
            field = value
        }

    var delegate: InputSearchDelegate? = null

    init {
        rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)
        radius = 12f.dpToPx
        setCardBackgroundColor(
            context.resolveColorAttribute(
                R.attr.colorBackgroundPrimary,
                android.R.color.white
            )
        )

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.InputSearchView,
            0, 0
        ).apply {
            try {
                binding.etSearch.hint = getString(R.styleable.InputSearchView_inputSearchHint)
                    ?: "Search Placeholder"

                val stateValue = getInt(R.styleable.InputSearchView_inputSearchState, 0)
                state = State.fromValue(stateValue)

                updateRightIconVisibility()

                updateState()
                setupCardPressState()
                setupFocusListeners()
                setupTextWatcher()
                setupCloseIconListener()
                setupSearchActionListener()
            } finally {
                recycle()
            }
        }
    }

    private fun updateRightIconVisibility() {
        val text = binding.etSearch.text?.toString() ?: ""
        binding.ivRightIcon.visibility = if (text.isNotEmpty()) VISIBLE else GONE
    }

    private fun setupFocusListeners() {
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            val oldState = _state

            if (hasFocus) {
                if (_state != State.DISABLE) {
                    _state = State.FOCUS
                    updateState()
                    delegate?.onFocusChange(this, true, _state, oldState)
                }
            } else {
                if (_state == State.FOCUS) {
                    val currentText = binding.etSearch.text?.toString()

                    state = if (previousState == State.ERROR) {
                        if (currentText == errorTextSnapshot) {
                            State.ERROR
                        } else {
                            State.REST
                        }
                    } else {
                        previousState
                    }

                    delegate?.onFocusChange(this, false, state, State.FOCUS)
                }
            }
        }
    }

    private fun setupTextWatcher() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                delegate?.onSearchTextChange(this@InputSearch, text)
                updateRightIconVisibility()

                if (_state == State.FOCUS && previousState == State.ERROR) {
                    if (text != errorTextSnapshot) {
                        errorTextSnapshot = null
                    }
                }
            }
        })
    }

    private fun setupCloseIconListener() {
        binding.ivRightIcon.setOnClickListener {
            binding.etSearch.text?.clear()
            binding.etSearch.clearFocus()
            hideKeyboard()
            errorTextSnapshot = null
            state = State.REST
            delegate?.onCloseIconClick(this)
        }

        val rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.kitColorNeutralGrayDarkA5))

        val rippleDrawable = RippleDrawable(rippleColor, null, null)
        rippleDrawable.radius = 12f.dpToPx.toInt()

        binding.ivRightIcon.background = rippleDrawable
    }

    private fun setupSearchActionListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO) {
                val query = binding.etSearch.text?.toString() ?: ""
                binding.etSearch.clearFocus()
                hideKeyboard()
                delegate?.onSearchSubmit(this, query)
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private fun createCardBackgroundDrawable(): Drawable {
        val disabledModifierDrawable = GradientDrawable().apply {
            cornerRadius = 12f.dpToPx
            setColor(
                context.resolveColorAttribute(
                    R.attr.colorBackgroundDisabled,
                    android.R.color.darker_gray
                )
            )
        }

        return LayerDrawable(arrayOf(disabledModifierDrawable))
    }

    private fun updateCardBackground() {
        background = createCardBackgroundDrawable()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                cardState = CardState.ON_PRESS
            }
            MotionEvent.ACTION_UP -> {
                cardState = CardState.REST
                handleSearchFieldClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                cardState = CardState.REST
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleSearchFieldClick() {
        if (_state == State.ERROR) {
            errorTextSnapshot = binding.etSearch.text?.toString()
            previousState = State.ERROR
            _state = State.FOCUS
            updateState()
        }
        binding.etSearch.requestFocus()
        delegate?.onSearchFieldClick(this)
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
    }

    private fun updateState() {
        val card: MaterialCardView = binding.inputSearch
        when (_state) {
            State.REST -> {
                card.setCardBackgroundColor(
                    context.resolveColorAttribute(
                        R.attr.colorBackgroundPrimary,
                        android.R.color.white
                    )
                )
                card.strokeColor = context.resolveColorAttribute(
                    R.attr.colorStrokeSubtle,
                    android.R.color.darker_gray
                )
                binding.etSearch.isEnabled = true
                binding.etSearch.setTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundPrimary,
                        android.R.color.black
                    )
                )
                binding.etSearch.setHintTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundPlaceholder,
                        android.R.color.darker_gray
                    )
                )
            }
            State.FOCUS -> {
                card.setCardBackgroundColor(
                    context.resolveColorAttribute(
                        R.attr.colorBackgroundPrimary,
                        android.R.color.white
                    )
                )
                card.strokeColor = context.resolveColorAttribute(
                    R.attr.colorStrokeAccent,
                    android.R.color.holo_blue_dark
                )
                binding.etSearch.isEnabled = true
                binding.etSearch.setTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundPrimary,
                        android.R.color.black
                    )
                )
                binding.etSearch.setHintTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundPlaceholder,
                        android.R.color.darker_gray
                    )
                )
            }
            State.DISABLE -> {
                updateCardBackground()
                card.strokeColor = context.resolveColorAttribute(
                    R.attr.colorStrokeSubtle,
                    android.R.color.darker_gray
                )
                binding.etSearch.isEnabled = false
                binding.etSearch.setTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundDisabled,
                        android.R.color.darker_gray
                    )
                )
                binding.etSearch.setHintTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundDisabled,
                        android.R.color.darker_gray
                    )
                )
            }
            State.ERROR -> {
                card.setCardBackgroundColor(
                    context.resolveColorAttribute(
                        R.attr.colorBackgroundPrimary,
                        android.R.color.white
                    )
                )
                card.strokeColor = context.resolveColorAttribute(
                    R.attr.colorStrokeAttentionIntense,
                    android.R.color.holo_red_dark
                )
                binding.etSearch.isEnabled = true
                binding.etSearch.setTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundPrimary,
                        android.R.color.black
                    )
                )
                binding.etSearch.setHintTextColor(
                    context.resolveColorAttribute(
                        R.attr.colorForegroundPlaceholder,
                        android.R.color.darker_gray
                    )
                )
            }
        }
    }

    fun setText(text: String) {
        binding.etSearch.setText(text)
    }

    fun getText(): String {
        return binding.etSearch.text?.toString() ?: ""
    }

    fun clearText() {
        binding.etSearch.text?.clear()
    }

    override fun clearFocus() {
        binding.etSearch.clearFocus()
        hideKeyboard()
    }
}