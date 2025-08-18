package com.example.components.card.multi.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardMultiDetailWrapperInfo,
            0, 0
        ).apply {
            try {
                info1Text = getString(R.styleable.CardMultiDetailWrapperInfo_info1Text)
                info2Text = getString(R.styleable.CardMultiDetailWrapperInfo_info2Text)

                updateInfo1Text()
                updateInfo2Text()
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
}