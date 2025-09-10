package com.edts.components.infobox

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.edts.components.databinding.InfoboxBinding
import com.google.android.material.card.MaterialCardView
import com.edts.components.R
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
            .setTopLeftCorner(CornerFamily.ROUNDED, 12.dp)
            .setTopRightCorner(CornerFamily.ROUNDED, 12.dp)
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
                context.getThemeColor(R.attr.colorShadowTintedAmbient),
                context.getThemeColor(R.attr.colorShadowTintedKey),
                context.getThemeColor(R.attr.colorShadowTintedKey),
                context.getThemeColor(R.attr.colorShadowTintedKey),
                context.getThemeColor(R.attr.colorShadowTintedKey),
                context.getThemeColor(R.attr.colorShadowTintedKey),
                context.getThemeColor(R.attr.colorShadowTintedKey)
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
                12.dp, 12.dp,
                12.dp, 12.dp,
                0f, 0f,
                0f, 0f
            )
            setColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        layers[0] = shadowDrawable
        layers[1] = InsetDrawable(contentDrawable, 0, 8.dp.toInt(), 0, 0) // Increased offset

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
                gradientDrawable?.setColor(context.getThemeColor(R.attr.colorBackgroundInfoSubtle))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundInfoIntense))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundInfoIntense))
            }
            InfoBoxVariant.SUCCESS -> {
                gradientDrawable?.setColor(context.getThemeColor(R.attr.colorBackgroundSuccessSubtle))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundSuccessIntense))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundSuccessIntense))
            }
            InfoBoxVariant.ERROR -> {
                gradientDrawable?.setColor(context.getThemeColor(R.attr.colorBackgroundAttentionSubtle))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundAttentionIntense))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundAttentionIntense))
            }
            InfoBoxVariant.GENERAL -> {
                gradientDrawable?.setColor(context.getThemeColor(R.attr.colorBackgroundSecondary))
                binding.tvInfoText.setTextColor(context.getThemeColor(R.attr.colorForegroundPrimary))
                binding.ivInfoIcon.setImageResource(R.drawable.placeholder)
                binding.ivInfoIcon.setColorFilter(context.getThemeColor(R.attr.colorForegroundTertiary))
            }
        }

        invalidate()
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

    private val Float.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}