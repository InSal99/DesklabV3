package com.edts.components.card.multi.detail

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.CardMultiDetailBinding

class CardMultiDetail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardMultiDetail,
            0, 0
        ).apply {
            try {
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
                updateRightSlotSrc()
                updateRightSlotTint()
            } finally {
                recycle()
            }
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
}