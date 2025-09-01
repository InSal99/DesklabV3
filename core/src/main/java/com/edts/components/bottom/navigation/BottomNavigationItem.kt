package com.edts.components.bottom.navigation

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.edts.components.R
import com.edts.components.databinding.BottomNavigationTabBinding

class BottomNavigationItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: BottomNavigationTabBinding = BottomNavigationTabBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class NavState(val value: Int) {
        ACTIVE(0),
        INACTIVE(1);

        companion object {
            fun fromValue(value: Int): NavState {
                return values().find { it.value == value } ?: INACTIVE
            }
        }
    }

    var navState: NavState = NavState.INACTIVE
        set(value) {
            val previousState = field
            field = value
            updateNavState(animated = previousState != value)
        }

    var navIcon: Int? = null
        set(value) {
            field = value
            updateNavIcon()
        }

    var navText: String? = null
        set(value) {
            field = value
            updateNavText()
        }

    var showBadge: Boolean = false
        set(value) {
            field = value
            updateBadgeVisibility()
        }

    private companion object {
        const val ANIMATION_DURATION = 150L
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BottomNavigationItem,
            0, 0
        ).apply {
            try {
                val navStateValue = getInt(R.styleable.BottomNavigationItem_navState, 1)
                navState = NavState.fromValue(navStateValue)

                val iconResId = getResourceId(R.styleable.BottomNavigationItem_navIcon, -1)
                if (iconResId != -1) {
                    navIcon = iconResId
                }

                navText = getString(R.styleable.BottomNavigationItem_navText)
                showBadge = getBoolean(R.styleable.BottomNavigationItem_navShowBadge, false)

                updateNavState(animated = false)
                updateNavIcon()
                updateNavText()
                updateBadgeVisibility()
            } finally {
                recycle()
            }
        }

        setOnClickListener {
            navState = if (navState == NavState.ACTIVE) NavState.INACTIVE else NavState.ACTIVE
        }
    }

    private fun updateNavState(animated: Boolean = true) {
        if (animated) {
            animateNavStateChange()
        } else {
            applyNavStateImmediately()
        }
    }

    private fun applyNavStateImmediately() {
        val typedValue = android.util.TypedValue()

        when (navState) {
            NavState.ACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundAccentPrimaryIntense, typedValue, true)
                val activeColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.tvBottomNavigation.setTextColor(activeColor)
                binding.ivBottomNavigation.imageTintList = ColorStateList.valueOf(activeColor)

                context.theme.resolveAttribute(R.attr.colorStrokeAccent, typedValue, true)
                val indicatorColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.bottomNavigationIndicator.setBackgroundColor(indicatorColor)
                binding.bottomNavigationIndicator.visibility = View.VISIBLE
            }
            NavState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundTertiary, typedValue, true)
                val inactiveColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.tvBottomNavigation.setTextColor(inactiveColor)
                binding.ivBottomNavigation.imageTintList = ColorStateList.valueOf(inactiveColor)

                context.theme.resolveAttribute(R.attr.colorStrokeSubtle, typedValue, true)
                val indicatorInactiveColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.bottomNavigationIndicator.setBackgroundColor(indicatorInactiveColor)
                binding.bottomNavigationIndicator.visibility = View.VISIBLE
            }
        }
    }

    private fun animateNavStateChange() {
        when (navState) {
            NavState.ACTIVE -> animateToActive()
            NavState.INACTIVE -> animateToInactive()
        }
    }

    private fun animateToActive() {
        val typedValue = android.util.TypedValue()

        context.theme.resolveAttribute(R.attr.colorForegroundAccentPrimaryIntense, typedValue, true)
        val activeColor = ContextCompat.getColor(context, typedValue.resourceId)

        context.theme.resolveAttribute(R.attr.colorStrokeAccent, typedValue, true)
        val indicatorColor = ContextCompat.getColor(context, typedValue.resourceId)

        val currentTextColor = binding.tvBottomNavigation.currentTextColor
        val currentIconColor = binding.ivBottomNavigation.imageTintList?.defaultColor ?: currentTextColor

        val textColorAnimator = ValueAnimator.ofArgb(currentTextColor, activeColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.tvBottomNavigation.setTextColor(color)
            }
        }

        val iconColorAnimator = ValueAnimator.ofArgb(currentIconColor, activeColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.ivBottomNavigation.imageTintList = ColorStateList.valueOf(color)
            }
        }

        binding.bottomNavigationIndicator.visibility = View.VISIBLE
        val indicatorColorAnimator = ValueAnimator.ofArgb(currentTextColor, indicatorColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.bottomNavigationIndicator.setBackgroundColor(color)
            }
        }

        AnimatorSet().apply {
            playTogether(textColorAnimator, iconColorAnimator, indicatorColorAnimator)
            start()
        }
    }

    private fun animateToInactive() {
        val typedValue = android.util.TypedValue()

        context.theme.resolveAttribute(R.attr.colorForegroundTertiary, typedValue, true)
        val inactiveColor = ContextCompat.getColor(context, typedValue.resourceId)

        context.theme.resolveAttribute(R.attr.colorStrokeSubtle, typedValue, true)
        val indicatorInactiveColor = ContextCompat.getColor(context, typedValue.resourceId)

        val currentTextColor = binding.tvBottomNavigation.currentTextColor
        val currentIconColor = binding.ivBottomNavigation.imageTintList?.defaultColor ?: currentTextColor
        val currentIndicatorColor = binding.bottomNavigationIndicator.solidColor

        val textColorAnimator = ValueAnimator.ofArgb(currentTextColor, inactiveColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.tvBottomNavigation.setTextColor(color)
            }
        }

        val iconColorAnimator = ValueAnimator.ofArgb(currentIconColor, inactiveColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.ivBottomNavigation.imageTintList = ColorStateList.valueOf(color)
            }
        }

        val indicatorColorAnimator = ValueAnimator.ofArgb(currentIndicatorColor, indicatorInactiveColor).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.bottomNavigationIndicator.setBackgroundColor(color)
            }
        }

        AnimatorSet().apply {
            playTogether(textColorAnimator, iconColorAnimator, indicatorColorAnimator)
            start()
        }
    }

    private fun updateNavIcon() {
        navIcon?.let {
            binding.ivBottomNavigation.setImageResource(it)
        }
    }

    private fun updateNavText() {
        navText?.let {
            binding.tvBottomNavigation.text = it
        }
    }

    private fun updateBadgeVisibility() {
        binding.cvBadgeBottomNavigation.visibility = if (showBadge) View.VISIBLE else View.GONE
    }
}


//package com.example.components.bottom.navigation
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.FrameLayout
//import androidx.core.content.ContextCompat
//import com.example.components.R
//import com.example.components.databinding.BottomNavigationTabBinding
//
//class BottomNavigationItem @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    private val binding: BottomNavigationTabBinding = BottomNavigationTabBinding.inflate(
//        LayoutInflater.from(context),
//        this,
//        true
//    )
//
//    enum class NavState(val value: Int) {
//        ACTIVE(0),
//        INACTIVE(1);
//
//        companion object {
//            fun fromValue(value: Int): NavState {
//                return values().find { it.value == value } ?: INACTIVE
//            }
//        }
//    }
//
//    var navState: NavState = NavState.INACTIVE
//        set(value) {
//            field = value
//            updateNavState()
//        }
//
//    var navIcon: Int? = null
//        set(value) {
//            field = value
//            updateNavIcon()
//        }
//
//    var navText: String? = null
//        set(value) {
//            field = value
//            updateNavText()
//        }
//
//    var showBadge: Boolean = false
//        set(value) {
//            field = value
//            updateBadgeVisibility()
//        }
//
//    init {
//        // Parse custom attributes
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.BottomNavigationItem,
//            0, 0
//        ).apply {
//            try {
//                val navStateValue = getInt(R.styleable.BottomNavigationItem_navState, 1)
//                navState = NavState.fromValue(navStateValue)
//
//                val iconResId = getResourceId(R.styleable.BottomNavigationItem_navIcon, -1)
//                if (iconResId != -1) {
//                    navIcon = iconResId
//                }
//
//                navText = getString(R.styleable.BottomNavigationItem_navText)
//                showBadge = getBoolean(R.styleable.BottomNavigationItem_navShowBadge, false)
//
//                updateNavState()
//                updateNavIcon()
//                updateNavText()
//                updateBadgeVisibility()
//            } finally {
//                recycle()
//            }
//        }
//    }
//
//    private fun updateNavState() {
//        val typedValue = android.util.TypedValue()
//
//        when (navState) {
//            NavState.ACTIVE -> {
//                // Active state - accent primary intense colors
//                context.theme.resolveAttribute(R.attr.colorForegroundAccentPrimaryIntense, typedValue, true)
//                binding.tvBottomNavigation.setTextColor(ContextCompat.getColor(context, typedValue.resourceId))
//                binding.ivBottomNavigation.imageTintList =
//                    ContextCompat.getColorStateList(context, typedValue.resourceId)
//
//                context.theme.resolveAttribute(R.attr.colorStrokeAccent, typedValue, true)
//                binding.bottomNavigationIndicator.setBackgroundColor(
//                    ContextCompat.getColor(context, typedValue.resourceId)
//                )
//                binding.bottomNavigationIndicator.visibility = View.VISIBLE
//            }
//            NavState.INACTIVE -> {
//                // Inactive state - tertiary colors
//                context.theme.resolveAttribute(R.attr.colorForegroundTertiary, typedValue, true)
//                binding.tvBottomNavigation.setTextColor(ContextCompat.getColor(context, typedValue.resourceId))
//                binding.ivBottomNavigation.imageTintList =
//                    ContextCompat.getColorStateList(context, typedValue.resourceId)
//                binding.bottomNavigationIndicator.setBackgroundColor(
//                    ContextCompat.getColor(context, typedValue.resourceId)
//                )
//                binding.bottomNavigationIndicator.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun updateNavIcon() {
//        navIcon?.let {
//            binding.ivBottomNavigation.setImageResource(it)
//        }
//    }
//
//    private fun updateNavText() {
//        navText?.let {
//            binding.tvBottomNavigation.text = it
//        }
//    }
//
//    private fun updateBadgeVisibility() {
//        binding.cvBadgeBottomNavigation.visibility = if (showBadge) View.VISIBLE else View.GONE
//    }
//}