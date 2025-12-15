package com.edts.components.card.multi.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.marginLeft
import com.edts.components.R
import com.edts.components.databinding.CardMultiDetailWrapperInfoBinding
import com.edts.components.utils.dpToPx

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
        updateInfo1Width()
    }

    private fun updateInfo1Constraints() {
        val info1LayoutParams = binding.tvInfo1.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

        if (showInfo2) {
            info1LayoutParams.endToStart = binding.ivDivider.id
            info1LayoutParams.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
        } else {
            info1LayoutParams.endToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
            info1LayoutParams.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        }

        binding.tvInfo1.layoutParams = info1LayoutParams
    }

    private fun updateInfo1Width() {
        binding.tvInfo1.post {
            val wrapperWidth = binding.cardMultiDetailWrapperInfo.measuredWidth
            val info1Width = binding.tvInfo1.measuredWidth
            val info2Width = binding.tvInfo2.measuredWidth
            val dividerWidth = binding.ivDivider.measuredWidth
            val dividerMargin = (12f * resources.displayMetrics.density).toInt()
            val contentWidth = info1Width + info2Width + dividerWidth + dividerMargin

            if (showInfo2) {
                if (wrapperWidth > contentWidth){
                    binding.tvInfo1.maxWidth = info1Width
                }
                if (wrapperWidth < contentWidth){
                    binding.tvInfo1.maxWidth = wrapperWidth - info2Width - dividerWidth - dividerMargin
                }
            } else {
                binding.tvInfo1.maxWidth = wrapperWidth
            }
        }
    }
}