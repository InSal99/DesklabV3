package com.example.components.card.multi.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.components.R
import com.example.components.databinding.CardMultiDetailWrapperInfoBinding

class CardMultiDetailWrapperInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CardMultiDetailWrapperInfoBinding = CardMultiDetailWrapperInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var info1Text: String? = null
        set(value) {
            field = value
            updateInfo1Text()
        }

    var info2Text: String? = null
        set(value) {
            field = value
            updateInfo2Text()
        }

    var showInfo2: Boolean = true
        set(value) {
            field = value
            updateInfo2Visibility()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardMultiDetailWrapperInfo,
            0, 0
        ).apply {
            try {
                info1Text = getString(R.styleable.CardMultiDetailWrapperInfo_info1Text)
                info2Text = getString(R.styleable.CardMultiDetailWrapperInfo_info2Text)

                // Handle showInfo2 attribute
                showInfo2 = getBoolean(R.styleable.CardMultiDetailWrapperInfo_showInfo2, true)

                updateInfo1Text()
                updateInfo2Text()
                updateInfo2Visibility()
                updateInfo1Constraints()
            } finally {
                recycle()
            }
        }
    }

    private fun updateInfo1Text() {
        info1Text?.let {
            binding.tvInfo1.text = it
        }
    }

    private fun updateInfo2Text() {
        info2Text?.let {
            binding.tvInfo2.text = it
        }
    }

    private fun updateInfo2Visibility() {
        binding.tvInfo2.visibility = if (showInfo2) View.VISIBLE else View.GONE
        binding.ivDivider.visibility = if (showInfo2) View.VISIBLE else View.GONE
        updateInfo1Constraints()
    }

    private fun updateInfo1Constraints() {
        val info1LayoutParams = binding.tvInfo1.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

        if (showInfo2) {
            // When info2 is visible, constrain info1 end to divider start
            info1LayoutParams.endToStart = binding.ivDivider.id
            info1LayoutParams.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
        } else {
            // When info2 is hidden, constrain info1 end to parent end
            info1LayoutParams.endToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
            info1LayoutParams.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        }

        binding.tvInfo1.layoutParams = info1LayoutParams
    }
}