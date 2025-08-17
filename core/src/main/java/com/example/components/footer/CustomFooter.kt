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

    enum class FooterType {
        CALL_TO_ACTION, CALL_TO_ACTION_DETAIL, DUAL_BUTTON, NO_ACTION
    }

    init {
        orientation = VERTICAL
//        parseAttributes(attrs)
        setupView()
    }

    // Your existing parseAttributes method...

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

                // Make attendance container clickable if needed
//                attendanceContainer = findViewById(R.id.attendanceContainer)
            }
        }
    }

    private fun setupClickListeners() {
        // Primary button click
        primaryButton?.setOnClickListener {
            handlePrimaryButtonClick()
        }

        // Secondary button click
        secondaryButton?.setOnClickListener {
            handleSecondaryButtonClick()
        }

        // Attendance container click (if applicable)
//        attendanceContainer?.setOnClickListener {
//            delegate?.onAttendanceInfoClicked()
//        }
    }

    private fun handlePrimaryButtonClick() {
        // Call generic delegate method
        delegate?.onPrimaryButtonClicked(footerType)

        // Call specific delegate methods based on type
        when (footerType) {
            FooterType.CALL_TO_ACTION,
            FooterType.CALL_TO_ACTION_DETAIL -> {
                delegate?.onRegisterClicked()
            }
            FooterType.DUAL_BUTTON -> {
                delegate?.onContinueClicked()
            }
            FooterType.NO_ACTION -> {
                // Handle if needed
            }
        }
    }

    private fun handleSecondaryButtonClick() {
        // Call generic delegate method
        delegate?.onSecondaryButtonClicked(footerType)

        // Call specific delegate methods based on type
        when (footerType) {
            FooterType.DUAL_BUTTON -> {
                delegate?.onCancelClicked()
            }
            else -> {
                // Handle other cases if needed
            }
        }
    }

    // Public methods to enable/disable buttons
    fun setPrimaryButtonEnabled(enabled: Boolean) {
        primaryButton?.isEnabled = enabled
    }

    fun setSecondaryButtonEnabled(enabled: Boolean) {
        secondaryButton?.isEnabled = enabled
    }

    // Loading state management
    fun setPrimaryButtonLoading(isLoading: Boolean) {
        primaryButton?.apply {
            isEnabled = !isLoading
            text = if (isLoading) {
                "Loading..."
            } else {
                primaryButtonText
            }
        }
    }
}