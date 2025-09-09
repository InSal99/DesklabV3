package com.edts.components.infoboxfooter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.edts.components.R
import com.edts.components.footer.Footer
import com.edts.components.footer.FooterDelegate
import com.edts.components.infobox.InfoBox
import com.edts.components.status.badge.StatusBadge

class InfoBoxFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val infoBox: InfoBox
    private val footer: Footer

    var infoText: CharSequence? = null
        set(value) {
            field = value
            infoBox.text = value
        }

    var infoVariant: InfoBox.InfoBoxVariant = InfoBox.InfoBoxVariant.INFORMATION
        set(value) {
            field = value
            infoBox.variant = value
        }

    var footerDelegate: FooterDelegate? = null
        set(value) {
            field = value
            footer.delegate = value
        }

    var isInfoBoxVisible: Boolean = true
        set(value) {
            field = value
            infoBox.visibility = if (value) View.VISIBLE else View.GONE
        }

    private var _footerType: Footer.FooterType = Footer.FooterType.CALL_TO_ACTION

    init {
        orientation = VERTICAL

        infoBox = InfoBox(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 0)
            }
        }

        footer = Footer(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 0)
            }
            setShadowVisibility(false)
        }

        addView(infoBox)
        addView(footer)

        parseAttributes(attrs)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfoBoxFooter, 0, 0)
        try {
            infoText = typedArray.getString(R.styleable.InfoBoxFooter_text)
            val infoVariantValue = typedArray.getInt(R.styleable.InfoBoxFooter_variant, 0)
            infoVariant = InfoBox.InfoBoxVariant.fromValue(infoVariantValue)
            isInfoBoxVisible = typedArray.getBoolean(R.styleable.InfoBoxFooter_showInfoBox, true)

            val footerTypeValue = typedArray.getInt(R.styleable.InfoBoxFooter_footerType, 0)
            _footerType = Footer.FooterType.fromInt(footerTypeValue)
            footer.setFooterType(_footerType)

            typedArray.getString(R.styleable.InfoBoxFooter_primaryButtonText)?.let {
                footer.setPrimaryButtonText(it)
            }

            typedArray.getString(R.styleable.InfoBoxFooter_secondaryButtonText)?.let {
                footer.setSecondaryButtonText(it)
            }

            typedArray.getString(R.styleable.InfoBoxFooter_footerTitle)?.let { title ->
                val description = typedArray.getString(R.styleable.InfoBoxFooter_footerDescription) ?: ""
                footer.setTitleAndDescription(title, description)
            }

            footer.setPrimaryButtonEnabled(typedArray.getBoolean(R.styleable.InfoBoxFooter_primaryButtonEnabled, true))
            footer.setSecondaryButtonEnabled(typedArray.getBoolean(R.styleable.InfoBoxFooter_secondaryButtonEnabled, true))

        } finally {
            typedArray.recycle()
        }
    }

    fun setInfoBoxText(text: CharSequence?) {
        infoText = text
    }

    fun setInfoBoxVariant(variant: InfoBox.InfoBoxVariant) {
        infoVariant = variant
    }

    fun showInfoBox(show: Boolean) {
        isInfoBoxVisible = show
    }

    fun setFooterType(type: Footer.FooterType) {
        _footerType = type
        footer.setFooterType(type)
    }

    fun getFooterType(): Footer.FooterType = _footerType

    fun setPrimaryButtonText(text: String) {
        footer.setPrimaryButtonText(text)
    }

    fun setSecondaryButtonText(text: String) {
        footer.setSecondaryButtonText(text)
    }

    fun setTitleAndDescription(title: String, description: String) {
        footer.setTitleAndDescription(title, description)
    }

    fun setStatusBadge(text: String, type: StatusBadge.ChipType) {
        footer.setStatusBadge(text, type)
    }

    fun setPrimaryButtonEnabled(enabled: Boolean) {
        footer.setPrimaryButtonEnabled(enabled)
    }

    fun setSecondaryButtonEnabled(enabled: Boolean) {
        footer.setSecondaryButtonEnabled(enabled)
    }

    fun setDescriptionVisibility(showDescription: Boolean) {
        footer.setDescriptionVisibility(showDescription)
    }

    fun setDualButtonDescription(title: String, supportText1: String, supportText2: String) {
        footer.setDualButtonDescription(title, supportText1, supportText2)
    }

    fun getInfoBox(): InfoBox = infoBox
    fun getFooter(): Footer = footer
}