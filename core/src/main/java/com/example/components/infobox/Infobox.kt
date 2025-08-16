package com.example.components.infobox

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.components.databinding.InfoboxBinding
import com.google.android.material.card.MaterialCardView
import com.example.components.R
import com.google.android.material.shape.CornerFamily

class InfoBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = InfoboxBinding.inflate(LayoutInflater.from(context), this, true)

    var text: CharSequence? = null
        set(value) {
            field = value
            binding.tvInfoText.text = value
        }

    var variant: InfoBoxVariant = InfoBoxVariant.INFORMATION
        set(value) {
            field = value
            applyVariant(value)
        }

    init {
        cardElevation = 0f
        setCustomCornerRadius()
        initAttrs(attrs)
    }

    private fun setCustomCornerRadius() {
        shapeAppearanceModel = shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 12.dp)
            .setTopRightCorner(CornerFamily.ROUNDED, 12.dp)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.InfoBox).use {
                text = it.getString(R.styleable.InfoBox_text)
                variant = InfoBoxVariant.fromValue(it.getInt(R.styleable.InfoBox_variant, 0))
            }
        }
    }

    private fun applyVariant(variant: InfoBoxVariant) {
        when (variant) {
            InfoBoxVariant.INFORMATION -> {
                setCardBackgroundColor(context.getThemeColor(R.attr.colorBackgroundInfoSubtle))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundInfoIntense))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundInfoIntense))
            }
            InfoBoxVariant.SUCCESS -> {
                setCardBackgroundColor(context.getThemeColor(R.attr.colorBackgroundSuccessSubtle))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundSuccessIntense))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundSuccessIntense))
            }
            InfoBoxVariant.ERROR -> {
                setCardBackgroundColor(context.getThemeColor(R.attr.colorBackgroundAttentionSubtle))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundAttentionIntense))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundAttentionIntense))
            }
            InfoBoxVariant.GENERAL -> {
                setCardBackgroundColor(context.getThemeColor(R.attr.colorBackgroundSecondary))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundPrimary))
                binding.ivInfoIcon.setImageResource(R.drawable.ic_star)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundTertiary))
            }
        }
    }

    private fun Context.getThemeColor(attrResId: Int): Int {
        val typedArray = obtainStyledAttributes(intArrayOf(attrResId))
        val color = typedArray.getColor(0, Color.TRANSPARENT)
        typedArray.recycle()
        return color
    }

    enum class InfoBoxVariant(val value: Int) {
        INFORMATION(0),
        SUCCESS(1),
        ERROR(2),
        GENERAL(3);

        companion object {
            fun fromValue(value: Int): InfoBoxVariant {
                return values().find { it.value == value } ?: INFORMATION
            }
        }
    }

    private val Int.dp: Float
        get() = this * resources.displayMetrics.density
}

