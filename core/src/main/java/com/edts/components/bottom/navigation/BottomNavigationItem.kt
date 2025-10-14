package com.edts.components.bottom.navigation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
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

            if (previousState != value) {
                delegate?.onBottomNavigationItemStateChanged(this, value, previousState)
            }
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

    var delegate: BottomNavigationDelegate? = null

    var itemPosition: Int = -1

    var clickCount: Int = 0
        private set(value) {
            field = value
        }

    private companion object {
        const val ANIMATION_DURATION = 150L
        const val BOUNCE_ANIMATION_DURATION = 600L
        const val INDICATOR_ANIMATION_DURATION = 200L
        const val BOUNCE_SCALE_FACTOR = 1.15f
        const val BOUNCE_OVERSHOOT_TENSION = 1.2f
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

        setupClickListener()
    }

    private fun setupClickListener() {
        setOnClickListener {
            handleItemClick()
        }
    }

    private fun handleItemClick() {
        clickCount++
        animateIconBounce()

        if (delegate != null) {
            delegate?.onBottomNavigationItemClicked(this, itemPosition, clickCount)
        } else {
            val newState = if (navState == NavState.ACTIVE) NavState.INACTIVE else NavState.ACTIVE
            navState = newState
        }
    }

    fun resetClickCount() {
        val oldCount = clickCount
        clickCount = 0
    }

    private fun animateIconBounce() {
        val currentScaleX = binding.ivBottomNavigation.scaleX
        val currentScaleY = binding.ivBottomNavigation.scaleY

        val scaleUpX = ObjectAnimator.ofFloat(
            binding.ivBottomNavigation,
            "scaleX",
            currentScaleX,
            BOUNCE_SCALE_FACTOR
        )
        val scaleUpY = ObjectAnimator.ofFloat(
            binding.ivBottomNavigation,
            "scaleY",
            currentScaleY,
            BOUNCE_SCALE_FACTOR
        )

        val scaleDownX = ObjectAnimator.ofFloat(
            binding.ivBottomNavigation,
            "scaleX",
            BOUNCE_SCALE_FACTOR,
            1.0f
        )
        val scaleDownY = ObjectAnimator.ofFloat(
            binding.ivBottomNavigation,
            "scaleY",
            BOUNCE_SCALE_FACTOR,
            1.0f
        )

        val scaleUpAnimator = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY)
            duration = BOUNCE_ANIMATION_DURATION / 3
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleDownAnimator = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY)
            duration = (BOUNCE_ANIMATION_DURATION * 2) / 3
            interpolator = OvershootInterpolator(BOUNCE_OVERSHOOT_TENSION)
        }

        val bounceAnimator = AnimatorSet().apply {
            play(scaleDownAnimator).after(scaleUpAnimator)
        }

        bounceAnimator.start()
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
                binding.bottomNavigationIndicator.scaleX = 1f
            }
            NavState.INACTIVE -> {
                context.theme.resolveAttribute(R.attr.colorForegroundTertiary, typedValue, true)
                val inactiveColor = ContextCompat.getColor(context, typedValue.resourceId)
                binding.tvBottomNavigation.setTextColor(inactiveColor)
                binding.ivBottomNavigation.imageTintList = ColorStateList.valueOf(inactiveColor)

                binding.bottomNavigationIndicator.visibility = View.GONE
                binding.bottomNavigationIndicator.scaleX = 0f
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

        binding.bottomNavigationIndicator.setBackgroundColor(indicatorColor)
        binding.bottomNavigationIndicator.visibility = View.VISIBLE
        binding.bottomNavigationIndicator.scaleX = 0f

        binding.bottomNavigationIndicator.post {
            binding.bottomNavigationIndicator.pivotX = binding.bottomNavigationIndicator.width / 2f

            val indicatorAnimator = ObjectAnimator.ofFloat(binding.bottomNavigationIndicator, "scaleX", 0f, 1f).apply {
                duration = INDICATOR_ANIMATION_DURATION
                interpolator = AccelerateDecelerateInterpolator()
            }

            AnimatorSet().apply {
                playTogether(textColorAnimator, iconColorAnimator, indicatorAnimator)
                start()
            }
        }
    }

    private fun animateToInactive() {
        val typedValue = android.util.TypedValue()

        context.theme.resolveAttribute(R.attr.colorForegroundTertiary, typedValue, true)
        val inactiveColor = ContextCompat.getColor(context, typedValue.resourceId)

        val currentTextColor = binding.tvBottomNavigation.currentTextColor
        val currentIconColor = binding.ivBottomNavigation.imageTintList?.defaultColor ?: currentTextColor

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

        val indicatorAnimator = ObjectAnimator.ofFloat(binding.bottomNavigationIndicator, "scaleX", 1f, 0f).apply {
            duration = INDICATOR_ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                if (animation.animatedFraction == 1f) {
                    binding.bottomNavigationIndicator.visibility = View.GONE
                }
            }
        }

        AnimatorSet().apply {
            playTogether(textColorAnimator, iconColorAnimator, indicatorAnimator)
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