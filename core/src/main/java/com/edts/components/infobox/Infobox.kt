package com.edts.components.infobox

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.edts.components.databinding.InfoboxBinding
import com.google.android.material.card.MaterialCardView
import com.edts.components.R
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
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
        clipToPadding = false
        clipChildren = false

        setCustomCornerRadius()
        initAttrs(attrs)
        applyTopShadowWithElevation()
        applyVariant(variant)
    }

    private fun setCustomCornerRadius() {
        shapeAppearanceModel = shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 12.dpToPx.toFloat())
            .setTopRightCorner(CornerFamily.ROUNDED, 12.dpToPx.toFloat())
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()
    }

    private fun applyTopShadowWithElevation() {
        val backgroundDrawable = createBackgroundWithTopShadow()
        background = backgroundDrawable
    }

    private fun createBackgroundWithTopShadow(): Drawable {
        val layers = arrayOfNulls<Drawable>(2)

        val shadowDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.TRANSPARENT,
                context.resolveColorAttribute(R.attr.colorShadowTintedAmbient, Color.TRANSPARENT),
                context.resolveColorAttribute(R.attr.colorShadowTintedKey, Color.TRANSPARENT),
                context.resolveColorAttribute(R.attr.colorShadowTintedKey, Color.TRANSPARENT),
                context.resolveColorAttribute(R.attr.colorShadowTintedKey, Color.TRANSPARENT),
                context.resolveColorAttribute(R.attr.colorShadowTintedKey, Color.TRANSPARENT),
                context.resolveColorAttribute(R.attr.colorShadowTintedKey, Color.TRANSPARENT)
            )
        ).apply {
            cornerRadii = floatArrayOf(
                0f, 0f,
                0f, 0f,
                0f, 0f,
                0f, 0f
            )
        }

        val contentDrawable = GradientDrawable().apply {
            cornerRadii = floatArrayOf(
                12.dpToPx.toFloat(), 12.dpToPx.toFloat(),
                12.dpToPx.toFloat(), 12.dpToPx.toFloat(),
                0f, 0f,
                0f, 0f
            )
            setColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        layers[0] = shadowDrawable
        layers[1] = InsetDrawable(contentDrawable, 0, 8.dpToPx, 0, 0)

        return LayerDrawable(layers)
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
        val currentBackground = background as? LayerDrawable
        val contentDrawable = currentBackground?.getDrawable(1) as? InsetDrawable
        val gradientDrawable = contentDrawable?.drawable as? GradientDrawable

        when (variant) {
            InfoBoxVariant.INFORMATION -> {
                gradientDrawable?.setColor(context.resolveColorAttribute(R.attr.colorBackgroundInfoSubtle, Color.TRANSPARENT))
                binding.tvInfoText.setTextColor(context.resolveColorAttribute(R.attr.colorForegroundInfoIntense, Color.BLACK))
                binding.ivInfoIcon.setImageResource(R.drawable.ic_information)
                binding.ivInfoIcon.setColorFilter(context.resolveColorAttribute(R.attr.colorForegroundInfoIntense, Color.BLACK))
            }
            InfoBoxVariant.SUCCESS -> {
                gradientDrawable?.setColor(context.resolveColorAttribute(R.attr.colorBackgroundSuccessSubtle, Color.TRANSPARENT))
                binding.tvInfoText.setTextColor(context.resolveColorAttribute(R.attr.colorForegroundSuccessIntense, Color.BLACK))
                binding.ivInfoIcon.setImageResource(R.drawable.ic_success)
                binding.ivInfoIcon.setColorFilter(context.resolveColorAttribute(R.attr.colorForegroundSuccessIntense, Color.BLACK))
            }
            InfoBoxVariant.ERROR -> {
                gradientDrawable?.setColor(context.resolveColorAttribute(R.attr.colorBackgroundAttentionSubtle, Color.TRANSPARENT))
                binding.tvInfoText.setTextColor(context.resolveColorAttribute(R.attr.colorForegroundAttentionIntense, Color.BLACK))
                binding.ivInfoIcon.setImageResource(R.drawable.ic_error)
                binding.ivInfoIcon.setColorFilter(context.resolveColorAttribute(R.attr.colorForegroundAttentionIntense, Color.BLACK))
            }
            InfoBoxVariant.GENERAL -> {
                gradientDrawable?.setColor(context.resolveColorAttribute(R.attr.colorBackgroundSecondary, Color.TRANSPARENT))
                binding.tvInfoText.setTextColor(context.resolveColorAttribute(R.attr.colorForegroundPrimary, Color.BLACK))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.resolveColorAttribute(R.attr.colorForegroundTertiary, Color.BLACK))
            }
        }

        invalidate()
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
}