package com.edts.components.leave.card

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import com.edts.components.R
import com.edts.components.databinding.LeaveQuotaCardBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors

/**
 * A custom view that displays leave quota information in a card format.
 *
 * This component extends MaterialCardView to encapsulate the layout and logic for displaying
 * key leave-related data. The data can be set via XML attributes or programmatically
 * by setting the component's properties.
 *
 * ### XML Usage Example:
 * ```xml
 * <com.example.components.CustomLeaveQuotaCard
 * android:layout_width="152dp"
 * android:layout_height="wrap_content"
 * app:leaveQuotaTitle="Cuti Tahunan"
 * app:leaveQuota="5"
 * app:expiredDate="31 Des 2025"
 * app:leaveUsed="2" />
 * ```
 */
class LeaveQuotaCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: LeaveQuotaCardBinding = LeaveQuotaCardBinding.inflate(LayoutInflater.from(context), this, true)

    // Public properties with custom setters provide a cleaner, more idiomatic API.
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

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.LeaveQuotaCard,
                0,
                0
            )
            try {
                // Read from XML attributes and set the properties.
                // This will trigger the custom setters above.
                title = typedArray.getString(R.styleable.LeaveQuotaCard_leaveQuotaTitle)
                leaveQuota = typedArray.getInt(R.styleable.LeaveQuotaCard_leaveQuota, 0)
                expiredDate = typedArray.getString(R.styleable.LeaveQuotaCard_expiredDate)
                leaveUsed = typedArray.getInt(R.styleable.LeaveQuotaCard_leaveUsed, 0)
            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Configures the visual properties of the card, such as elevation, stroke, and corners.
     */
    private fun setupCardAppearance() {
        val strokeSubtleColor = MaterialColors.getColor(this, R.attr.colorStrokeSubtle)
        val cornerRadius = resources.getDimension(R.dimen.radius_8dp)
        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        val elevation = resources.getDimension(R.dimen.dimen_1dp)

        this.radius = cornerRadius
        this.cardElevation = elevation
        this.strokeColor = strokeSubtleColor
        this.strokeWidth = strokeWidth

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
