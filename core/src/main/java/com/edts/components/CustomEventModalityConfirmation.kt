package com.edts.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.edts.components.databinding.CustomEventModalityConfirmationBinding

class CustomEventModalityConfirmation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomEventModalityConfirmationBinding

    /**
     * The delegate responsible for handling click events on this component.
     * Assign an object that implements [CustomEventModalityConfirmationDelegate] to receive callbacks.
     */
    var delegate: CustomEventModalityConfirmationDelegate? = null

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
        binding = CustomEventModalityConfirmationBinding.inflate(inflater, this, true)

        attrs?.let {
            context.withStyledAttributes(
                it,
                R.styleable.CustomEventModalityConfirmation,
                0,
                0
            ) {
                modalTitle = getString(R.styleable.CustomEventModalityConfirmation_modalTitle)
                modalDescription =
                    getString(R.styleable.CustomEventModalityConfirmation_modalDescription)
                modalCloseButtonText =
                    getString(R.styleable.CustomEventModalityConfirmation_modalCloseButtonLabel)
                modalConfirmButtonText =
                    getString(R.styleable.CustomEventModalityConfirmation_modalConfirmButtonLabel)
            }
        }

        // Set up click listeners to notify the delegate
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