package com.edts.components.modal

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.EventModalityLoadingBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

/**
 * A custom view that displays a loading indicator and a text message within a MaterialCardView.
 *
 * This component is designed to be used as a modality overlay to indicate a background
 * process is running. The text can be customized via the `app:modalLoadingTitle` attribute in XML
 * or programmatically by setting the `title` property.
 *
 */
class EventModalityLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventModalityLoadingBinding = EventModalityLoadingBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * The title text for the loading modal. Setting this property will update the
     * displayed text.
     */
    var title: String? = null
        set(value) {
            field = value
            binding.tvModalTitle.text = value
        }

    init {

        setupCardAppearance()
        parseAttributes(attrs)
    }

    /**
     * Configures the visual appearance of the MaterialCardView.
     */
    private fun setupCardAppearance() {
        cardElevation = resources.getDimension(R.dimen.margin_2dp)
        radius = resources.getDimension(R.dimen.radius_16dp)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        strokeColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)

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
     * Parses custom attributes from the XML layout.
     * @param attrs The set of attributes from the XML tag that is inflating the view.
     */
    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.withStyledAttributes(
                it,
                R.styleable.EventModalityLoading,
                0,
                0
            ) {
                title = getString(R.styleable.EventModalityLoading_modalLoadingTitle)
            }
        }
    }
}