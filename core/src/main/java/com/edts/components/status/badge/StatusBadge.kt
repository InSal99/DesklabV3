package com.edts.components.status.badge

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.utils.pxToDp
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.textview.MaterialTextView

class StatusBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr) {
    enum class ChipType(
        val backgroundColorAttr: Int,
        val backgroundColorRes: Int,
        val textColorAttr: Int,
        val textColorRes: Int,
        val iconRes: Int
    ) {
        APPROVED(R.attr.colorBackgroundSuccessSubtle, R.color.kitColorGreen10, R.attr.colorForegroundSuccessIntense, R.color.kitColorGreen50, R.drawable.kit_ic_success),
        DECLINE(R.attr.colorBackgroundTertiary, R.color.kitColorNeutralGrayLight20, R.attr.colorForegroundSecondary, R.color.kitColorNeutralGrayLight60, R.drawable.kit_ic_error),
        WAITING(R.attr.colorBackgroundInfoSubtle, R.color.kitColorBlue10, R.attr.colorForegroundInfoIntense, R.color.kitColorBlue50, R.drawable.kit_ic_alarm),
        CANCEL(R.attr.colorBackgroundAttentionSubtle, R.color.kitColorRed10, R.attr.colorForegroundAttentionIntense, R.color.kitColorRed40, R.drawable.kit_ic_error);
    }

    var chipType: ChipType = ChipType.APPROVED
        set(value) {
            field = value
            applyChipStyle()
        }

    init {
        setupChip()
        initAttrs(attrs)
    }

    private fun setupChip() {
        gravity = Gravity.CENTER_VERTICAL
        compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.margin_4dp)
        setTextAppearance(context, R.style.TextMedium_Label3)

        val horizontalPadding = resources.getDimensionPixelSize(R.dimen.margin_12dp)
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.margin_8dp)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusBadge, 0, 0)
        try {
            val typeOrdinal = typedArray.getInt(R.styleable.StatusBadge_statusBadgeType, ChipType.APPROVED.ordinal)
            chipType = ChipType.values().getOrElse(typeOrdinal) { ChipType.APPROVED }

            val chipText = typedArray.getString(R.styleable.StatusBadge_statusBadgeText)

            if (!chipText.isNullOrEmpty()) {
                text = chipText
            }
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyChipStyle() {
        val backgroundColor = context.resolveColorAttr(chipType.backgroundColorAttr, chipType.backgroundColorRes)
        val textColor = context.resolveColorAttr(chipType.textColorAttr, chipType.textColorRes)
        val strokeColor = context.resolveColorAttr(R.attr.colorStrokeInteractive, R.color.kitColorNeutralGrayLightA20)
//        val backgroundColor = ContextCompat.getColor(context, chipType.backgroundColorRes)
//        val textColor = ContextCompat.getColor(context, chipType.textColorRes)
//        val strokeColor = getColorFromAttr(R.attr.colorStrokeInteractive)

        val background = ContextCompat.getDrawable(context, R.drawable.kit_bg_status_badge)?.mutate() as? GradientDrawable
        background?.setColor(backgroundColor)
        background?.setStroke(resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp), strokeColor)

        this.background = background
        setTextColor(textColor)

//        val icon = ContextCompat.getDrawable(context, chipType.iconRes)

        val iconSize = 16.pxToDp
        val icon = ContextCompat.getDrawable(context, chipType.iconRes)?.mutate()
        icon?.setBounds(0, 0, iconSize, iconSize)
        setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        compoundDrawableTintList = ColorStateList.valueOf(textColor)
    }

//    private fun getColorFromAttr(attr: Int): Int {
//        val typedValue = TypedValue()
//        return if (context.theme.resolveAttribute(attr, typedValue, true)) {
//            typedValue.data
//        } else {
//            R.attr.colorForegroundTertiary
//        }
//    }
}