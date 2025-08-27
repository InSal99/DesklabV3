package com.edts.components.footer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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

    private var primaryButtonEnabled: Boolean = true
    private var secondaryButtonEnabled: Boolean = true

    private var primaryButton: Button? = null
    private var secondaryButton: Button? = null
    private var statusBadge: StatusBadge? = null

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
}


//class CustomFooter @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : LinearLayout(context, attrs, defStyleAttr) {
//
//    // Delegate property
//    var delegate: CustomFooterDelegate? = null
//
//    // Footer configuration properties
//    private var footerType: FooterType = FooterType.CALL_TO_ACTION
//    private var primaryButtonText: String = "Daftar Sekarang"
//    private var secondaryButtonText: String = "Button Label"
//    private var footerTitle: String = ""
//    private var footerDescription: String = ""
//    private var statusText: String = ""
//    private var statusType: StatusBadge.ChipType = StatusBadge.ChipType.APPROVED
//
//    // Button state properties
//    private var primaryButtonEnabled: Boolean = true
//    private var secondaryButtonEnabled: Boolean = true
//    private var primaryButtonLoading: Boolean = false
//
//    // Button configuration properties
//    private var primaryButtonSize: String = "MD"
//    private var secondaryButtonSize: String = "MD"
//    private var primaryButtonType: String = "PRIMARY"
//    private var secondaryButtonType: String = "SECONDARY"
//
//    // Styling properties
//    private var footerPaddingHorizontal: Int = 0
//    private var footerPaddingVertical: Int = 0
//
//    // View references for click handling
//    private var primaryButton: CustomButton? = null
//    private var secondaryButton: CustomButton? = null
//    private var statusBadge: StatusBadge? = null
//
//    enum class FooterType(val value: Int) {
//        CALL_TO_ACTION(0),
//        CALL_TO_ACTION_DETAIL(1),
//        DUAL_BUTTON(2),
//        NO_ACTION(3);
//
//        companion object {
//            fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: CALL_TO_ACTION
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
//            // Parse footer type
//            val typeOrdinal = typedArray.getInt(R.styleable.CustomFooter_footerType, FooterType.CALL_TO_ACTION.value)
//            footerType = FooterType.fromInt(typeOrdinal)
//
//            // Parse text attributes
//            primaryButtonText = typedArray.getString(R.styleable.CustomFooter_primaryButtonText) ?: primaryButtonText
//            secondaryButtonText = typedArray.getString(R.styleable.CustomFooter_secondaryButtonText) ?: secondaryButtonText
//            footerTitle = typedArray.getString(R.styleable.CustomFooter_footerTitle) ?: ""
//            footerDescription = typedArray.getString(R.styleable.CustomFooter_footerDescription) ?: ""
//            statusText = typedArray.getString(R.styleable.CustomFooter_statusBadgeText) ?: ""
//
//            // Parse status badge type
//            val statusTypeOrdinal = typedArray.getInt(R.styleable.CustomFooter_statusBadgeType, StatusBadge.ChipType.APPROVED.ordinal)
//            statusType = StatusBadge.ChipType.entries.toTypedArray().getOrElse(statusTypeOrdinal) { StatusBadge.ChipType.APPROVED }
//
//            // Parse button states
//            primaryButtonEnabled = typedArray.getBoolean(R.styleable.CustomFooter_primaryButtonEnabled, true)
//            secondaryButtonEnabled = typedArray.getBoolean(R.styleable.CustomFooter_secondaryButtonEnabled, true)
////            primaryButtonLoading = typedArray.getBoolean(R.styleable.CustomFooter_primaryButtonLoading, false)
//
//            // Parse button sizes
////            primaryButtonSize = getButtonSize(typedArray.getInt(R.styleable.CustomFooter_primaryButtonSize, 1))
////            secondaryButtonSize = getButtonSize(typedArray.getInt(R.styleable.CustomFooter_secondaryButtonSize, 1))
//
//            // Parse button types
////            primaryButtonType = getButtonType(typedArray.getInt(R.styleable.CustomFooter_primaryButtonType, 0))
////            secondaryButtonType = getButtonType(typedArray.getInt(R.styleable.CustomFooter_secondaryButtonType, 1))
//
//            // Parse styling attributes
////            val backgroundRes = typedArray.getResourceId(R.styleable.CustomFooter_footerBackground, -1)
////            if (backgroundRes != -1) {
////                setBackgroundResource(backgroundRes)
////            } else {
////                val backgroundColor = typedArray.getColor(R.styleable.CustomFooter_footerBackground, Color.TRANSPARENT)
////                if (backgroundColor != Color.TRANSPARENT) {
////                    setBackgroundColor(backgroundColor)
////                }
////            }
//
//            // Parse padding attributes
//            footerPaddingHorizontal = resources.getDimensionPixelSize(R.dimen.margin_12dp)
//            footerPaddingVertical = resources.getDimensionPixelSize(R.dimen.margin_8dp)
//
//        } finally {
//            typedArray.recycle()
//        }
//    }
//
//    private fun getButtonSize(value: Int): String {
//        return when (value) {
//            0 -> "SM"
//            1 -> "MD"
//            2 -> "LG"
//            else -> "MD"
//        }
//    }
//
//    private fun getButtonType(value: Int): String {
//        return when (value) {
//            0 -> "PRIMARY"
//            1 -> "SECONDARY"
//            2 -> "TERTIARY"
//            3 -> "GHOST"
//            else -> "PRIMARY"
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
//        val inflatedView = LayoutInflater.from(context).inflate(layoutRes, this, true)
//
//        // Apply padding
//        setPadding(footerPaddingHorizontal, footerPaddingVertical, footerPaddingHorizontal, footerPaddingVertical)
//
//        bindDataToViews()
//        setupClickListeners()
//    }
//
//    private fun bindDataToViews() {
//        when (footerType) {
//            FooterType.CALL_TO_ACTION -> {
//                primaryButton = findViewById<CustomButton>(R.id.btnFooterCTAPrimary)?.apply {
//                    setLabel(primaryButtonText)
////                    setCustomButtonSize(primaryButtonSize)
////                    setCustomButtonType(primaryButtonType)
//                    isEnabled = primaryButtonEnabled
////                    setLoading(primaryButtonLoading)
//                }
//            }
//
//            FooterType.CALL_TO_ACTION_DETAIL -> {
//                findViewById<TextView>(R.id.tvFooterCTADescTitle)?.text = footerTitle
//                findViewById<TextView>(R.id.tvFooterCTADescDescription)?.text = footerDescription
//                primaryButton = findViewById<CustomButton>(R.id.btnFooterCTADescPrimary)?.apply {
//                    setLabel(primaryButtonText)
////                    setCustomButtonSize(primaryButtonSize)
////                    setCustomButtonType(primaryButtonType)
//                    isEnabled = primaryButtonEnabled
////                    setLoading(primaryButtonLoading)
//                }
//            }
//
//            FooterType.DUAL_BUTTON -> {
//                secondaryButton = findViewById<CustomButton>(R.id.btnFooterDualButtonSecondary)?.apply {
//                    setLabel(secondaryButtonText)
////                    setCustomButtonSize(secondaryButtonSize)
////                    setCustomButtonType(secondaryButtonType)
//                    isEnabled = secondaryButtonEnabled
//                }
//                primaryButton = findViewById<CustomButton>(R.id.btnFooterDualButtonPrimary)?.apply {
//                    setLabel(primaryButtonText)
////                    setCustomButtonSize(primaryButtonSize)
////                    setCustomButtonType(primaryButtonType)
//                    isEnabled = primaryButtonEnabled
////                    setLoading(primaryButtonLoading)
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
//    // Public methods for programmatic configuration
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
//
////    fun setPrimaryButtonLoading(isLoading: Boolean) {
////        primaryButtonLoading = isLoading
////        primaryButton?.setLoading(isLoading)
////    }
//
////    fun updateButtonConfiguration(
////        primarySize: String? = null,
////        secondarySize: String? = null,
////        primaryType: String? = null,
////        secondaryType: String? = null
////    ) {
////        primarySize?.let {
////            primaryButtonSize = it
////            primaryButton?.setCustomButtonSize(it)
////        }
////        secondarySize?.let {
////            secondaryButtonSize = it
////            secondaryButton?.setCustomButtonSize(it)
////        }
////        primaryType?.let {
////            primaryButtonType = it
////            primaryButton?.setCustomButtonType(it)
////        }
////        secondaryType?.let {
////            secondaryButtonType = it
////            secondaryButton?.setCustomButtonType(it)
////        }
////    }
//}