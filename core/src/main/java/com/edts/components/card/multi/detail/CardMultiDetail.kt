package com.edts.components.card.multi.detail

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.CardMultiDetailBinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
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

    var cmdShowLeftSlot: Boolean = true
        set(value) {
            field = value
            updateCmdShowLeftSlot()
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

    var cmdShowRightSlot: Boolean = true
        set(value) {
            field = value
            updateCmdShowRightSlot()
            updateClickability()
        }

    enum class CardState {
        REST,
        ON_PRESS
    }

    private var cardState: CardState = CardState.REST
        set(value) {
            field = value
        }

    var delegate: CardMultiDetailDelegate? = null

    private var clickCount = 0
    private var lastClickTime = 0L
    private val clickDebounceDelay = 300L

    private companion object {
        const val TAG = "CardMultiDetail"
    }

    init {
        radius = 12f.dpToPx

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardMultiDetail,
            0, 0
        ).apply {
            try {
                val typedValue = TypedValue()
                rippleColor = if (cmdShowRightSlot) {
                    ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))
                } else{
                    ContextCompat.getColorStateList(context, android.R.color.transparent)
                }

                val leftSlotTypeValue = getInt(R.styleable.CardMultiDetail_cmdLeftSlotType, 1)
                leftSlotType = LeftSlotType.fromValue(leftSlotTypeValue)

                val leftSlotSrcResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotSrc, -1)
                if (leftSlotSrcResId != -1) {
                    leftSlotSrc = leftSlotSrcResId
                }

                val leftSlotBgColorRef = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotBackgroundColor, -1)
                if (leftSlotBgColorRef != -1) {
                    val typedValue = TypedValue()
                    if (context.theme.resolveAttribute(leftSlotBgColorRef, typedValue, true)) {
                        leftSlotBackgroundColor = typedValue.data
                    } else {
                        try {
                            leftSlotBackgroundColor = ContextCompat.getColor(context, leftSlotBgColorRef)
                        } catch (e: Resources.NotFoundException) {
                            leftSlotBackgroundColor = ContextCompat.getColor(context, R.color.colorFFF)
                        }
                    }
                } else {
                    val leftSlotBgColor = getColor(R.styleable.CardMultiDetail_cmdLeftSlotBackgroundColor, -1)
                    if (leftSlotBgColor != -1) {
                        leftSlotBackgroundColor = leftSlotBgColor
                    }
                }

                val leftSlotTintRef = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotTint, -1)
                if (leftSlotTintRef != -1) {
                    val typedValue = TypedValue()
                    if (context.theme.resolveAttribute(leftSlotTintRef, typedValue, true)) {
                        leftSlotTint = typedValue.data
                    } else {
                        try {
                            leftSlotTint = ContextCompat.getColor(context, leftSlotTintRef)
                        } catch (e: Resources.NotFoundException) {
                            leftSlotTint = ContextCompat.getColor(context, R.color.colorFFF)
                        }
                    }
                } else {
                    val leftSlotTintColor = getColor(R.styleable.CardMultiDetail_cmdLeftSlotTint, -1)
                    if (leftSlotTintColor != -1) {
                        leftSlotTint = leftSlotTintColor
                    }
                }

                cmdShowLeftSlot = getBoolean(R.styleable.CardMultiDetail_cmdShowLeftSlot, true)

                cmdTitle = getString(R.styleable.CardMultiDetail_cmdTitle)
                cmdInfo1Text = getString(R.styleable.CardMultiDetail_cmdInfo1Text)
                cmdInfo2Text = getString(R.styleable.CardMultiDetail_cmdInfo2Text)

                cmdShowInfo2 = getBoolean(R.styleable.CardMultiDetail_cmdShowInfo2, true)

                val rightSlotSrcResId = getResourceId(R.styleable.CardMultiDetail_cmdRightSlotSrc, -1)
                if (rightSlotSrcResId != -1) {
                    rightSlotSrc = rightSlotSrcResId
                }

                val rightSlotTintResId = getResourceId(R.styleable.CardMultiDetail_cmdRightSlotTint, -1)
                if (rightSlotTintResId != -1) {
                    rightSlotTint = ContextCompat.getColor(context, rightSlotTintResId)
                } else {
                    val rightSlotTintColor = getColor(R.styleable.CardMultiDetail_cmdRightSlotTint, -1)
                    if (rightSlotTintColor != -1) {
                        rightSlotTint = rightSlotTintColor
                    }
                }

                cmdShowRightSlot = getBoolean(R.styleable.CardMultiDetail_cmdShowRightSlot, true)

                updateLeftSlot()
                updateLeftSlotSrc()
                updateLeftSlotBackgroundColor()
                updateLeftSlotTint()
                updateCmdShowLeftSlot()
                updateCmdTitle()
                updateCmdInfo()
                updateCmdShowInfo2()
                updateRightSlotSrc()
                updateRightSlotTint()
                updateCmdShowRightSlot()
                updateClickability()
                setupCardPressState()

                setOnClickListener {
                    handleClick()
                }
            } finally {
                recycle()
            }
        }
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
            Log.d(TAG, "  - Show Left Slot: $cmdShowLeftSlot")
            Log.d(TAG, "  - Show Right Slot: $cmdShowRightSlot")
            Log.d(TAG, "  - Is Clickable: $isClickable")
            Log.d(TAG, "  - Total clicks: $clickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "--------------------")

            delegate?.onCardClick(this)
        } else {
            Log.d(TAG, "Click ignored due to debounce (too fast)")
        }
    }

    private fun setupCardPressState() {
        updateClickability()
    }

    private fun updateClickability() {
        val shouldBeClickable = cmdShowRightSlot
        isClickable = shouldBeClickable
        isFocusable = shouldBeClickable

        Log.d(TAG, "Clickability updated: $shouldBeClickable (based on cmdShowRightSlot: $cmdShowRightSlot)")

        rippleColor = if (shouldBeClickable) {
            ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))
        } else{
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        }

        if (!shouldBeClickable && cardState == CardState.ON_PRESS) {
            cardState = CardState.REST
        }
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
            binding.cvCardLeftSlot.slotBackgroundColor = color
        }
    }

    private fun updateLeftSlotTint() {
        leftSlotTint?.let { color ->
            binding.cvCardLeftSlot.slotTint = color
        }
    }

    private fun updateCmdShowLeftSlot() {
        binding.cvCardLeftSlot.visibility = if (cmdShowLeftSlot) View.VISIBLE else View.GONE
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
        binding.ivCmdRightSlot.isClickable = true
        binding.ivCmdRightSlot.isFocusable = true

        val rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))

        val rippleDrawable = RippleDrawable(rippleColor, null, null)
        rippleDrawable.radius = 16f.dpToPx.toInt()

        binding.ivCmdRightSlot.background = rippleDrawable

        binding.ivCmdRightSlot.setOnClickListener {
            delegate?.onRightSlotClick(this)
        }

        rightSlotSrc?.let {
            binding.ivCmdRightSlot.setImageResource(it)
        }
    }

    private fun updateRightSlotTint() {
        rightSlotTint?.let { color ->
            binding.ivCmdRightSlot.imageTintList = ColorStateList.valueOf(color)
        }
    }

    private fun updateCmdShowRightSlot() {
        binding.ivCmdRightSlot.visibility = if (cmdShowRightSlot) View.VISIBLE else View.GONE
        rippleColor = if (cmdShowRightSlot) {
            ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.colorNeutral70Opacity20))
        } else{
            ContextCompat.getColorStateList(context, android.R.color.transparent)
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
        if (!isClickable) {
            Log.d(TAG, "Programmatic click ignored - component is not clickable")
            return false
        }

        Log.d(TAG, "Programmatic click triggered")
        handleClick()
        return super.performClick()
    }
}