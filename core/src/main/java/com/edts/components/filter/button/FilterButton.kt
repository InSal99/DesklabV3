package com.edts.components.filter.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import com.edts.components.R
import com.edts.components.databinding.FilterBtnBinding
import com.google.android.material.card.MaterialCardView

class FilterButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: FilterBtnBinding = FilterBtnBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val shadowPaint1 = Paint()
    private val shadowPaint2 = Paint()
    private val cornerRadiusPx = 12.toFloat() * context.resources.displayMetrics.density


    init {
        setupShadowPaints()
        setupClickAnimation()
        radius = cornerRadiusPx
    }

    private fun setupShadowPaints() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        shadowPaint1.apply {
            setShadowLayer(
                2f,
                0f,
                0f,
                R.attr.colorShadowNeutralAmbient
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        shadowPaint2.apply {
            setShadowLayer(
                2f,
                0f,
                0f,
                R.attr.colorShadowNeutralKey
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private fun setupClickAnimation() {
        isClickable = true
        isFocusable = true
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
        return super.performClick()
    }

    private fun animateScaleDown() {
        val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.95f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    private fun animateScaleUp() {
        val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", scaleX, 1.0f)
        val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", scaleY, 1.0f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY)
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
        }
        animatorSet.start()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.let { c ->
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

            c.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, shadowPaint1)
            c.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, shadowPaint2)
        }
        super.onDraw(canvas)
    }

}