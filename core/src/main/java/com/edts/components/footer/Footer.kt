package com.edts.components.footer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.button.Button
import com.edts.components.databinding.FooterCallToActionBinding
import com.edts.components.databinding.FooterCallToActionDescriptionBinding
import com.edts.components.databinding.FooterDualButtonBinding
import com.edts.components.databinding.FooterNoActionBinding
import com.edts.components.status.badge.StatusBadge
import com.edts.components.utils.resolveColorAttribute

class Footer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var footerType: FooterType = FooterType.CALL_TO_ACTION
    private var primaryButtonText: String = "Daftar Sekarang"
    private var secondaryButtonText: String = "Button Label"
    private var footerTitle: String = ""
    private var footerDescription: String = ""
    private var statusText: String = ""
    private var statusType: StatusBadge.ChipType = StatusBadge.ChipType.APPROVED
    private var footerHasStroke: Boolean = false
    private var topStrokeView: View? = null
    private var showDescription: Boolean = false
    private var dualButtonTitle: String = ""
    private var dualButtonSupportText1: String = ""
    private var dualButtonSupportText2: String = ""
    private var primaryButtonEnabled: Boolean = true
    private var secondaryButtonEnabled: Boolean = true
    private var primaryButton: Button? = null
    private var secondaryButton: Button? = null
    private var statusBadge: StatusBadge? = null
    private var ctaBinding: FooterCallToActionBinding? = null
    private var ctaDescBinding: FooterCallToActionDescriptionBinding? = null
    private var dualButtonBinding: FooterDualButtonBinding? = null
    private var noActionBinding: FooterNoActionBinding? = null

    var delegate: FooterDelegate? = null

    init {
        orientation = VERTICAL
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorFFF))
        parseAttributes(attrs)
        setupView()
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFooter, 0, 0)
        try {
            val typeOrdinal = typedArray.getInt(R.styleable.CustomFooter_footerType, FooterType.CALL_TO_ACTION.value)
            footerType = FooterType.fromInt(typeOrdinal)
            footerHasStroke = typedArray.getBoolean(R.styleable.CustomFooter_footerHasStroke, false)
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
        clearBindings()

        if (footerHasStroke) {
            topStrokeView = View(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    context.resources.getDimensionPixelSize(R.dimen.dimen_1dp)
                )
                setBackgroundColor(context.resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30))
            }
            addView(topStrokeView)
        }

        when (footerType) {
            FooterType.CALL_TO_ACTION -> {
                ctaBinding = FooterCallToActionBinding.inflate(
                    LayoutInflater.from(context), this, true
                )
            }
            FooterType.CALL_TO_ACTION_DETAIL -> {
                ctaDescBinding = FooterCallToActionDescriptionBinding.inflate(
                    LayoutInflater.from(context), this, true
                )
            }
            FooterType.DUAL_BUTTON -> {
                dualButtonBinding = FooterDualButtonBinding.inflate(
                    LayoutInflater.from(context), this, true
                )
            }
            FooterType.NO_ACTION -> {
                noActionBinding = FooterNoActionBinding.inflate(
                    LayoutInflater.from(context), this, true
                )
            }
        }

        bindDataToViews()
        setupClickListeners()
    }

    private fun bindDataToViews() {
        when (footerType) {
            FooterType.CALL_TO_ACTION -> {
                ctaBinding?.let { binding ->
                    binding.tvFooterCTATitle.text = dualButtonTitle
                    binding.tvFooterCTASupportText1.text = dualButtonSupportText1
                    binding.tvFooterCTASupportText2.text = dualButtonSupportText2
                    setDescriptionVisibility(showDescription)
                    primaryButton = binding.btnFooterCTAPrimary.apply {
                        setLabel(primaryButtonText)
                        isEnabled = primaryButtonEnabled
                    }
                }
            }
            FooterType.CALL_TO_ACTION_DETAIL -> {
                ctaDescBinding?.let { binding ->
                    binding.tvFooterCTADescTitle.text = footerTitle
                    binding.tvFooterCTADescDescription.text = footerDescription
                    primaryButton = binding.btnFooterCTADescPrimary.apply {
                        setLabel(primaryButtonText)
                        isEnabled = primaryButtonEnabled
                    }
                }
            }
            FooterType.DUAL_BUTTON -> {
                dualButtonBinding?.let { binding ->
                    binding.tvFooterDualButtonTitle.text = dualButtonTitle
                    binding.tvFooterDualButtonSupportText1.text = dualButtonSupportText1
                    binding.tvFooterDualButtonSupportText2.text = dualButtonSupportText2
                    setDescriptionVisibility(showDescription)
                    secondaryButton = binding.btnFooterDualButtonSecondary.apply {
                        setLabel(secondaryButtonText)
                        isEnabled = secondaryButtonEnabled
                    }
                    primaryButton = binding.btnFooterDualButtonPrimary.apply {
                        setLabel(primaryButtonText)
                        isEnabled = primaryButtonEnabled
                    }
                }
            }
            FooterType.NO_ACTION -> {
                noActionBinding?.let { binding ->
                    binding.tvFooterNoActionTitle.text = footerTitle
                    binding.tvFooterNoActionDescription.text = footerDescription
                    statusBadge = binding.bFooterNoActionStatus.apply {
                        text = statusText
                        chipType = statusType
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        when (footerType) {
            FooterType.CALL_TO_ACTION -> {
                ctaBinding?.btnFooterCTAPrimary?.setOnClickListener {
                    handlePrimaryButtonClick()
                }
            }
            FooterType.CALL_TO_ACTION_DETAIL -> {
                ctaDescBinding?.btnFooterCTADescPrimary?.setOnClickListener {
                    handlePrimaryButtonClick()
                }
            }
            FooterType.DUAL_BUTTON -> {
                dualButtonBinding?.let { binding ->
                    binding.btnFooterDualButtonPrimary.setOnClickListener {
                        handlePrimaryButtonClick()
                    }
                    binding.btnFooterDualButtonSecondary.setOnClickListener {
                        handleSecondaryButtonClick()
                    }
                }
            }
            FooterType.NO_ACTION -> {
            }
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

    private fun clearBindings() {
        ctaBinding = null
        ctaDescBinding = null
        dualButtonBinding = null
        noActionBinding = null
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
        ctaDescBinding?.let { binding ->
            binding.tvFooterCTADescTitle.text = title
            binding.tvFooterCTADescDescription.text = description
        }
        noActionBinding?.let { binding ->
            binding.tvFooterNoActionTitle.text = title
            binding.tvFooterNoActionDescription.text = description
        }
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

    fun setDescriptionVisibility(showDescription: Boolean) {
        this.showDescription = showDescription
        val visibility = if (showDescription) View.VISIBLE else View.GONE
        dualButtonBinding?.let { binding ->
            binding.tvFooterDualButtonTitle.visibility = visibility
            binding.tvFooterDualButtonSupportText1.visibility = visibility
            binding.tvFooterDualButtonTextDivider.visibility = visibility
            binding.tvFooterDualButtonSupportText2.visibility = visibility
        }
        ctaBinding?.let { binding ->
            binding.tvFooterCTATitle.visibility = visibility
            binding.tvFooterCTASupportText1.visibility = visibility
            binding.tvFooterCTATextDivider.visibility = visibility
            binding.tvFooterCTASupportText2.visibility = visibility
        }
    }

    fun setDualButtonDescription(title: String, supportText1: String, supportText2: String) {
        dualButtonTitle = title
        dualButtonSupportText1 = supportText1
        dualButtonSupportText2 = supportText2
        dualButtonBinding?.let { binding ->
            binding.tvFooterDualButtonTitle.text = title
            binding.tvFooterDualButtonSupportText1.text = supportText1
            binding.tvFooterDualButtonSupportText2.text = supportText2
        }
        ctaBinding?.let { binding ->
            binding.tvFooterCTATitle.text = title
            binding.tvFooterCTASupportText1.text = supportText1
            binding.tvFooterCTASupportText2.text = supportText2
        }
    }

    fun setStroke(hasStroke: Boolean) {
        if (this.footerHasStroke != hasStroke) {
            this.footerHasStroke = hasStroke
            setupView()
        }
    }

    fun isDescriptionVisible(): Boolean = showDescription

    fun getDualButtonTitle(): String = dualButtonTitle

    fun getDualButtonSupportText1(): String = dualButtonSupportText1

    fun getDualButtonSupportText2(): String = dualButtonSupportText2

    enum class FooterType(val value: Int) {
        CALL_TO_ACTION(0),
        CALL_TO_ACTION_DETAIL(1),
        DUAL_BUTTON(2),
        NO_ACTION(3);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: CALL_TO_ACTION
        }
    }
}