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
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import com.example.components.R
import com.example.components.databinding.LeaveCardBinding
import com.example.components.event.card.EventCardDelegate
import com.google.android.material.card.MaterialCardView

class LeaveCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: LeaveCardBinding

    private val shadowPaint1 = Paint()
    private val shadowPaint2 = Paint()
    private val cornerRadiusPx = 8.5.toFloat() * context.resources.displayMetrics.density

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
        setupShadowPaints()
        binding = LeaveCardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        setupCard()
        initAttrs(attrs)

        setOnClickListener {
            leaveCardDelegate?.onClick(this)
        }

        setupTouchAnimation()
    }

    private fun setupShadowPaints() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        shadowPaint1.apply {
            setShadowLayer(
                4f,
                0f,
                0f,
                R.attr.colorBackgroundAttentionSubtle
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        shadowPaint2.apply {
            setShadowLayer(
                4f,
                0f,
                0f,
                R.attr.colorForegroundAttentionIntense
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.let { c ->
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

            c.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, shadowPaint1)
            c.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, shadowPaint2)
        }
        super.onDraw(canvas)
    }

    private fun setupCard() {
        strokeColor = getColorFromAttr(R.attr.colorStrokeSubtle)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_weight_1dp)
        radius = resources.getDimension(R.dimen.radius_12dp)
        setCardBackgroundColor(getColorFromAttr(R.attr.colorBackgroundPrimary))

        isClickable = true
        isFocusable = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchAnimation() {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateScale(0.98f)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    animateScale(1.0f)
                }
            }
            false
        }
    }

    private fun animateScale(scale: Float) {
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", scale)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", scale)
        AnimatorSet().apply {
            duration = 150
            playTogether(scaleX, scaleY)
            start()
        }
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

    private fun getColorFromAttr(attr: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attr, typedValue, true)) {
            typedValue.data
        } else {
            Color.GRAY
        }
    }
}