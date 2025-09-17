package com.edts.components.header

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.edts.components.R
import com.edts.components.databinding.HeaderBinding

class Header @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: HeaderBinding =
        HeaderBinding.inflate(LayoutInflater.from(context), this, true)

    private companion object {
        const val TAG = "Header"
        const val CLICK_DEBOUNCE_DELAY = 200L
    }

    var delegate: HeaderDelegate? = null

    private var leftClickCount = 0
    private var rightClickCount = 0
    private var lastLeftClickTime = 0L
    private var lastRightClickTime = 0L

    var showLeftButton: Boolean = true
        set(value) {
            field = value
            binding.ivLeftBtn.visibility = if (value) View.VISIBLE else View.GONE
        }

    var sectionTitleText: String = ""
        set(value) {
            field = value
            binding.tvSectionTitle.text = value
        }

    var showSectionTitle: Boolean = true
        set(value) {
            field = value
            binding.tvSectionTitle.visibility = if (value) View.VISIBLE else View.GONE
        }

    var sectionSubtitleText: String = ""
        set(value) {
            field = value
            binding.tvSectionSubtitle.text = value
        }

    var showSectionSubtitle: Boolean = true
        set(value) {
            field = value
            binding.tvSectionSubtitle.visibility = if (value) View.VISIBLE else View.GONE
        }

    var showRightButton: Boolean = true
        set(value) {
            field = value
            binding.ivRightBtn.visibility = if (value) View.VISIBLE else View.GONE
        }

    var rightButtonSrc: Int = -1
        set(@DrawableRes value) {
            field = value
            if (value != -1) {
                binding.ivRightBtn.setImageResource(value)
            }
        }

    init {
        setupClickListeners()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Header,
            0, 0
        ).apply {
            try {
                showLeftButton = getBoolean(R.styleable.Header_showLeftButton, true)
                sectionTitleText = getString(R.styleable.Header_sectionTitleText) ?: ""
                showSectionTitle = getBoolean(R.styleable.Header_showSectionTitle, true)
                sectionSubtitleText = getString(R.styleable.Header_sectionSubtitleText) ?: ""
                showSectionSubtitle = getBoolean(R.styleable.Header_showSectionSubtitle, true)
                showRightButton = getBoolean(R.styleable.Header_showRightButton, true)

                val rightButtonDrawableRes = getResourceId(R.styleable.Header_rightButtonSrc, -1)

                if (rightButtonDrawableRes != -1) {
                    rightButtonSrc = rightButtonDrawableRes
                }

                updateTitleStyle()
            } finally {
                recycle()
            }
        }
    }

    private fun updateTitleStyle() {
        val shouldUseLargerStyle = !showLeftButton && !showSectionSubtitle

        if (shouldUseLargerStyle) {
            val typedValue = TypedValue()
            if (context.theme.resolveAttribute(R.attr.d3SemiBold, typedValue, true)) {
                binding.tvSectionTitle.setTextAppearance(typedValue.resourceId)
                Log.d(TAG, "Applied d3SemiBold style to title")
            } else {
                Log.w(TAG, "d3SemiBold style not found, keeping current style")
            }
        } else {
            val typedValue = TypedValue()
            if (context.theme.resolveAttribute(R.attr.h1SemiBold, typedValue, true)) {
                binding.tvSectionTitle.setTextAppearance(typedValue.resourceId)
                Log.d(TAG, "Applied h1SemiBold style to title")
            } else {
                Log.w(TAG, "h1SemiBold style not found, keeping current style")
            }
        }
    }

    private fun setupClickListeners() {
        binding.ivLeftBtn.setOnClickListener {
            handleLeftButtonClick()
        }

        binding.ivRightBtn.setOnClickListener {
            handleRightButtonClick()
        }
    }

    private fun handleLeftButtonClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastLeftClickTime > CLICK_DEBOUNCE_DELAY) {
            leftClickCount++
            lastLeftClickTime = currentTime

            Log.d(TAG, "Left button clicked!")
            Log.d(TAG, "  - Button: Left")
            Log.d(TAG, "  - Title: ${sectionTitleText.ifEmpty { "No title" }}")
            Log.d(TAG, "  - Subtitle: ${sectionSubtitleText.ifEmpty { "No subtitle" }}")
            Log.d(TAG, "  - Button Visible: $showLeftButton")
            Log.d(TAG, "  - Total left clicks: $leftClickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Action: Navigate back or left action")
            Log.d(TAG, "--------------------")

            delegate?.onLeftButtonClicked()
        } else {
            Log.d(TAG, "Left button click ignored due to debounce (too fast)")
        }
    }

    private fun handleRightButtonClick() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastRightClickTime > CLICK_DEBOUNCE_DELAY) {
            rightClickCount++
            lastRightClickTime = currentTime

            Log.d(TAG, "Right button clicked!")
            Log.d(TAG, "  - Button: Right")
            Log.d(TAG, "  - Title: ${sectionTitleText.ifEmpty { "No title" }}")
            Log.d(TAG, "  - Subtitle: ${sectionSubtitleText.ifEmpty { "No subtitle" }}")
            Log.d(TAG, "  - Button Visible: $showRightButton")
            Log.d(TAG, "  - Button Source: ${if (rightButtonSrc != -1) rightButtonSrc else "Default"}")
            Log.d(TAG, "  - Total right clicks: $rightClickCount")
            Log.d(TAG, "  - Click timestamp: $currentTime")
            Log.d(TAG, "  - Action: Menu or right action")
            Log.d(TAG, "--------------------")

            delegate?.onRightButtonClicked()
        } else {
            Log.d(TAG, "Right button click ignored due to debounce (too fast)")
        }
    }

    fun resetLeftClickCount() {
        val previousCount = leftClickCount
        leftClickCount = 0
        Log.d(TAG, "Left button click count reset from $previousCount to 0")
    }

    fun resetRightClickCount() {
        val previousCount = rightClickCount
        rightClickCount = 0
        Log.d(TAG, "Right button click count reset from $previousCount to 0")
    }

    fun getLeftClickCount(): Int {
        return leftClickCount
    }

    fun getRightClickCount(): Int {
        return rightClickCount
    }
}