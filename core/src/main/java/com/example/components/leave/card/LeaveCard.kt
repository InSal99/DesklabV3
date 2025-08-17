package com.example.components.leave.card

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.components.R
import com.example.components.databinding.LeaveCardBinding
import com.example.components.event.card.EventCardDelegate
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

    var employeeImage: Drawable?
        get() = binding.lEmployeeInfo.employeeImage
        set(value) {
            binding.lEmployeeInfo.employeeImage = value
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animateScaleDown()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animateScaleUp()
                if (event.action == MotionEvent.ACTION_UP) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        Log.d("LeaveCard", "leave card selected")
        leaveCardDelegate?.onClick(this)
        return super.performClick()
    }

    private fun animateScaleDown() {
        val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.95f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = 150
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    private fun animateScaleUp() {
        val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1.0f)
        val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1.0f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY)
            duration = 150
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    private fun setupClickAnimation() {
        isClickable = true
        isFocusable = true
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeaveCard, 0, 0)
        try {
            employeeName = typedArray.getString(R.styleable.LeaveCard_employeeName)
            employeeRole = typedArray.getString(R.styleable.LeaveCard_employeeRole)
            employeeImage = typedArray.getDrawable(R.styleable.LeaveCard_employeeImage)

            counterText = typedArray.getString(R.styleable.LeaveCard_counterText)

            val typeOrdinal = typedArray.getInt(R.styleable.LeaveCard_counterType, LeaveCounter.CounterType.NORMAL.ordinal)
            counterType = LeaveCounter.CounterType.values().getOrElse(typeOrdinal) { LeaveCounter.CounterType.NORMAL }
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupCardAppearance(context: Context) {
        strokeColor = resolveColorAttribute(R.attr.colorStrokeSubtle, R.color.colorNeutral30)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        radius = resources.getDimension(R.dimen.radius_12dp)
        setCardBackgroundColor(resolveColorAttribute(R.attr.colorBackgroundPrimary, R.color.colorFFF))

        cardElevation = 2f * context.resources.displayMetrics.density

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = resolveColorAttribute(
                R.attr.colorShadowNeutralAmbient,
                R.color.colorGreen50
            )
            outlineSpotShadowColor = resolveColorAttribute(
                R.attr.colorShadowNeutralKey,
                R.color.colorGreen50
            )
        }

        setupClickAnimation()
    }

    private fun resolveColorAttribute(@AttrRes attrRes: Int, @ColorRes fallbackColor: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attrRes, typedValue, true)) {
            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            ContextCompat.getColor(context, fallbackColor)
        }
    }
}