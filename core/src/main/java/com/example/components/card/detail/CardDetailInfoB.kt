package com.example.components.card.detail

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.components.R
import com.example.components.card.multi.detail.CardLeftSlot
import com.example.components.databinding.CardDetailInfoBBinding

class CardDetailInfoB @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: CardDetailInfoBBinding = CardDetailInfoBBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val indicatorPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            8f,
            context.resources.displayMetrics
        )
        isAntiAlias = true
    }

    private val cornerRadiusPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        24f,
        context.resources.displayMetrics
    )

    var showIndicator: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var indicatorColor: Int? = null
        set(value) {
            field = value
            updateIndicatorColor()
        }

    enum class LeftSlotType(val value: Int) {
        IMAGE(0),
        ICON(1);

        companion object {
            fun fromValue(value: Int): LeftSlotType {
                return values().find { it.value == value } ?: ICON
            }
        }
    }

    var leftSlotType: LeftSlotType = LeftSlotType.ICON
        set(value) {
            field = value
            updateLeftSlotType()
        }

    var leftSlotSrc: Int? = null
        set(value) {
            field = value
            updateLeftSlotSrc()
        }

    var leftSlotBackgroundColor: Int? = null
        set(value) {
            field = value
            updateLeftSlotBackgroundColor()
        }

    var leftSlotTint: Int? = null
        set(value) {
            field = value
            updateLeftSlotTint()
        }

    var titleText: String? = null
        set(value) {
            field = value
            updateTitleText()
        }

    var descText: String? = null
        set(value) {
            field = value
            updateDescText()
        }

    var rightSlotSrc: Int? = null
        set(value) {
            field = value
            updateRightSlotSrc()
        }

    var rightSlotTint: Int? = null
        set(value) {
            field = value
            updateRightSlotTint()
        }

    init {
        setWillNotDraw(false)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardDetailInfoB,
            0, 0
        ).apply {
            try {
                val typedValue = TypedValue()
                if (context.theme.resolveAttribute(R.attr.colorStrokeUtilityUlangTahunIntense, typedValue, true)) {
                    indicatorPaint.color = ContextCompat.getColor(context, typedValue.resourceId)
                }

                // Handle cdibShowIndicator attribute
                showIndicator = getBoolean(R.styleable.CardDetailInfoB_cdibShowIndicator, true)

                // Handle cdibIndicatorColor attribute
                val indicatorColorRes = getResourceId(R.styleable.CardDetailInfoB_cdibIndicatorColor, -1)
                if (indicatorColorRes != -1) {
                    indicatorColor = indicatorColorRes
                } else {
                    val indicatorColorValue = getColor(R.styleable.CardDetailInfoB_cdibIndicatorColor, -1)
                    if (indicatorColorValue != -1) {
                        indicatorColor = indicatorColorValue
                    }
                }

                val leftSlotTypeValue = getInt(R.styleable.CardDetailInfoB_cdibLeftSlotType, 1)
                leftSlotType = LeftSlotType.fromValue(leftSlotTypeValue)

                val leftSlotSrcRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotSrc, -1)
                if (leftSlotSrcRes != -1) {
                    leftSlotSrc = leftSlotSrcRes
                }

                val leftSlotBgColorRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotBackgroundColor, -1)
                if (leftSlotBgColorRes != -1) {
                    leftSlotBackgroundColor = leftSlotBgColorRes
                } else {
                    val leftSlotBgColor = getColor(R.styleable.CardDetailInfoB_cdibLeftSlotBackgroundColor, -1)
                    if (leftSlotBgColor != -1) {
                        leftSlotBackgroundColor = leftSlotBgColor
                    }
                }

                val leftSlotTintRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotTint, -1)
                if (leftSlotTintRes != -1) {
                    leftSlotTint = leftSlotTintRes
                } else {
                    val leftSlotTintColor = getColor(R.styleable.CardDetailInfoB_cdibLeftSlotTint, -1)
                    if (leftSlotTintColor != -1) {
                        leftSlotTint = leftSlotTintColor
                    }
                }

                titleText = getString(R.styleable.CardDetailInfoB_cdibTitleText)
                descText = getString(R.styleable.CardDetailInfoB_cdibDescText)

                val rightSlotSrcRes = getResourceId(R.styleable.CardDetailInfoB_cdibRightSlotSrc, -1)
                if (rightSlotSrcRes != -1) {
                    rightSlotSrc = rightSlotSrcRes
                }

                val rightSlotTintRes = getResourceId(R.styleable.CardDetailInfoB_cdibRightSlotTint, -1)
                if (rightSlotTintRes != -1) {
                    rightSlotTint = rightSlotTintRes
                } else {
                    val rightSlotTintColor = getColor(R.styleable.CardDetailInfoB_cdibRightSlotTint, -1)
                    if (rightSlotTintColor != -1) {
                        rightSlotTint = rightSlotTintColor
                    }
                }

                updateLeftSlotType()
                updateLeftSlotSrc()
                updateLeftSlotBackgroundColor()
                updateLeftSlotTint()
                updateTitleText()
                updateDescText()
                updateRightSlotSrc()
                updateRightSlotTint()
                updateIndicatorColor()
            } finally {
                recycle()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas) // Draw all children first

        if (showIndicator) {
            drawIndicator(canvas) // Draw indicator on top
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        val strokeWidth = indicatorPaint.strokeWidth
        val halfStroke = strokeWidth / 2f
        val path = Path()

        path.moveTo(cornerRadiusPx, halfStroke)
        path.arcTo(
            RectF(halfStroke, halfStroke, cornerRadiusPx * 2 + halfStroke, cornerRadiusPx * 2 + halfStroke),
            270f, -90f, false
        )
        path.lineTo(halfStroke, height - cornerRadiusPx)
        path.arcTo(
            RectF(halfStroke, height - cornerRadiusPx * 2 - halfStroke, cornerRadiusPx * 2 + halfStroke, height - halfStroke),
            180f, -90f, false
        )

        canvas.drawPath(path, indicatorPaint)
    }

    private fun updateIndicatorColor() {
        indicatorColor?.let { color ->
            if (color > 0) {
                val resolvedColor = resolveColorAttribute(color)
                indicatorPaint.color = resolvedColor
            } else {
                indicatorPaint.color = color
            }
            invalidate()
        }
    }

    private fun updateLeftSlotType() {
        binding.cvCardLeftSlotCdib.slotType = when (leftSlotType) {
            LeftSlotType.IMAGE -> CardLeftSlot.SlotType.IMAGE
            LeftSlotType.ICON -> CardLeftSlot.SlotType.ICON
        }
    }

    private fun updateLeftSlotSrc() {
        leftSlotSrc?.let {
            binding.cvCardLeftSlotCdib.slotSrc = it
        }
    }

    private fun updateLeftSlotBackgroundColor() {
        leftSlotBackgroundColor?.let { color ->
            if (color > 0) {
                val resolvedColor = resolveColorAttribute(color)
                binding.cvCardLeftSlotCdib.slotBackgroundColor = resolvedColor
            } else {
                binding.cvCardLeftSlotCdib.slotBackgroundColor = color
            }
        }
    }

    private fun updateLeftSlotTint() {
        leftSlotTint?.let { color ->
            if (color > 0) {
                val resolvedColor = resolveColorAttribute(color)
                binding.cvCardLeftSlotCdib.slotTint = resolvedColor
            } else {
                binding.cvCardLeftSlotCdib.slotTint = color
            }
        }
    }

    private fun resolveColorAttribute(colorAttr: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(colorAttr, typedValue, true)) {
            ContextCompat.getColor(context, typedValue.resourceId)
        } else {
            try {
                ContextCompat.getColor(context, colorAttr)
            } catch (e: Exception) {
                colorAttr
            }
        }
    }

    private fun updateTitleText() {
        titleText?.let {
            binding.tvCdibTitle.text = it
        }
    }

    private fun updateDescText() {
        descText?.let {
            binding.tvCdibDescription.text = it
        }
    }

    private fun updateRightSlotSrc() {
        rightSlotSrc?.let {
            binding.ivCdibRightSlot.setImageResource(it)
        }
    }

    private fun updateRightSlotTint() {
        rightSlotTint?.let { colorResOrValue ->
            try {
                val typedValue = TypedValue()
                if (context.theme.resolveAttribute(colorResOrValue, typedValue, true)) {
                    binding.ivCdibRightSlot.imageTintList =
                        ContextCompat.getColorStateList(context, typedValue.resourceId)
                } else {
                    binding.ivCdibRightSlot.imageTintList =
                        ContextCompat.getColorStateList(context, colorResOrValue)
                }
            } catch (e: Exception) {
                binding.ivCdibRightSlot.imageTintList = ColorStateList.valueOf(colorResOrValue)
            }
        }
    }
}

//package com.example.components.card.detail
//
//import android.content.Context
//import android.content.res.ColorStateList
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.Path
//import android.graphics.RectF
//import android.util.AttributeSet
//import android.util.TypedValue
//import android.view.LayoutInflater
//import android.widget.FrameLayout
//import androidx.core.content.ContextCompat
//import com.example.components.R
//import com.example.components.card.multi.detail.CardLeftSlot
//import com.example.components.databinding.CardDetailInfoBBinding
//
//class CardDetailInfoB @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    private val binding: CardDetailInfoBBinding = CardDetailInfoBBinding.inflate(
//        LayoutInflater.from(context),
//        this,
//        true
//    )
//
//    private val indicatorPaint = Paint().apply {
//        style = Paint.Style.STROKE
//        strokeWidth = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_PX,
//            8f,
//            context.resources.displayMetrics
//        )
//        isAntiAlias = true
//    }
//
//    private val cornerRadiusPx = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_PX,
//        24f,
//        context.resources.displayMetrics
//    )
//
//    var showIndicator: Boolean = true
//        set(value) {
//            field = value
//            invalidate()
//        }
//
//    enum class LeftSlotType(val value: Int) {
//        IMAGE(0),
//        ICON(1);
//
//        companion object {
//            fun fromValue(value: Int): LeftSlotType {
//                return values().find { it.value == value } ?: ICON
//            }
//        }
//    }
//
//    var leftSlotType: LeftSlotType = LeftSlotType.ICON
//        set(value) {
//            field = value
//            updateLeftSlotType()
//        }
//
//    var leftSlotSrc: Int? = null
//        set(value) {
//            field = value
//            updateLeftSlotSrc()
//        }
//
//    var leftSlotBackgroundColor: Int? = null
//        set(value) {
//            field = value
//            updateLeftSlotBackgroundColor()
//        }
//
//    var leftSlotTint: Int? = null
//        set(value) {
//            field = value
//            updateLeftSlotTint()
//        }
//
//    var titleText: String? = null
//        set(value) {
//            field = value
//            updateTitleText()
//        }
//
//    var descText: String? = null
//        set(value) {
//            field = value
//            updateDescText()
//        }
//
//    var rightSlotSrc: Int? = null
//        set(value) {
//            field = value
//            updateRightSlotSrc()
//        }
//
//    var rightSlotTint: Int? = null
//        set(value) {
//            field = value
//            updateRightSlotTint()
//        }
//
//    init {
//        setWillNotDraw(false)
//
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.CardDetailInfoB,
//            0, 0
//        ).apply {
//            try {
//                val typedValue = TypedValue()
//                if (context.theme.resolveAttribute(R.attr.colorStrokeUtilityUlangTahunIntense, typedValue, true)) {
//                    indicatorPaint.color = ContextCompat.getColor(context, typedValue.resourceId)
//                }
//
//                val leftSlotTypeValue = getInt(R.styleable.CardDetailInfoB_cdibLeftSlotType, 1)
//                leftSlotType = LeftSlotType.fromValue(leftSlotTypeValue)
//
//                val leftSlotSrcRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotSrc, -1)
//                if (leftSlotSrcRes != -1) {
//                    leftSlotSrc = leftSlotSrcRes
//                }
//
//                val leftSlotBgColorRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotBackgroundColor, -1)
//                if (leftSlotBgColorRes != -1) {
//                    leftSlotBackgroundColor = leftSlotBgColorRes
//                } else {
//                    val leftSlotBgColor = getColor(R.styleable.CardDetailInfoB_cdibLeftSlotBackgroundColor, -1)
//                    if (leftSlotBgColor != -1) {
//                        leftSlotBackgroundColor = leftSlotBgColor
//                    }
//                }
//
//                val leftSlotTintRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotTint, -1)
//                if (leftSlotTintRes != -1) {
//                    leftSlotTint = leftSlotTintRes
//                } else {
//                    val leftSlotTintColor = getColor(R.styleable.CardDetailInfoB_cdibLeftSlotTint, -1)
//                    if (leftSlotTintColor != -1) {
//                        leftSlotTint = leftSlotTintColor
//                    }
//                }
//
//                titleText = getString(R.styleable.CardDetailInfoB_cdibTitleText)
//                descText = getString(R.styleable.CardDetailInfoB_cdibDescText)
//
//                val rightSlotSrcRes = getResourceId(R.styleable.CardDetailInfoB_cdibRightSlotSrc, -1)
//                if (rightSlotSrcRes != -1) {
//                    rightSlotSrc = rightSlotSrcRes
//                }
//
//                val rightSlotTintRes = getResourceId(R.styleable.CardDetailInfoB_cdibRightSlotTint, -1)
//                if (rightSlotTintRes != -1) {
//                    rightSlotTint = rightSlotTintRes
//                } else {
//                    val rightSlotTintColor = getColor(R.styleable.CardDetailInfoB_cdibRightSlotTint, -1)
//                    if (rightSlotTintColor != -1) {
//                        rightSlotTint = rightSlotTintColor
//                    }
//                }
//
//                updateLeftSlotType()
//                updateLeftSlotSrc()
//                updateLeftSlotBackgroundColor()
//                updateLeftSlotTint()
//                updateTitleText()
//                updateDescText()
//                updateRightSlotSrc()
//                updateRightSlotTint()
//            } finally {
//                recycle()
//            }
//        }
//    }
//
//    override fun dispatchDraw(canvas: Canvas) {
//        super.dispatchDraw(canvas) // Draw all children first
//
//        if (showIndicator) {
//            drawIndicator(canvas) // Draw indicator on top
//        }
//    }
//
//    private fun drawIndicator(canvas: Canvas) {
//        val strokeWidth = indicatorPaint.strokeWidth
//        val halfStroke = strokeWidth / 2f
//        val path = Path()
//
//        path.moveTo(cornerRadiusPx, halfStroke)
//        path.arcTo(
//            RectF(halfStroke, halfStroke, cornerRadiusPx * 2 + halfStroke, cornerRadiusPx * 2 + halfStroke),
//            270f, -90f, false
//        )
//        path.lineTo(halfStroke, height - cornerRadiusPx)
//        path.arcTo(
//            RectF(halfStroke, height - cornerRadiusPx * 2 - halfStroke, cornerRadiusPx * 2 + halfStroke, height - halfStroke),
//            180f, -90f, false
//        )
//
//        canvas.drawPath(path, indicatorPaint)
//    }
//
//    private fun updateLeftSlotType() {
//        binding.cvCardLeftSlotCdib.slotType = when (leftSlotType) {
//            LeftSlotType.IMAGE -> CardLeftSlot.SlotType.IMAGE
//            LeftSlotType.ICON -> CardLeftSlot.SlotType.ICON
//        }
//    }
//
//    private fun updateLeftSlotSrc() {
//        leftSlotSrc?.let {
//            binding.cvCardLeftSlotCdib.slotSrc = it
//        }
//    }
//
//    private fun updateLeftSlotBackgroundColor() {
//        leftSlotBackgroundColor?.let { color ->
//            if (color > 0) {
//                val resolvedColor = resolveColorAttribute(color)
//                binding.cvCardLeftSlotCdib.slotBackgroundColor = resolvedColor
//            } else {
//                binding.cvCardLeftSlotCdib.slotBackgroundColor = color
//            }
//        }
//    }
//
//    private fun updateLeftSlotTint() {
//        leftSlotTint?.let { color ->
//            if (color > 0) {
//                val resolvedColor = resolveColorAttribute(color)
//                binding.cvCardLeftSlotCdib.slotTint = resolvedColor
//            } else {
//                binding.cvCardLeftSlotCdib.slotTint = color
//            }
//        }
//    }
//
//    private fun resolveColorAttribute(colorAttr: Int): Int {
//        val typedValue = TypedValue()
//        return if (context.theme.resolveAttribute(colorAttr, typedValue, true)) {
//            ContextCompat.getColor(context, typedValue.resourceId)
//        } else {
//            try {
//                ContextCompat.getColor(context, colorAttr)
//            } catch (e: Exception) {
//                colorAttr
//            }
//        }
//    }
//
//    private fun updateTitleText() {
//        titleText?.let {
//            binding.tvCdibTitle.text = it
//        }
//    }
//
//    private fun updateDescText() {
//        descText?.let {
//            binding.tvCdibDescription.text = it
//        }
//    }
//
//    private fun updateRightSlotSrc() {
//        rightSlotSrc?.let {
//            binding.ivCdibRightSlot.setImageResource(it)
//        }
//    }
//
//    private fun updateRightSlotTint() {
//        rightSlotTint?.let { colorResOrValue ->
//            try {
//                val typedValue = TypedValue()
//                if (context.theme.resolveAttribute(colorResOrValue, typedValue, true)) {
//                    binding.ivCdibRightSlot.imageTintList =
//                        ContextCompat.getColorStateList(context, typedValue.resourceId)
//                } else {
//                    binding.ivCdibRightSlot.imageTintList =
//                        ContextCompat.getColorStateList(context, colorResOrValue)
//                }
//            } catch (e: Exception) {
//                binding.ivCdibRightSlot.imageTintList = ColorStateList.valueOf(colorResOrValue)
//            }
//        }
//    }
//}