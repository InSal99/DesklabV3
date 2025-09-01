package com.edts.components.card.detail

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.card.multi.detail.CardLeftSlot
import com.edts.components.databinding.CardDetailInfoBBinding
import com.google.android.material.card.MaterialCardView
import android.view.MotionEvent

class CardDetailInfoB @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: CardDetailInfoBBinding = CardDetailInfoBBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val indicatorPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            2f,
            context.resources.displayMetrics
        )
        isAntiAlias = true
    }

    private val cornerRadiusPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        28f,
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

    var showDescription: Boolean = true
        set(value) {
            field = value
            updateDescriptionVisibility()
        }

    var showRightSlot: Boolean = true
        set(value) {
            field = value
            updateRightSlotVisibility()
        }

    var showLeftSlot: Boolean = true
        set(value) {
            field = value
            updateLeftSlotVisibility()
        }

    enum class CardState {
        REST,
        ON_PRESS
    }

    private var cardState: CardState = CardState.REST
        set(value) {
            field = value
            updateCardBackground()
        }

    private val colorCache = mutableMapOf<Int, Int>()

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

    var leftSlotSize: Int = (32 * context.resources.displayMetrics.density).toInt()
        set(value) {
            field = value
            updateLeftSlotSize()
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

    var delegate: CardDetailInfoBDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L


    init {
        setWillNotDraw(false)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardDetailInfoB,
            0, 0
        ).apply {
            try {
                val typedValue = TypedValue()
//                if (context.theme.resolveAttribute(R.attr.colorBackgroundModifierOnPress, typedValue, true)) {
//                    val colorStateList = AppCompatResources.getColorStateList(context, typedValue.resourceId)
//                    rippleColor = colorStateList
//                }
                rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)

                if (context.theme.resolveAttribute(R.attr.colorStrokeUtilityUlangTahunIntense, typedValue, true)) {
                    indicatorPaint.color = ContextCompat.getColor(context, typedValue.resourceId)
                }

                showIndicator = getBoolean(R.styleable.CardDetailInfoB_cdibShowIndicator, true)

                val indicatorColorRes = getResourceId(R.styleable.CardDetailInfoB_cdibIndicatorColor, -1)
                if (indicatorColorRes != -1) {
                    indicatorColor = indicatorColorRes
                } else {
                    val indicatorColorValue = getColor(R.styleable.CardDetailInfoB_cdibIndicatorColor, -1)
                    if (indicatorColorValue != -1) {
                        indicatorColor = indicatorColorValue
                    }
                }

                showDescription = getBoolean(R.styleable.CardDetailInfoB_cdibShowDescription, true)
                showRightSlot = getBoolean(R.styleable.CardDetailInfoB_cdibShowRightSlot, true)
                showLeftSlot = getBoolean(R.styleable.CardDetailInfoB_cdibShowLeftSlot, true)

                val leftSlotTypeValue = getInt(R.styleable.CardDetailInfoB_cdibLeftSlotType, 1)
                leftSlotType = LeftSlotType.fromValue(leftSlotTypeValue)

                val leftSlotSrcRes = getResourceId(R.styleable.CardDetailInfoB_cdibLeftSlotSrc, -1)
                if (leftSlotSrcRes != -1) {
                    leftSlotSrc = leftSlotSrcRes
                }

                val leftSlotSizeValue = getDimensionPixelSize(R.styleable.CardDetailInfoB_cdibLeftSlotSize, -1)
                if (leftSlotSizeValue != -1) {
                    leftSlotSize = leftSlotSizeValue
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
                updateLeftSlotSize()
                updateLeftSlotBackgroundColor()
                updateLeftSlotTint()
                updateLeftSlotVisibility()
                updateTitleText()
                updateDescText()
                updateDescriptionVisibility()
                updateRightSlotSrc()
                updateRightSlotTint()
                updateRightSlotVisibility()
                updateIndicatorColor()
//                setupCardAppearance()
                setupCardPressState()
            } finally {
                recycle()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        radius = 12f * resources.displayMetrics.density
        if (showIndicator) {
            drawIndicator(canvas)
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        val strokeWidth = indicatorPaint.strokeWidth
        val halfStroke = strokeWidth / 2f
        val path = Path()

        path.moveTo(cornerRadiusPx, halfStroke)
        path.arcTo(
            RectF(
                halfStroke,
                halfStroke,
                cornerRadiusPx * 2 + halfStroke,
                cornerRadiusPx * 2 + halfStroke
            ),
            270f, -90f, false
        )
        path.lineTo(halfStroke, height - cornerRadiusPx)
        path.arcTo(
            RectF(
                halfStroke,
                height - cornerRadiusPx * 2 - halfStroke,
                cornerRadiusPx * 2 + halfStroke,
                height - halfStroke
            ),
            180f, -90f, false
        )

//        path.lineTo(
//            cornerRadiusPx + 2f * resources.displayMetrics.density,
//            height - halfStroke
//        )

        path.arcTo(
            RectF(
                halfStroke + strokeWidth *3,
                height - cornerRadiusPx * 2 - halfStroke,
                cornerRadiusPx * 2 + halfStroke + strokeWidth,
                height - halfStroke
            ),
            90f, 90f, false
        )
        path.lineTo(halfStroke + strokeWidth *3, cornerRadiusPx)
        path.arcTo(
            RectF(
                halfStroke + strokeWidth *3,
                halfStroke,
                cornerRadiusPx * 2 + halfStroke + strokeWidth,
                cornerRadiusPx * 2 + halfStroke
            ),
            180f, 90f, false
        )
//        path.close()
        canvas.drawPath(path, indicatorPaint)
    }

//    private fun setupCardAppearance() {
//        binding.cardDetailInfoB.cardElevation = 2f * context.resources.displayMetrics.density
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            outlineAmbientShadowColor = resolveColorAttribute(R.attr.colorShadowNeutralAmbient)
//            outlineSpotShadowColor = resolveColorAttribute(R.attr.colorShadowNeutralKey)
//        }
//    }

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

    private fun updateDescriptionVisibility() {
        binding.tvCdibDescription.visibility = if (showDescription) View.VISIBLE else View.GONE
        updateContentLayoutConstraints()
    }

    private fun updateContentLayoutConstraints() {
        val titleLayoutParams = binding.tvCdibTitle.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
        val contentLayoutParams = binding.cdibContent.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

        if (showDescription) {
            titleLayoutParams.topToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            titleLayoutParams.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
            titleLayoutParams.bottomToTop = binding.tvCdibDescription.id

            contentLayoutParams.topToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            contentLayoutParams.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        } else {
            titleLayoutParams.topToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            titleLayoutParams.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            titleLayoutParams.bottomToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET

            contentLayoutParams.topToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            contentLayoutParams.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        }

        binding.tvCdibTitle.layoutParams = titleLayoutParams
        binding.cdibContent.layoutParams = contentLayoutParams
    }

    private fun updateRightSlotVisibility() {
        binding.ivCdibRightSlot.visibility = if (showRightSlot) View.VISIBLE else View.GONE
    }

    private fun updateLeftSlotVisibility() {
        binding.cvCardLeftSlotCdib.visibility = if (showLeftSlot) View.VISIBLE else View.GONE
        updateContentMarginAndPadding()
    }

    private fun updateContentMarginAndPadding() {
        val contentLayoutParams = binding.cdibContent.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

        if (showLeftSlot) {
            contentLayoutParams.marginStart = (4 * resources.displayMetrics.density).toInt()
            contentLayoutParams.startToEnd = binding.cvCardLeftSlotCdib.id
            contentLayoutParams.startToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET

            binding.cdibContent.setPadding(
                (4 * resources.displayMetrics.density).toInt(),
                binding.cdibContent.paddingTop,
                (4 * resources.displayMetrics.density).toInt(),
                binding.cdibContent.paddingBottom
            )
        } else {
            contentLayoutParams.marginStart = 0
            contentLayoutParams.startToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
            contentLayoutParams.startToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID

            binding.cdibContent.setPadding(
                0,
                binding.cdibContent.paddingTop,
                0,
                binding.cdibContent.paddingBottom
            )
        }

        binding.cdibContent.layoutParams = contentLayoutParams
    }

    private fun getCachedColor(@AttrRes colorAttr: Int): Int {
        return colorCache.getOrPut(colorAttr) {
            resolveColorAttribute(colorAttr)
        }
    }

    private fun updateCardBackground() {
        when (cardState) {
            CardState.REST -> {
                setCardBackgroundColor(getCachedColor(R.attr.colorBackgroundPrimary))
                val elevatedModifierDrawable = GradientDrawable().apply {
                    cornerRadius = 12f * resources.displayMetrics.density
                    setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated))
                }
                foreground = elevatedModifierDrawable
            }
            CardState.ON_PRESS -> {
                setCardBackgroundColor(getCachedColor(R.attr.colorBackgroundPrimary))
                val overlayDrawable = GradientDrawable().apply {
                    cornerRadius = 12f * resources.displayMetrics.density
                    setColor(getCachedColor(R.attr.colorBackgroundModifierOnPress))
                }
                foreground = overlayDrawable
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("CardTouch", "ACTION_DOWN - setting ON_PRESS state")
                cardState = CardState.ON_PRESS
            }
            MotionEvent.ACTION_UP -> {
                Log.d("CardTouch", "ACTION_UP - setting REST state")
                cardState = CardState.REST
                handleClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("CardTouch", "ACTION_CANCEL - setting REST state")
                cardState = CardState.REST
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > clickDebounceDelay) {
            clickCount++
            lastClickTime = currentTime

            Log.d("CardClick", "Card clicked! Total clicks: $clickCount")
            Log.d("CardClick", "Click timestamp: $currentTime")

            delegate?.onCardClick(this)
        } else {
            Log.d("CardClick", "Click ignored due to debounce (too fast)")
        }
    }

    fun resetClickCount() {
        val previousCount = clickCount
        clickCount = 0
        Log.d("CardClick", "Click count reset from $previousCount to 0")
    }

    fun getClickCount(): Int {
        return clickCount
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
        updateCardBackground()
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

    private fun updateLeftSlotSize() {
        val layoutParams = binding.cvCardLeftSlotCdib.layoutParams
        layoutParams.width = leftSlotSize
        layoutParams.height = leftSlotSize
        binding.cvCardLeftSlotCdib.layoutParams = layoutParams
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

    private fun resolveColorAttribute(colorRes: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(colorRes, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            try {
                ContextCompat.getColor(context, colorRes)
            } catch (e: Exception) {
                colorRes
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