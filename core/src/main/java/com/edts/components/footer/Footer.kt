package com.edts.components.footer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.edts.components.R
import com.edts.components.button.Button
import com.edts.components.status.badge.StatusBadge

class Footer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var delegate: FooterDelegate? = null

    private var footerType: FooterType = FooterType.CALL_TO_ACTION
    private var primaryButtonText: String = "Daftar Sekarang"
    private var secondaryButtonText: String = "Button Label"
    private var footerTitle: String = ""
    private var footerDescription: String = ""
    private var statusText: String = ""
    private var statusType: StatusBadge.ChipType = StatusBadge.ChipType.APPROVED

    // New properties for dual button description
    private var showDescription: Boolean = false
    private var dualButtonTitle: String = ""
    private var dualButtonSupportText1: String = ""
    private var dualButtonSupportText2: String = ""

    private var primaryButtonEnabled: Boolean = true
    private var secondaryButtonEnabled: Boolean = true

    private var primaryButton: Button? = null
    private var secondaryButton: Button? = null
    private var statusBadge: StatusBadge? = null

    // References to the new text views
    private var tvDualButtonTitle: TextView? = null
    private var tvDualButtonSupportText1: TextView? = null
    private var tvDualButtonTextDivider: TextView? = null
    private var tvDualButtonSupportText2: TextView? = null

    enum class FooterType(val value: Int) {
        CALL_TO_ACTION(0),
        CALL_TO_ACTION_DETAIL(1),
        DUAL_BUTTON(2),
        NO_ACTION(3);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: CALL_TO_ACTION
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
            val typeOrdinal = typedArray.getInt(R.styleable.CustomFooter_footerType, FooterType.CALL_TO_ACTION.value)
            footerType = FooterType.fromInt(typeOrdinal)

            primaryButtonText = typedArray.getString(R.styleable.CustomFooter_primaryButtonText) ?: primaryButtonText
            secondaryButtonText = typedArray.getString(R.styleable.CustomFooter_secondaryButtonText) ?: secondaryButtonText
            footerTitle = typedArray.getString(R.styleable.CustomFooter_footerTitle) ?: ""
            footerDescription = typedArray.getString(R.styleable.CustomFooter_footerDescription) ?: ""
            statusText = typedArray.getString(R.styleable.CustomFooter_statusBadgeText) ?: ""

            val statusTypeOrdinal = typedArray.getInt(R.styleable.CustomFooter_statusBadgeType, StatusBadge.ChipType.APPROVED.ordinal)
            statusType = StatusBadge.ChipType.values().getOrElse(statusTypeOrdinal) { StatusBadge.ChipType.APPROVED }

            primaryButtonEnabled = typedArray.getBoolean(R.styleable.CustomFooter_primaryButtonEnabled, true)
            secondaryButtonEnabled = typedArray.getBoolean(R.styleable.CustomFooter_secondaryButtonEnabled, true)

            // New attributes for dual button description
//            showDescription = typedArray.getBoolean(R.styleable.CustomFooter_showDescription, false)
//            dualButtonTitle = typedArray.getString(R.styleable.CustomFooter_dualButtonTitle) ?: ""
//            dualButtonSupportText1 = typedArray.getString(R.styleable.CustomFooter_dualButtonSupportText1) ?: ""
//            dualButtonSupportText2 = typedArray.getString(R.styleable.CustomFooter_dualButtonSupportText2) ?: ""

        } finally {
            typedArray.recycle()
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
                primaryButton = findViewById<Button>(R.id.btnFooterCTAPrimary)?.apply {
                    setLabel(primaryButtonText)
                    isEnabled = primaryButtonEnabled
                }
            }

            FooterType.CALL_TO_ACTION_DETAIL -> {
                findViewById<TextView>(R.id.tvFooterCTADescTitle)?.text = footerTitle
                findViewById<TextView>(R.id.tvFooterCTADescDescription)?.text = footerDescription
                primaryButton = findViewById<Button>(R.id.btnFooterCTADescPrimary)?.apply {
                    setLabel(primaryButtonText)
                    isEnabled = primaryButtonEnabled
                }
            }

            FooterType.DUAL_BUTTON -> {
                // Initialize text view references
                tvDualButtonTitle = findViewById(R.id.tvFooterDualButtonTitle)
                tvDualButtonSupportText1 = findViewById(R.id.tvFooterDualButtonSupportText1)
                tvDualButtonTextDivider = findViewById(R.id.tvFooterDualButtonTextDivider)
                tvDualButtonSupportText2 = findViewById(R.id.tvFooterDualButtonSupportText2)

                // Set text values
                tvDualButtonTitle?.text = dualButtonTitle
                tvDualButtonSupportText1?.text = dualButtonSupportText1
                tvDualButtonSupportText2?.text = dualButtonSupportText2

                // Set visibility based on showDescription
                setDescriptionVisibility(showDescription)

                secondaryButton = findViewById<Button>(R.id.btnFooterDualButtonSecondary)?.apply {
                    setLabel(secondaryButtonText)
                    isEnabled = secondaryButtonEnabled
                }
                primaryButton = findViewById<Button>(R.id.btnFooterDualButtonPrimary)?.apply {
                    setLabel(primaryButtonText)
                    isEnabled = primaryButtonEnabled
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

    fun setFooterType(type: FooterType) {
        if (footerType != type) {
            footerType = type
            setupView()
        }
    }

    fun setPrimaryButtonText(text: String) {
        primaryButtonText = text
        primaryButton?.setLabel(text)
    }

    fun setSecondaryButtonText(text: String) {
        secondaryButtonText = text
        secondaryButton?.setLabel(text)
    }

    fun setTitleAndDescription(title: String, description: String) {
        footerTitle = title
        footerDescription = description
        findViewById<TextView>(R.id.tvFooterCTADescTitle)?.text = title
        findViewById<TextView>(R.id.tvFooterCTADescDescription)?.text = description
        findViewById<TextView>(R.id.tvFooterNoActionTitle)?.text = title
        findViewById<TextView>(R.id.tvFooterNoActionDescription)?.text = description
    }

    fun setStatusBadge(text: String, type: StatusBadge.ChipType) {
        statusText = text
        statusType = type
        statusBadge?.apply {
            this.text = text
            chipType = type
        }
    }

    fun setPrimaryButtonEnabled(enabled: Boolean) {
        primaryButtonEnabled = enabled
        primaryButton?.isEnabled = enabled
    }

    fun setSecondaryButtonEnabled(enabled: Boolean) {
        secondaryButtonEnabled = enabled
        secondaryButton?.isEnabled = enabled
    }

    // New function to control description visibility
    fun setDescriptionVisibility(showDescription: Boolean) {
        this.showDescription = showDescription

        val visibility = if (showDescription) View.VISIBLE else View.GONE

        tvDualButtonTitle?.visibility = visibility
        tvDualButtonSupportText1?.visibility = visibility
        tvDualButtonTextDivider?.visibility = visibility
        tvDualButtonSupportText2?.visibility = visibility
    }

    // New function to set dual button description data
    fun setDualButtonDescription(title: String, supportText1: String, supportText2: String) {
        dualButtonTitle = title
        dualButtonSupportText1 = supportText1
        dualButtonSupportText2 = supportText2

        tvDualButtonTitle?.text = title
        tvDualButtonSupportText1?.text = supportText1
        tvDualButtonSupportText2?.text = supportText2
    }

    // Getter functions for the new properties
    fun isDescriptionVisible(): Boolean = showDescription

    fun getDualButtonTitle(): String = dualButtonTitle

    fun getDualButtonSupportText1(): String = dualButtonSupportText1

    fun getDualButtonSupportText2(): String = dualButtonSupportText2
}



//class Footer @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : LinearLayout(context, attrs, defStyleAttr) {
//
//    var delegate: FooterDelegate? = null
//
//    private var footerType: FooterType = FooterType.CALL_TO_ACTION
//    private var primaryButtonText: String = "Daftar Sekarang"
//    private var secondaryButtonText: String = "Button Label"
//    private var footerTitle: String = ""
//    private var footerDescription: String = ""
//    private var statusText: String = ""
//    private var statusType: StatusBadge.ChipType = StatusBadge.ChipType.APPROVED
//
//    private var primaryButtonEnabled: Boolean = true
//    private var secondaryButtonEnabled: Boolean = true
//
//    private var primaryButton: Button? = null
//    private var secondaryButton: Button? = null
//    private var statusBadge: StatusBadge? = null
//
//    enum class FooterType(val value: Int) {
//        CALL_TO_ACTION(0),
//        CALL_TO_ACTION_DETAIL(1),
//        DUAL_BUTTON(2),
//        NO_ACTION(3);
//
//        companion object {
//            fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: CALL_TO_ACTION
//        }
//    }
//
//    init {
//        orientation = VERTICAL
//        parseAttributes(attrs)
//        setupView()
//    }
//
//    private fun parseAttributes(attrs: AttributeSet?) {
//        attrs ?: return
//
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFooter, 0, 0)
//        try {
//            val typeOrdinal = typedArray.getInt(R.styleable.CustomFooter_footerType, FooterType.CALL_TO_ACTION.value)
//            footerType = FooterType.fromInt(typeOrdinal)
//
//            primaryButtonText = typedArray.getString(R.styleable.CustomFooter_primaryButtonText) ?: primaryButtonText
//            secondaryButtonText = typedArray.getString(R.styleable.CustomFooter_secondaryButtonText) ?: secondaryButtonText
//            footerTitle = typedArray.getString(R.styleable.CustomFooter_footerTitle) ?: ""
//            footerDescription = typedArray.getString(R.styleable.CustomFooter_footerDescription) ?: ""
//            statusText = typedArray.getString(R.styleable.CustomFooter_statusBadgeText) ?: ""
//
//            val statusTypeOrdinal = typedArray.getInt(R.styleable.CustomFooter_statusBadgeType, StatusBadge.ChipType.APPROVED.ordinal)
//            statusType = StatusBadge.ChipType.values().getOrElse(statusTypeOrdinal) { StatusBadge.ChipType.APPROVED }
//
//            primaryButtonEnabled = typedArray.getBoolean(R.styleable.CustomFooter_primaryButtonEnabled, true)
//            secondaryButtonEnabled = typedArray.getBoolean(R.styleable.CustomFooter_secondaryButtonEnabled, true)
//
//        } finally {
//            typedArray.recycle()
//        }
//    }
//
//    private fun setupView() {
//        removeAllViews()
//
//        val layoutRes = when (footerType) {
//            FooterType.CALL_TO_ACTION -> R.layout.footer_call_to_action
//            FooterType.CALL_TO_ACTION_DETAIL -> R.layout.footer_call_to_action_description
//            FooterType.DUAL_BUTTON -> R.layout.footer_dual_button
//            FooterType.NO_ACTION -> R.layout.footer_no_action
//        }
//
//        LayoutInflater.from(context).inflate(layoutRes, this, true)
//
//        bindDataToViews()
//        setupClickListeners()
//    }
//
//    private fun bindDataToViews() {
//        when (footerType) {
//            FooterType.CALL_TO_ACTION -> {
//                primaryButton = findViewById<Button>(R.id.btnFooterCTAPrimary)?.apply {
//                    setLabel(primaryButtonText)
//                    isEnabled = primaryButtonEnabled
//                }
//            }
//
//            FooterType.CALL_TO_ACTION_DETAIL -> {
//                findViewById<TextView>(R.id.tvFooterCTADescTitle)?.text = footerTitle
//                findViewById<TextView>(R.id.tvFooterCTADescDescription)?.text = footerDescription
//                primaryButton = findViewById<Button>(R.id.btnFooterCTADescPrimary)?.apply {
//                    setLabel(primaryButtonText)
//                    isEnabled = primaryButtonEnabled
//                }
//            }
//
//            FooterType.DUAL_BUTTON -> {
//                secondaryButton = findViewById<Button>(R.id.btnFooterDualButtonSecondary)?.apply {
//                    setLabel(secondaryButtonText)
//                    isEnabled = secondaryButtonEnabled
//                }
//                primaryButton = findViewById<Button>(R.id.btnFooterDualButtonPrimary)?.apply {
//                    setLabel(primaryButtonText)
//                    isEnabled = primaryButtonEnabled
//                }
//            }
//
//            FooterType.NO_ACTION -> {
//                findViewById<TextView>(R.id.tvFooterNoActionTitle)?.text = footerTitle
//                findViewById<TextView>(R.id.tvFooterNoActionDescription)?.text = footerDescription
//                statusBadge = findViewById<StatusBadge>(R.id.bFooterNoActionStatus)?.apply {
//                    text = statusText
//                    chipType = statusType
//                }
//            }
//        }
//    }
//
//    private fun setupClickListeners() {
//        primaryButton?.setOnClickListener {
//            handlePrimaryButtonClick()
//        }
//
//        secondaryButton?.setOnClickListener {
//            handleSecondaryButtonClick()
//        }
//    }
//
//    private fun handlePrimaryButtonClick() {
//        delegate?.onPrimaryButtonClicked(footerType)
//
//        when (footerType) {
//            FooterType.CALL_TO_ACTION,
//            FooterType.CALL_TO_ACTION_DETAIL -> {
//                delegate?.onRegisterClicked()
//            }
//            FooterType.DUAL_BUTTON -> {
//                delegate?.onContinueClicked()
//            }
//            FooterType.NO_ACTION -> {}
//        }
//    }
//
//    private fun handleSecondaryButtonClick() {
//        delegate?.onSecondaryButtonClicked(footerType)
//
//        when (footerType) {
//            FooterType.DUAL_BUTTON -> {
//                delegate?.onCancelClicked()
//            }
//            else -> {}
//        }
//    }
//
//    fun setFooterType(type: FooterType) {
//        if (footerType != type) {
//            footerType = type
//            setupView()
//        }
//    }
//
//    fun setPrimaryButtonText(text: String) {
//        primaryButtonText = text
//        primaryButton?.setLabel(text)
//    }
//
//    fun setSecondaryButtonText(text: String) {
//        secondaryButtonText = text
//        secondaryButton?.setLabel(text)
//    }
//
//    fun setTitleAndDescription(title: String, description: String) {
//        footerTitle = title
//        footerDescription = description
//        findViewById<TextView>(R.id.tvFooterCTADescTitle)?.text = title
//        findViewById<TextView>(R.id.tvFooterCTADescDescription)?.text = description
//        findViewById<TextView>(R.id.tvFooterNoActionTitle)?.text = title
//        findViewById<TextView>(R.id.tvFooterNoActionDescription)?.text = description
//    }
//
//    fun setStatusBadge(text: String, type: StatusBadge.ChipType) {
//        statusText = text
//        statusType = type
//        statusBadge?.apply {
//            this.text = text
//            chipType = type
//        }
//    }
//
//    fun setPrimaryButtonEnabled(enabled: Boolean) {
//        primaryButtonEnabled = enabled
//        primaryButton?.isEnabled = enabled
//    }
//
//    fun setSecondaryButtonEnabled(enabled: Boolean) {
//        secondaryButtonEnabled = enabled
//        secondaryButton?.isEnabled = enabled
//    }
//}