package com.edts.components.leave.card

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.LeaveQuotaDescriptionBinding
import java.util.regex.Pattern

class LeaveQuotaDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: LeaveQuotaDescriptionBinding
    private val defaultTextColor: Int
    private val attentionTextColor: Int

    var title: String? = null
        set(value) {
            field = value
            binding.tvLeaveQuotaTitle.text = value
        }

    var quota: String? = null
        set(value) {
            field = value
            val displayText = formatQuotaText(value)
            binding.tvLeaveQuota.text = displayText
            updateQuotaTextColor(displayText)
        }

    init {
        binding = LeaveQuotaDescriptionBinding.inflate(LayoutInflater.from(context), this)

        defaultTextColor = resolveColorAttr(R.attr.colorForegroundPrimary)
        attentionTextColor = resolveColorAttr(R.attr.colorForegroundAttentionIntense)

        binding.tvLeaveQuota.setTextColor(defaultTextColor)

        attrs?.let { applyAttributes(it) }
    }

    private fun applyAttributes(attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.LeaveQuotaDescription, 0, 0) {
            title = getString(R.styleable.LeaveQuotaDescription_leaveQuotaTitleDesc)
            quota = getString(R.styleable.LeaveQuotaDescription_leaveQuotaDesc)
        }
    }

    private fun formatQuotaText(quotaText: String?): String? {
        return if (quotaText?.toIntOrNull() != null) {
            "$quotaText Hari"
        } else {
            quotaText
        }
    }

    private fun updateQuotaTextColor(displayText: String?) {
        displayText?.takeIf { it.isNotBlank() }?.let { text ->
            val matcher = Pattern.compile("-?\\d+").matcher(text)
            val quotaValue = if (matcher.find()) matcher.group(0).toIntOrNull() else null

            val color = if (quotaValue != null && quotaValue <= 0) {
                attentionTextColor
            } else {
                defaultTextColor
            }
            binding.tvLeaveQuota.setTextColor(color)
        } ?: run {
            binding.tvLeaveQuota.setTextColor(defaultTextColor)
        }
    }

    private fun resolveColorAttr(@AttrRes attrRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            typedValue.data
        } else {
            Color.BLACK
        }
    }
}