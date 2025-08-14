package com.example.components.leave.card

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.LeaveCounterBinding

class LeaveCounter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = LeaveCounterBinding.inflate(LayoutInflater.from(context), this)

    enum class CounterType(val textColorAttr: Int) {
        NORMAL(R.attr.colorForegroundSecondary),
        CRITICAL(R.attr.colorForegroundAttentionIntense);
    }

    var text: CharSequence?
        get() = binding.tvLeaveCounter.text
        set(value) {
            binding.tvLeaveCounter.text = value
        }

    var counterType: CounterType = CounterType.NORMAL
        set(value) {
            field = value
            applyTypeStyle()
        }

    init {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeaveCounter, 0, 0)
        try {
            text = typedArray.getString(R.styleable.LeaveCounter_counterText)

            val typeOrdinal = typedArray.getInt(R.styleable.LeaveCounter_counterType, CounterType.NORMAL.ordinal)
            counterType = CounterType.values().getOrElse(typeOrdinal) { CounterType.NORMAL }

        } finally {
            typedArray.recycle()
        }
    }

    private fun applyTypeStyle() {
        val color = getColorFromAttr(counterType.textColorAttr)
        binding.tvLeaveCounter.setTextColor(color)
    }

    private fun getColorFromAttr(attr: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attr, typedValue, true)) {
            typedValue.data
        } else {
            R.attr.colorForegroundSecondary
        }
    }
}