package com.edts.components.dropdown.filter

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.DropdownFilterHorizontalBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class DropdownFilterHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: DropdownFilterHorizontalBinding =
        DropdownFilterHorizontalBinding.inflate(LayoutInflater.from(context), this, true)

    var dropdownFilterHorizontalDelegate: DropdownFilterHorizontalDelegate? = null

    var title: String? = null
        set(value) {
            field = value
            binding.tvTitleLabel.text = value
        }

    var description: String? = null
        set(value) {
            field = value
            binding.tvDescriptionLabel.text = value
            binding.tvDotSpacer.isVisible = !value.isNullOrEmpty()
        }

    init {
        setupCardAppearance()
        applyStyledAttributes(attrs)
        setupClickListener()
    }

    private fun setupClickListener() {
        setOnClickListener {
            dropdownFilterHorizontalDelegate?.onClick(this)
        }
    }

    private fun applyStyledAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.DropdownFilterHorizontal, 0, 0) {
            title = getString(R.styleable.DropdownFilterHorizontal_dropdownTitle)
            description = getString(R.styleable.DropdownFilterHorizontal_dropdownDescription)
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = context.resolveColorAttribute(
            R.attr.colorStrokeSubtle,
            R.color.kitColorNeutralGrayLight30
        )
        val rippleColor = context.resolveColorAttribute(
            R.attr.colorBackgroundModifierOnPress,
            R.color.kitColorNeutralGrayLight20
        )

        val cornerRadius = 999f.dpToPx
        val strokeWidth = 1.dpToPx
        val elevation = 1f.dpToPx

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth
        this.rippleColor = ColorStateList.valueOf(rippleColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = context.resolveColorAttribute(
                R.attr.colorForegroundPrimary,
                R.color.kitColorNeutralBlack
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }
}