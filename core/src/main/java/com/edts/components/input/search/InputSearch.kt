package com.edts.components.input.search

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.edts.components.R
import com.edts.components.databinding.InputSearchBinding
import com.google.android.material.card.MaterialCardView

class InputSearch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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

    var state: State = State.REST
        set(value) {
            field = value
            updateState()
        }

    init {
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
                updateState()

            } finally {
                recycle()
            }
        }

        binding.ivRightIcon.setOnClickListener {
            binding.etSearch.text?.clear()
        }
    }

    private fun updateState() {
        val card: MaterialCardView = binding.inputSearch
        when (state) {
            State.REST -> {
                card.setCardBackgroundColor(resolveAttrColor(R.attr.colorBackgroundPrimary))
                card.strokeColor = resolveAttrColor(R.attr.colorStrokeSubtle)
                binding.etSearch.isEnabled = true
                binding.etSearch.setTextColor(resolveAttrColor(R.attr.colorForegroundPrimary))
                binding.etSearch.setHintTextColor(resolveAttrColor(R.attr.colorForegroundPlaceholder))
            }
            State.FOCUS -> {
                card.setCardBackgroundColor(resolveAttrColor(R.attr.colorBackgroundPrimary))
                card.strokeColor = resolveAttrColor(R.attr.colorStrokeAccent)
                binding.etSearch.isEnabled = true
            }
            State.DISABLE -> {
                card.backgroundTintList =
                    ColorStateList.valueOf(resolveAttrColor(R.attr.colorBackgroundDisabled))
                card.strokeColor = resolveAttrColor(R.attr.colorStrokeSubtle)
                binding.etSearch.isEnabled = false
                binding.etSearch.setHintTextColor(resolveAttrColor(R.attr.colorForegroundDisabled))
            }
            State.ERROR -> {
                card.setCardBackgroundColor(resolveAttrColor(R.attr.colorBackgroundPrimary))
                card.strokeColor = resolveAttrColor(R.attr.colorStrokeAttentionIntense)
                binding.etSearch.isEnabled = true
            }
        }
    }

    private fun resolveAttrColor(attr: Int): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()
        return color
    }
}