package com.example.components.card.multi.detail

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.example.components.R
import com.example.components.databinding.CardMultiDetailBinding
import com.google.android.material.card.MaterialCardView

class CardMultiDetail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: CardMultiDetailBinding = CardMultiDetailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

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
            updateLeftSlot()
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

    var cmdTitle: String? = null
        set(value) {
            field = value
            updateCmdTitle()
        }

    var cmdInfo1Text: String? = null
        set(value) {
            field = value
            updateCmdInfo()
        }

    var cmdInfo2Text: String? = null
        set(value) {
            field = value
            updateCmdInfo()
        }

    var cmdShowInfo2: Boolean = true
        set(value) {
            field = value
            updateCmdShowInfo2()
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

    var delegate: CardMultiDetailDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L // 300ms debounce to prevent accidental double clicks

    private companion object {
        const val TAG = "CardMultiDetail"
    }

    init {
        radius = 12f * resources.displayMetrics.density

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardMultiDetail,
            0, 0
        ).apply {
            try {
                val typedValue = TypedValue()
//                if (context.theme.resolveAttribute(R.attr.colorBackgroundModifierOnPress, typedValue, true)) {
//                    val colorStateList = AppCompatResources.getColorStateList(context, typedValue.resourceId)
//                    rippleColor = colorStateList
//                }
                rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)

                val leftSlotTypeValue = getInt(R.styleable.CardMultiDetail_cmdLeftSlotType, 1)
                leftSlotType = LeftSlotType.fromValue(leftSlotTypeValue)

                val leftSlotSrcResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotSrc, -1)
                if (leftSlotSrcResId != -1) {
                    leftSlotSrc = leftSlotSrcResId
                }

                val leftSlotBgColorResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotBackgroundColor, -1)
                if (leftSlotBgColorResId != -1) {
                    leftSlotBackgroundColor = leftSlotBgColorResId
                } else {
                    val leftSlotBgColor = getColor(R.styleable.CardMultiDetail_cmdLeftSlotBackgroundColor, -1)
                    if (leftSlotBgColor != -1) {
                        leftSlotBackgroundColor = leftSlotBgColor
                    }
                }

                val leftSlotTintResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotTint, -1)
                if (leftSlotTintResId != -1) {
                    leftSlotTint = leftSlotTintResId
                } else {
                    val leftSlotTintColor = getColor(R.styleable.CardMultiDetail_cmdLeftSlotTint, -1)
                    if (leftSlotTintColor != -1) {
                        leftSlotTint = leftSlotTintColor
                    }
                }

                cmdTitle = getString(R.styleable.CardMultiDetail_cmdTitle)
                cmdInfo1Text = getString(R.styleable.CardMultiDetail_cmdInfo1Text)
                cmdInfo2Text = getString(R.styleable.CardMultiDetail_cmdInfo2Text)

                // Handle cmdShowInfo2 attribute
                cmdShowInfo2 = getBoolean(R.styleable.CardMultiDetail_cmdShowInfo2, true)

                val rightSlotSrcResId = getResourceId(R.styleable.CardMultiDetail_cmdRightSlotSrc, -1)
                if (rightSlotSrcResId != -1) {
                    rightSlotSrc = rightSlotSrcResId
                }

                val rightSlotTintResId = getResourceId(R.styleable.CardMultiDetail_cmdRightSlotTint, -1)
                if (rightSlotTintResId != -1) {
                    rightSlotTint = rightSlotTintResId
                } else {
                    val rightSlotTintColor = getColor(R.styleable.CardMultiDetail_cmdRightSlotTint, -1)
                    if (rightSlotTintColor != -1) {
                        rightSlotTint = rightSlotTintColor
                    }
                }

                updateLeftSlot()
                updateLeftSlotSrc()
                updateLeftSlotBackgroundColor()
                updateLeftSlotTint()
                updateCmdTitle()
                updateCmdInfo()
                updateCmdShowInfo2()
                updateRightSlotSrc()
                updateRightSlotTint()
                setupCardPressState()
            } finally {
                recycle()
            }
        }
    }

    private fun getCachedColor(@AttrRes colorAttr: Int): Int {
        return colorCache.getOrPut(colorAttr) {
            resolveColorAttribute(colorAttr)
        }
    }

//    private fun createCardBackgroundDrawable(): Drawable {
//        val baseDrawable = GradientDrawable().apply {
//            cornerRadius = 12f * resources.displayMetrics.density
//            setColor(getCachedColor(R.attr.colorBackgroundPrimary))
//        }
//
//        return when (cardState) {
//            CardState.REST -> {
//                val elevatedModifierDrawable = GradientDrawable().apply {
//                    cornerRadius = 12f * resources.displayMetrics.density
//                    setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated))
//                }
//                LayerDrawable(arrayOf(baseDrawable, elevatedModifierDrawable))
//            }
//            CardState.ON_PRESS -> {
//                val pressModifierDrawable = GradientDrawable().apply {
//                    cornerRadius = 12f * resources.displayMetrics.density
//                    setColor(getCachedColor(R.attr.colorBackgroundModifierCardElevated))
//                }
//                LayerDrawable(arrayOf(baseDrawable, pressModifierDrawable))
//            }
//        }
//    }

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
                Log.d(TAG, "ACTION_DOWN - setting ON_PRESS state")
                cardState = CardState.ON_PRESS
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP - setting REST state")
                cardState = CardState.REST
                handleClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "ACTION_CANCEL - setting REST state")
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

            Log.d(TAG, "CardMultiDetail clicked!")
            Log.d(TAG, "  - Title: ${cmdTitle ?: "No title"}")
            Log.d(TAG, "  - Info1: ${cmdInfo1Text ?: "No info1"}")
            Log.d(TAG, "  - Info2: ${cmdInfo2Text ?: "No info2"}")
            Log.d(TAG, "  - Show Info2: $cmdShowInfo2")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Total system clicks: $clickCount")
            Log.d(TAG, "--------------------")

            delegate?.onCardClick(this)
        } else {
            Log.d(TAG, "Click ignored due to debounce (too fast)")
        }
    }

    private fun setupCardPressState() {
        isClickable = true
        isFocusable = true
        updateCardBackground()
    }

    private fun updateLeftSlot() {
        binding.cvCardLeftSlot.slotType = when (leftSlotType) {
            LeftSlotType.IMAGE -> CardLeftSlot.SlotType.IMAGE
            LeftSlotType.ICON -> CardLeftSlot.SlotType.ICON
        }
    }

    private fun updateLeftSlotSrc() {
        leftSlotSrc?.let {
            binding.cvCardLeftSlot.slotSrc = it
        }
    }

    private fun updateLeftSlotBackgroundColor() {
        leftSlotBackgroundColor?.let { color ->
            if (color > 0) {
                val resolvedColor = resolveColorAttribute(color)
                binding.cvCardLeftSlot.slotBackgroundColor = resolvedColor
            } else {
                binding.cvCardLeftSlot.slotBackgroundColor = color
            }
        }
    }

    private fun updateLeftSlotTint() {
        leftSlotTint?.let { color ->
            if (color > 0) {
                val resolvedColor = resolveColorAttribute(color)
                binding.cvCardLeftSlot.slotTint = resolvedColor
            } else {
                binding.cvCardLeftSlot.slotTint = color
            }
        }
    }

    private fun updateCmdTitle() {
        cmdTitle?.let {
            binding.tvCmdTitle.text = it
        }
    }

    private fun updateCmdInfo() {
        cmdInfo1Text?.let {
            binding.cvCmdDescription.info1Text = it
        }
        cmdInfo2Text?.let {
            binding.cvCmdDescription.info2Text = it
        }
    }

    private fun updateCmdShowInfo2() {
        binding.cvCmdDescription.showInfo2 = cmdShowInfo2
    }

    private fun updateRightSlotSrc() {
        rightSlotSrc?.let {
            binding.ivCmdRightSlot.setImageResource(it)
        }
    }

    private fun updateRightSlotTint() {
        rightSlotTint?.let { color ->
            val resolvedColor = if (color > 0) {
                resolveColorAttribute(color)
            } else {
                color
            }
            binding.ivCmdRightSlot.imageTintList = ColorStateList.valueOf(resolvedColor)
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

    fun resetClickCount() {
        val previousCount = clickCount
        clickCount = 0
        Log.d(TAG, "Click count reset from $previousCount to 0")
    }

    fun getClickCount(): Int {
        return clickCount
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "Programmatic click triggered")
        handleClick()
        return super.performClick()
    }
}