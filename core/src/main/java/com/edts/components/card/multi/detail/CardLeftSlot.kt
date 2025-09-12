package com.edts.components.card.multi.detail

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.CardLeftSlotBinding

class CardLeftSlot @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: CardLeftSlotBinding = CardLeftSlotBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class SlotType(val value: Int) {
        IMAGE(0),
        ICON(1);

        companion object {
            fun fromValue(value: Int): SlotType {
                return values().find { it.value == value } ?: IMAGE
            }
        }
    }

    var slotType: SlotType = SlotType.IMAGE
        set(value) {
            field = value
            updateSlotType()
        }

    var slotSrc: Int? = null
        set(value) {
            field = value
            updateSlotSrc()
        }

    var slotBackgroundColor: Int? = null
        set(value) {
            field = value
            updateSlotBackgroundColor()
        }

    var slotTint: Int? = null
        set(value) {
            field = value
            updateSlotTint()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardLeftSlot,
            0, 0
        ).apply {
            try {
                val slotTypeValue = getInt(R.styleable.CardLeftSlot_slotType, 0)
                slotType = SlotType.fromValue(slotTypeValue)

                val srcResId = getResourceId(R.styleable.CardLeftSlot_slotSrc, -1)
                if (srcResId != -1) {
                    slotSrc = srcResId
                }

                val backgroundColorResId = getResourceId(R.styleable.CardLeftSlot_slotBackgroundColor, -1)
                if (backgroundColorResId != -1) {
                    slotBackgroundColor = backgroundColorResId
                } else {
                    val color = getColor(R.styleable.CardLeftSlot_slotBackgroundColor, -1)
                    if (color != -1) {
                        slotBackgroundColor = color
                    }
                }

                val tintResId = getResourceId(R.styleable.CardLeftSlot_slotTint, -1)
                if (tintResId != -1) {
                    slotTint = tintResId
                } else {
                    val tintColor = getColor(R.styleable.CardLeftSlot_slotTint, -1)
                    if (tintColor != -1) {
                        slotTint = tintColor
                    }
                }

                updateSlotType()
                updateSlotSrc()
                updateSlotBackgroundColor()
                updateSlotTint()
            } finally {
                recycle()
            }
        }
    }

    private fun updateSlotType() {
        val layoutParams = binding.ivCardLeftSlot.layoutParams
        when (slotType) {
            SlotType.IMAGE -> {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            SlotType.ICON -> {
                val sizePx = dpToPx(20f)
                layoutParams.width = sizePx
                layoutParams.height = sizePx
            }
        }
        binding.ivCardLeftSlot.layoutParams = layoutParams
    }

    private fun updateSlotSrc() {
        slotSrc?.let {
            binding.ivCardLeftSlot.setImageResource(it)
        }
    }

    private fun updateSlotBackgroundColor() {
        slotBackgroundColor?.let { colorRes ->
            try {
                // Try to resolve as color resource first
                val color = ContextCompat.getColor(context, colorRes)
                binding.cardLeftSlot.setCardBackgroundColor(color)
            } catch (e: Exception) {
                try {
                    // Try to resolve as theme attribute
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(colorRes, typedValue, true)
                    val color = ContextCompat.getColor(context, typedValue.resourceId)
                    binding.cardLeftSlot.setCardBackgroundColor(color)
                } catch (e: Exception) {
                    // Use as direct color value
                    binding.cardLeftSlot.setCardBackgroundColor(colorRes)
                }
            }
        }
    }

    private fun updateSlotTint() {
        slotTint?.let { tintRes ->
            try {
                // Try to resolve as color resource first
                val colorStateList = ContextCompat.getColorStateList(context, tintRes)
                binding.ivCardLeftSlot.imageTintList = colorStateList
            } catch (e: Exception) {
                try {
                    // Try to resolve as theme attribute
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(tintRes, typedValue, true)
                    val colorStateList = ContextCompat.getColorStateList(context, typedValue.resourceId)
                    binding.ivCardLeftSlot.imageTintList = colorStateList
                } catch (e: Exception) {
                    // Use as direct color value
                    binding.ivCardLeftSlot.imageTintList = ColorStateList.valueOf(tintRes)
                }
            }
        }
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}