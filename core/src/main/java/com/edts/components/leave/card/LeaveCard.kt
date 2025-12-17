package com.edts.components.leave.card

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.LeaveCardBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttr
import com.google.android.material.card.MaterialCardView

class LeaveCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding = LeaveCardBinding.inflate(LayoutInflater.from(context), this, true)

    var leaveCardDelegate: LeaveCardDelegate? = null

    var employeeName: CharSequence?
        get() = binding.lEmployeeInfo.employeeName
        set(value) {
            binding.lEmployeeInfo.employeeName = value
        }

    var employeeRole: CharSequence?
        get() = binding.lEmployeeInfo.employeeRole
        set(value) {
            binding.lEmployeeInfo.employeeRole = value
        }

    var employeeImage: Int? = null
        set(value) {
            field = value
            updateImageSrc()
        }

    var employeeImageUrl: String? = null
        set(value) {
            field = value
            updateImageSrc()
        }

    var counterText: CharSequence?
        get() = binding.lDescription.counterText
        set(value) {
            binding.lDescription.counterText = value
        }

    var counterType: LeaveCounter.CounterType
        get() = binding.lDescription.counterType
        set(value) {
            binding.lDescription.counterType = value
        }

    init {
        setupCardAppearance(context)
        initAttrs(attrs)
    }

    override fun performClick(): Boolean {
        val handled = super.performClick()
        leaveCardDelegate?.onClick(this)
        return handled
    }

    private fun setupClickAnimation() {
        isClickable = true
        isFocusable = true

        val activeColor = run {
            val fallbackColorRes = R.color.kitColorNeutralGrayDarkA5
            val resolved = context.resolveColorAttr(R.attr.colorBackgroundModifierOnPress, fallbackColorRes)
            try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
        }
        rippleColor = ColorStateList.valueOf(activeColor)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeaveCard, 0, 0)
        try {
            employeeName = typedArray.getString(R.styleable.LeaveCard_employeeName)
            employeeRole = typedArray.getString(R.styleable.LeaveCard_employeeRole)
            employeeImage = typedArray.getResourceId(R.styleable.LeaveCard_employeeImage, R.drawable.kit_ic_placeholder)
            employeeImageUrl = typedArray.getString(R.styleable.LeaveCard_employeeImageUrl)

            counterText = typedArray.getString(R.styleable.LeaveCard_counterText)

            val typeOrdinal = typedArray.getInt(R.styleable.LeaveCard_counterType, LeaveCounter.CounterType.NORMAL.ordinal)
            counterType = LeaveCounter.CounterType.values().getOrElse(typeOrdinal) { LeaveCounter.CounterType.NORMAL }
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupCardAppearance(context: Context) {
        strokeColor = run {
            val fallbackColorRes = R.color.kitColorNeutralGrayLight30
            val resolved = context.resolveColorAttr(R.attr.colorStrokeSubtle, fallbackColorRes)
            try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
        }
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        radius = resources.getDimension(R.dimen.radius_12dp)
        setCardBackgroundColor(run {
            val fallbackColorRes = R.color.kitColorNeutralWhite
            val resolved = context.resolveColorAttr(R.attr.colorBackgroundElevated, fallbackColorRes)
            try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
        })
        cardElevation = 2f.dpToPx

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = run {
                val fallbackColorRes = R.color.kitColorBrandPrimaryA10
                val resolved = context.resolveColorAttr(R.attr.colorShadowTintedAmbient, fallbackColorRes)
                try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
            }
            outlineSpotShadowColor = run {
                val fallbackColorRes = R.color.kitColorBrandPrimaryA20
                val resolved = context.resolveColorAttr(R.attr.colorShadowTintedKey, fallbackColorRes)
                try { ContextCompat.getColor(context, resolved) } catch (e: Exception) { resolved }
            }
        }
        setupClickAnimation()
    }

    private fun updateImageSrc() {
        employeeImageUrl?.let { binding.lEmployeeInfo.employeeImageUrl = it }
        employeeImage?.let { binding.lEmployeeInfo.employeeImage = it }
    }
}