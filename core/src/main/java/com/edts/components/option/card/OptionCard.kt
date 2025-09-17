package com.edts.components.option.card

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.OptionCardBinding
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
        Log.d("OptionCard", "option card selected")
        delegate?.onClick(this)
        return super.performClick()
    }

    private fun setupClickAnimation() {
        isClickable = true
        isFocusable = true

        val activeColor = resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.color000Opacity5)
        rippleColor = ColorStateList.valueOf(activeColor)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.OptionCard).use {
                titleText = it.getString(R.styleable.OptionCard_cardText)
                iconDrawable = it.getDrawable(R.styleable.OptionCard_cardIcon)
            }
        }
    }

    private fun setupCardAppearance(context: Context) {
        strokeWidth = (1 * context.resources.displayMetrics.density).toInt()
        radius = context.resources.getDimension(R.dimen.radius_12dp)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorStrokeSubtle, typedValue, true)
        setStrokeColor(ContextCompat.getColor(context, typedValue.resourceId))

        cardElevation = 2f * context.resources.displayMetrics.density

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = resolveColorAttribute(
                R.attr.colorShadowNeutralAmbient,
                R.color.colorGreen50
            )
            outlineSpotShadowColor = resolveColorAttribute(
                R.attr.colorShadowNeutralKey,
                R.color.colorGreen50
            )
        }

        setupClickAnimation()
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
}