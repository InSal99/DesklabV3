package com.edts.components.card.multi.detail

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
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
            updateCardBackground()
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
                rippleColor = ContextCompat.getColorStateList(context, android.R.color.transparent)

                val leftSlotTypeValue = getInt(R.styleable.CardMultiDetail_cmdLeftSlotType, 1)
                leftSlotType = LeftSlotType.fromValue(leftSlotTypeValue)

                val leftSlotSrcResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotSrc, -1)
                if (leftSlotSrcResId != -1) {
                    leftSlotSrc = leftSlotSrcResId
                }

                val leftSlotBgColorResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotBackgroundColor, -1)
                if (leftSlotBgColorResId != -1) {
                    val tv = TypedValue()
                    leftSlotBackgroundColor = if (context.theme.resolveAttribute(leftSlotBgColorResId, tv, true)) {
                        if (tv.resourceId != 0) ContextCompat.getColor(context, tv.resourceId) else tv.data
                    } else {
                        ContextCompat.getColor(context, leftSlotBgColorResId)
                    }
                } else {
                    val leftSlotBgColor = getColor(R.styleable.CardMultiDetail_cmdLeftSlotBackgroundColor, -1)
                    if (leftSlotBgColor != -1) {
                        leftSlotBackgroundColor = leftSlotBgColor
                    }
                }

                val leftSlotTintResId = getResourceId(R.styleable.CardMultiDetail_cmdLeftSlotTint, -1)
                if (leftSlotTintResId != -1) {
                    val tv = TypedValue()
                    leftSlotTint = if (context.theme.resolveAttribute(leftSlotTintResId, tv, true)) {
                        if (tv.resourceId != 0) ContextCompat.getColor(context, tv.resourceId) else tv.data
                    } else {
                        ContextCompat.getColor(context, leftSlotTintResId)
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
            } finally {
                recycle()
            }
        }
    }

    // REFACTORED: Now uses resolveColorAttribute directly, with inlined workaround.
    private fun updateCardBackground() {
        if (!isClickable) return

        val fallbackColorRes = android.R.color.transparent

        val resolvedPrimary = context.resolveColorAttribute(R.attr.colorBackgroundPrimary, fallbackColorRes)
        val colorPrimary = try {
            ContextCompat.getColor(context, resolvedPrimary)
        } catch (e: Exception) {
            resolvedPrimary
        }

        when (cardState) {
            CardState.REST -> {
                setCardBackgroundColor(colorPrimary)

                val resolvedElevated = context.resolveColorAttribute(R.attr.colorBackgroundModifierCardElevated, fallbackColorRes)
                val colorElevated = try {
                    ContextCompat.getColor(context, resolvedElevated)
                } catch (e: Exception) {
                    resolvedElevated
                }

                val elevatedModifierDrawable = GradientDrawable().apply {
                    cornerRadius = 12f.dpToPx
                    setColor(colorElevated)
                }
                foreground = elevatedModifierDrawable
            }
            CardState.ON_PRESS -> {
                setCardBackgroundColor(colorPrimary)

                val resolvedOnPress = context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, fallbackColorRes)
                val colorOnPress = try {
                    ContextCompat.getColor(context, resolvedOnPress)
                } catch (e: Exception) {
                    resolvedOnPress
                }

                val overlayDrawable = GradientDrawable().apply {
                    cornerRadius = 12f.dpToPx
                    setColor(colorOnPress)
                }
                foreground = overlayDrawable
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isClickable) {
            return false
        }

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
        updateCardBackground()
    }

    private fun updateClickability() {
        val shouldBeClickable = cmdShowRightSlot
        isClickable = shouldBeClickable
        isFocusable = shouldBeClickable

        Log.d(TAG, "Clickability updated: $shouldBeClickable (based on cmdShowRightSlot: $cmdShowRightSlot)")

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