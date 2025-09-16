package com.edts.components.leave.card

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.LeaveQuotaCardBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

class LeaveQuotaCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: LeaveQuotaCardBinding =
        LeaveQuotaCardBinding.inflate(LayoutInflater.from(context), this, true)

    var title: String? = null
        set(value) {
            field = value
            binding.tvLeaveQuotaTitle.text = value
        }

    var leaveQuota: Int = 0
        set(value) {
            field = value
            binding.tvLeaveQuota.text = context.getString(R.string.leave_balance_format, value)
        }

    var expiredDate: String? = null
        set(value) {
            field = value
            binding.tvExpiredDate.text = context.getString(R.string.leave_expiry_format, value)
        }

    var leaveUsed: Int = 0
        set(value) {
            field = value
            binding.tvLeaveUsed.text = context.getString(R.string.leave_used_format, value)
        }

    init {
        setupCardAppearance()
        parseAttributes(attrs)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.LeaveQuotaCard, 0, 0) {
                title = getString(R.styleable.LeaveQuotaCard_leaveQuotaTitle)
                leaveQuota = getInt(R.styleable.LeaveQuotaCard_leaveQuota, 0)
                expiredDate = getString(R.styleable.LeaveQuotaCard_expiredDate)
                leaveUsed = getInt(R.styleable.LeaveQuotaCard_leaveUsed, 0)
            }
        }
    }

    private fun setupCardAppearance() {
        strokeColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)
        radius = resources.getDimension(R.dimen.radius_8dp)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        cardElevation = resources.getDimension(R.dimen.dimen_1dp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = MaterialColors.getColor(
                context,
                R.attr.colorForegroundPrimary,
                Color.BLACK
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }
}