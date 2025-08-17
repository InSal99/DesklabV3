package com.example.components.footer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.components.R
import com.example.components.button.CustomButton
import com.example.components.status.badge.StatusBadge

class CustomFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Delegate property
    var delegate: CustomFooterDelegate? = null

    // Your existing properties...
    private var footerType: FooterType = FooterType.CALL_TO_ACTION
    private var primaryButtonText: String = "Daftar Sekarang"
    private var secondaryButtonText: String = "Button Label"

    // The undeclared variables you asked for
    private var footerTitle: String = ""
    private var footerDescription: String = ""
    private var statusText: String = ""
    private var statusType: StatusBadge.ChipType = StatusBadge.ChipType.APPROVED

    // View references for click handling
    private var primaryButton: CustomButton? = null
    private var secondaryButton: CustomButton? = null
    private var statusBadge: StatusBadge? = null

    enum class FooterType(val value: Int) {
        CALL_TO_ACTION(0), CALL_TO_ACTION_DETAIL(1), DUAL_BUTTON(2), NO_ACTION(3);
        companion object {
            fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: CALL_TO_ACTION
        }
    }

    init {
        orientation = VERTICAL
        parseAttributes(attrs)
        setupView()
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFooter, 0, 0)
        try {
            // Read footerType and convert it from int to enum
            val typeOrdinal = typedArray.getInt(R.styleable.CustomFooter_footerType, FooterType.CALL_TO_ACTION.value)
            footerType = FooterType.fromInt(typeOrdinal)

            // Read button and text attributes
            primaryButtonText = typedArray.getString(R.styleable.CustomFooter_primaryButtonText) ?: primaryButtonText
            secondaryButtonText = typedArray.getString(R.styleable.CustomFooter_secondaryButtonText) ?: secondaryButtonText
            footerTitle = typedArray.getString(R.styleable.CustomFooter_footerTitle) ?: ""
            footerDescription = typedArray.getString(R.styleable.CustomFooter_footerDescription) ?: ""
            statusText = typedArray.getString(R.styleable.CustomFooter_statusBadgeText) ?: ""

            // Read the status badge type
            val statusTypeOrdinal = typedArray.getInt(R.styleable.CustomFooter_statusBadgeType, StatusBadge.ChipType.APPROVED.ordinal)
            statusType = StatusBadge.ChipType.entries.toTypedArray().getOrElse(statusTypeOrdinal) { StatusBadge.ChipType.APPROVED }

        } finally {
            typedArray.recycle() // Always recycle the TypedArray to free up resources
        }
    }

    private fun setupView() {
        removeAllViews()

        val layoutRes = when (footerType) {
            FooterType.CALL_TO_ACTION -> R.layout.footer_call_to_action
            FooterType.CALL_TO_ACTION_DETAIL -> R.layout.footer_call_to_action_description
            FooterType.DUAL_BUTTON -> R.layout.footer_dual_button
            FooterType.NO_ACTION -> R.layout.footer_no_action
        }

        LayoutInflater.from(context).inflate(layoutRes, this, true)
        bindDataToViews()
        setupClickListeners()
    }

    private fun bindDataToViews() {
        when (footerType) {
            FooterType.CALL_TO_ACTION -> {
                primaryButton = findViewById<CustomButton>(R.id.btnFooterCTAPrimary)?.apply {
                    text = primaryButtonText
                }
            }

            FooterType.CALL_TO_ACTION_DETAIL -> {
                findViewById<TextView>(R.id.tvFooterCTADescTitle)?.text = footerTitle
                findViewById<TextView>(R.id.tvFooterCTADescDescription)?.text = footerDescription
                primaryButton = findViewById<CustomButton>(R.id.btnFooterCTADescPrimary)?.apply {
                    setLabel(primaryButtonText)
                }
            }

            FooterType.DUAL_BUTTON -> {
                secondaryButton = findViewById<CustomButton>(R.id.btnFooterDualButtonSecondary)?.apply {
                    setLabel(secondaryButtonText)
                }
                primaryButton = findViewById<CustomButton>(R.id.btnFooterDualButtonPrimary)?.apply {
                    setLabel(primaryButtonText)
                }
            }

            FooterType.NO_ACTION -> {
                findViewById<TextView>(R.id.tvFooterNoActionTitle)?.text = footerTitle
                findViewById<TextView>(R.id.tvFooterNoActionDescription)?.text = footerDescription
                statusBadge = findViewById<StatusBadge>(R.id.bFooterNoActionStatus)?.apply {
                    text = statusText
                    chipType = statusType
                }
            }
        }
    }

    private fun setupClickListeners() {
        primaryButton?.setOnClickListener {
            handlePrimaryButtonClick()
        }

        secondaryButton?.setOnClickListener {
            handleSecondaryButtonClick()
        }
    }

    private fun handlePrimaryButtonClick() {
        delegate?.onPrimaryButtonClicked(footerType)

        when (footerType) {
            FooterType.CALL_TO_ACTION,
            FooterType.CALL_TO_ACTION_DETAIL -> {
                delegate?.onRegisterClicked()
            }
            FooterType.DUAL_BUTTON -> {
                delegate?.onContinueClicked()
            }
            FooterType.NO_ACTION -> {}
        }
    }

    private fun handleSecondaryButtonClick() {
        delegate?.onSecondaryButtonClicked(footerType)

        when (footerType) {
            FooterType.DUAL_BUTTON -> {
                delegate?.onCancelClicked()
            }
            else -> {}
        }
    }

    fun setPrimaryButtonEnabled(enabled: Boolean) {
        primaryButton?.isEnabled = enabled
    }

    fun setSecondaryButtonEnabled(enabled: Boolean) {
        secondaryButton?.isEnabled = enabled
    }

    fun setPrimaryButtonLoading(isLoading: Boolean) {
        primaryButton?.apply {
            isEnabled = !isLoading
            text = if (isLoading) "Loading..." else primaryButtonText
        }
    }
}