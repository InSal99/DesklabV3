package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.components.databinding.CustomEventModalityConfirmationBinding
import androidx.core.content.withStyledAttributes

class CustomEventModalityConfirmation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CustomEventModalityConfirmationBinding

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
    }

    /**
     * Sets the click listener for the confirm button.
     * @param listener The lambda function to be executed on click.
     */
    fun setOnConfirmClickListener(listener: () -> Unit) {
        binding.btnModalityConfirm.setOnClickListener { listener() }
    }

    /**
     * Sets the click listener for the close button.
     * @param listener The lambda function to be executed on click.
     */
    fun setOnCloseClickListener(listener: () -> Unit) {
        binding.btnModalityClose.setOnClickListener { listener() }
    }
}
