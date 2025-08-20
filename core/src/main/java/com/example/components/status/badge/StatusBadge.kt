package com.example.components.status.badge

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.components.R

class StatusBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {
    enum class ChipType(
        val backgroundColorAttr: Int,
        val textColorAttr: Int,
        val iconRes: Int
    ) {
        APPROVED(R.attr.colorBackgroundSuccessSubtle, R.attr.colorForegroundSuccessIntense, R.drawable.placeholder),
        DECLINE(R.attr.colorBackgroundAttentionSubtle, R.attr.colorForegroundAttentionIntense, R.drawable.placeholder),
        WAITING(R.attr.colorBackgroundInfoSubtle, R.attr.colorForegroundInfoIntense, R.drawable.placeholder),
        CANCEL(R.attr.colorBackgroundTertiary, R.attr.colorForegroundSecondary, R.drawable.placeholder);
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
        val backgroundColor = getColorFromAttr(chipType.backgroundColorAttr)
        val textColor = getColorFromAttr(chipType.textColorAttr)
        val strokeColor = getColorFromAttr(R.attr.colorStrokeInteractive)

        val background = ContextCompat.getDrawable(context, R.drawable.bg_status_badge)?.mutate() as? GradientDrawable
        background?.setColor(backgroundColor)
        background?.setStroke(resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp), strokeColor)

        this.background = background
        setTextColor(textColor)

        val icon = ContextCompat.getDrawable(context, chipType.iconRes)
        setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        compoundDrawableTintList = ColorStateList.valueOf(textColor)
    }

    private fun getColorFromAttr(attr: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attr, typedValue, true)) {
            typedValue.data
        } else {
            R.attr.colorForegroundTertiary
        }
    }
}