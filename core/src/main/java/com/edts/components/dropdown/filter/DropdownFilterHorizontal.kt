package com.edts.components.dropdown.filter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.DropdownFilterHorizontalBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

class DropdownFilterHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: DropdownFilterHorizontalBinding

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
        binding = DropdownFilterHorizontalBinding.inflate(LayoutInflater.from(context), this, true)

        setupCardAppearance()

        isClickable = true
        isFocusable = true

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.DropdownFilterHorizontal,
                defStyleAttr,
                0
            )
            try {
                title = typedArray.getString(R.styleable.DropdownFilterHorizontal_dropdownTitle)
                description = typedArray.getString(R.styleable.DropdownFilterHorizontal_dropdownDescription)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)
        val rippleColor = MaterialColors.getColor(this, R.attr.colorBackgroundModifierOnPress)
        val cornerRadius = resources.getDimension(R.dimen.radius_999dp)
        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        val elevation = resources.getDimension(R.dimen.dimen_1dp)

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth
        this.rippleColor = ColorStateList.valueOf(rippleColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = MaterialColors.getColor(
                context,
                R.attr.colorForegroundPrimary,
                Color.BLACK
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        dropdownFilterHorizontalDelegate?.onClick(this)
        return true
    }
}