package com.edts.components.option.card

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import com.edts.components.R
import com.edts.components.databinding.OptionCardBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.card.MaterialCardView

class OptionCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding = OptionCardBinding.inflate(LayoutInflater.from(context), this, true)

    var delegate: OptionCardDelegate? = null

    var titleText: CharSequence? = null
        set(value) {
            field = value
            binding.tvOptionTitle.text = value
        }

    var iconDrawable: Drawable? = null
        set(value) {
            field = value
            binding.ivOptionAction.setImageDrawable(value)
        }

    var iconResource: Int? = null
        set(value) {
            field = value
            value?.let { binding.ivOptionAction.setImageResource(it) }
        }

    init {
        initAttrs(attrs)
        setupCardAppearance(context)
    }

    override fun performClick(): Boolean {
        delegate?.onClick(this)
        return super.performClick()
    }

    private fun setupClickAnimation() {
        isClickable = true
        isFocusable = true

        val activeColor = context.resolveColorAttr(
            R.attr.colorBackgroundModifierOnPress,
            android.R.color.transparent
        )
        rippleColor = ColorStateList.valueOf(activeColor)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionCard)
            try {
                titleText = typedArray.getString(R.styleable.OptionCard_cardText)
                iconDrawable = typedArray.getDrawable(R.styleable.OptionCard_cardIcon)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun setupCardAppearance(context: Context) {
        strokeWidth = 1.dpToPx
        radius = context.resources.getDimension(R.dimen.radius_12dp)
        strokeColor = context.resolveColorAttr(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
        cardElevation = 1f.dpToPx

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = context.resolveColorAttr(
                R.attr.colorForegroundPrimary,
                android.R.color.transparent
            )
            outlineSpotShadowColor = context.resolveColorAttr(
                R.attr.colorForegroundPrimary,
                android.R.color.transparent
            )
        }
        setupClickAnimation()
    }
}