package com.edts.components.modal

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.EventModalityConfirmationBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

class EventModalityConfirmation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: EventModalityConfirmationBinding

    /**
     * The delegate responsible for handling click events on this component.
     * Assign an object that implements [EventModalityConfirmationDelegate] to receive callbacks.
     */
    var delegate: EventModalityConfirmationDelegate? = null

    var modalTitle: String? = null
        set(value) {
            field = value
            binding.tvModalityTitle.text = value
        }

    var modalDescription: String? = null
        set(value) {
            field = value
            binding.tvModalityDescription.text = value
        }

    var modalCloseButtonText: String? = null
        set(value) {
            field = value
            binding.btnModalityClose.text = value
        }

    var modalConfirmButtonText: String? = null
        set(value) {
            field = value
            binding.btnModalityConfirm.text = value
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = EventModalityConfirmationBinding.inflate(inflater, this, true)

        cardElevation = resources.getDimension(R.dimen.margin_2dp)
        radius = resources.getDimension(R.dimen.radius_16dp)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        strokeColor = MaterialColors.getColor(
            context,
            R.attr.colorStrokeSubtle,
            Color.GRAY
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = MaterialColors.getColor(
                context,
                R.attr.colorForegroundPrimary,
                Color.BLACK
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }


        attrs?.let {
            context.withStyledAttributes(
                it,
                R.styleable.EventModalityConfirmation,
                0,
                0
            ) {
                modalTitle = getString(R.styleable.EventModalityConfirmation_modalTitle)
                modalDescription =
                    getString(R.styleable.EventModalityConfirmation_modalDescription)
                modalCloseButtonText =
                    getString(R.styleable.EventModalityConfirmation_modalCloseButtonLabel)
                modalConfirmButtonText =
                    getString(R.styleable.EventModalityConfirmation_modalConfirmButtonLabel)
            }
        }

        binding.btnModalityConfirm.setOnClickListener {
            Log.d("EventModalityConfirmation", "Confirm Button Clicked ✅")
            delegate?.onConfirmClick(this)
        }

        binding.btnModalityClose.setOnClickListener {
            Log.d("EventModalityConfirmation", "Close Button Clicked ✅")
            delegate?.onCloseClick(this)
        }
    }
}