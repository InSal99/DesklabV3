package com.edts.components.leave.card

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.withStyledAttributes
import com.edts.components.R
import com.edts.components.databinding.LeaveQuotaCardBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.card.MaterialCardView

class LeaveQuotaCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: LeaveQuotaCardBinding =
        LeaveQuotaCardBinding.inflate(LayoutInflater.from(context), this, true)

    var title: String? = null
        set(value) {
            field = value
            binding.tvLeaveQuotaTitle.text = value
        }

    var leaveQuota: String? = null
        set(value) {
            field = value
            binding.tvLeaveQuota.text = context.getString(R.string.leave_balance_format, value)
        }

    var expiredDate: String? = null
        set(value) {
            field = value
            binding.tvExpiredDate.text = context.getString(R.string.leave_expiry_format, value)
        }

    var leaveUsed: String? = null
        set(value) {
            field = value
            binding.tvLeaveUsed.text = context.getString(R.string.leave_used_format, value)
        }

    init {
        setupCardAppearance()
        applyStyledAttributes(attrs)
    }

    private fun applyStyledAttributes(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.LeaveQuotaCard, 0, 0) {
            title = getString(R.styleable.LeaveQuotaCard_leaveQuotaTitle)
            leaveQuota = getString(R.styleable.LeaveQuotaCard_leaveQuota)
            expiredDate = getString(R.styleable.LeaveQuotaCard_expiredDate)
            leaveUsed = getString(R.styleable.LeaveQuotaCard_leaveUsed)
        }
    }

    private fun setupCardAppearance() {
        val strokeSubtleColor = context.resolveColorAttr(
            R.attr.colorStrokeSubtle,
            R.color.colorNeutral30
        )
        val cornerRadius = 8f.dpToPx
        val strokeWidth = 1.dpToPx
        val elevation = 1f.dpToPx

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val shadowColor = context.resolveColorAttr(
                R.attr.colorForegroundPrimary,
                R.color.color000
            )
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }
}