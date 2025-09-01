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

/**
 * A custom UI component that serves as a clickable dropdown filter.
 *
 * This component extends MaterialCardView to create a self-contained, reusable UI element.
 * It displays a title and an optional description, providing visual feedback on press.
 *
 */
class DropdownFilterHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    // Note: The default style attribute is now the one for MaterialCardView
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: DropdownFilterHorizontalBinding

    /**
     * The delegate responsible for handling click events on this dropdown.
     * Assign an object that implements [DropdownFilterHorizontalDelegate] to receive callbacks.
     */
    var dropdownFilterHorizontalDelegate: DropdownFilterHorizontalDelegate? = null

    /** The main title text of the dropdown. */
    var title: String? = null
        set(value) {
            field = value
            binding.tvTitleLabel.text = value
        }

    /** The descriptive text of the dropdown. If empty, the dot separator is hidden. */
    var description: String? = null
        set(value) {
            field = value
            binding.tvDescriptionLabel.text = value
            binding.tvDotSpacer.isVisible = !value.isNullOrEmpty()
        }

    init {
        // Inflate the layout and attach it as a child of this MaterialCardView.
        binding = DropdownFilterHorizontalBinding.inflate(LayoutInflater.from(context), this, true)

        // Programmatically set the card's core appearance for better encapsulation.
        setupCardAppearance()

        // Ensure the view is clickable and can receive focus.
        isClickable = true
        isFocusable = true

        // Parse custom XML attributes.
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

    /**
     * Configures the visual properties of the card like elevation, stroke, and corners.
     * This centralizes styling within the component, making it self-contained.
     */
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

        // Set shadow colors only for API 28+
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

    /**
     * Performs the click action and notifies the delegate.
     */
    override fun performClick(): Boolean {
        super.performClick()
        dropdownFilterHorizontalDelegate?.onClick(this)
        return true
    }
}