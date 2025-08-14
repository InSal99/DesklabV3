package com.example.components.leave.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.components.R
import com.example.components.databinding.LeaveDescriptionBinding

class LeaveDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = LeaveDescriptionBinding.inflate(LayoutInflater.from(context), this)

    var counterText: CharSequence?
        get() = binding.lcDescriptionCounter.text
        set(value) {
            binding.lcDescriptionCounter.text = value
        }

    var counterType: LeaveCounter.CounterType
        get() = binding.lcDescriptionCounter.counterType
        set(value) {
            binding.lcDescriptionCounter.counterType = value
        }

    init {
        orientation = HORIZONTAL
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeaveDescription, 0, 0)

        try {
            counterText = typedArray.getString(R.styleable.LeaveDescription_counterText)

            val typeOrdinal = typedArray.getInt(R.styleable.LeaveDescription_counterType, LeaveCounter.CounterType.NORMAL.ordinal)
            counterType = LeaveCounter.CounterType.values().getOrElse(typeOrdinal) { LeaveCounter.CounterType.NORMAL }
        } finally {
            typedArray.recycle()
        }
    }
}