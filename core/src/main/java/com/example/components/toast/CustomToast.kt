package com.example.components.toast

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.components.R

class CustomToast @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var toastType: Type = Type.GENERAL
    private var toastMessage: String = ""
    private val shadowPaint1 = Paint()
    private val shadowPaint2 = Paint()

    enum class Type(@DrawableRes val iconRes: Int, @AttrRes val colorAttr: Int) {
        SUCCESS(R.drawable.placeholder, R.attr.colorBackgroundSuccessIntense),
        ERROR(R.drawable.placeholder, R.attr.colorBackgroundAttentionIntense),
        INFO(R.drawable.placeholder, R.attr.colorBackgroundInfoIntense),
        GENERAL(R.drawable.placeholder, R.attr.colorBackgroundPrimary)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_toast_view, this, true)

        if (attrs != null) {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomToast,
                0, 0
            ).apply {
                try {
                    val typeIndex = getInt(R.styleable.CustomToast_toastType, 3)
                    toastType = Type.values()[typeIndex]
                    toastMessage = getString(R.styleable.CustomToast_toastMessage) ?: ""
                } catch (e: Exception) {
                    e.printStackTrace()
                    toastType = Type.GENERAL
                    toastMessage = ""
                } finally {
                    recycle()
                }
            }
        }

        applyToastStyle()
        setupShadowPaints()
    }

    private fun applyToastStyle() {
        val drawable = ContextCompat.getDrawable(context, R.drawable.bg_toast)?.mutate() as? GradientDrawable
        drawable?.apply {
            setColor(resolveColorAttr(toastType.colorAttr))
        }
        background = drawable

        findViewById<AppCompatImageView>(R.id.iv_icon).setImageResource(toastType.iconRes)
        findViewById<AppCompatTextView>(R.id.tv_message).text = toastMessage
    }

    private fun resolveColorAttr(@AttrRes attrRes: Int): Int {
        val typedValue = TypedValue()
        if (!context.theme.resolveAttribute(attrRes, typedValue, true)) {
            throw Resources.NotFoundException("Attribute $attrRes not found in theme!")
        }
        return typedValue.data
    }

    private fun setupShadowPaints() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        shadowPaint1.apply {
            setShadowLayer(
                4f,
                0f,
                2f,
                R.attr.colorShadowNeutralAmbient
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        shadowPaint2.apply {
            setShadowLayer(
                4f,
                0f,
                0f,
                R.attr.colorShadowNeutralKey
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    fun setToast(type: Type, message: String) {
        toastType = type
        toastMessage = message
        applyToastStyle()
    }

//    fun showToast() {
//        startAnimation()
//
//        android.widget.Toast(context).apply {
//            setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 100)
//            duration = android.widget.Toast.LENGTH_LONG
//            view = this@CustomToast
//            show()
//        }
//    }

//    fun startAnimation() {
//        val slideIn = TranslateAnimation(
//            Animation.RELATIVE_TO_SELF, 0f,
//            Animation.RELATIVE_TO_SELF, 0f,
//            Animation.RELATIVE_TO_SELF, 20f,
//            Animation.RELATIVE_TO_SELF, 0f
//        ).apply {
//            duration = 300
//            interpolator = DecelerateInterpolator()
//        }
//
//        startAnimation(slideIn)
//    }

//    fun showToast() {
//        post {
//            val slideIn = TranslateAnimation(0f, 100f, height.toFloat(), 0f).apply {
//                duration = 300
//                interpolator = DecelerateInterpolator()
//            }
//
//            val slideOut = TranslateAnimation(100f, 0f, 0f, height.toFloat()).apply {
//                duration = 300
//                interpolator = AccelerateInterpolator()
//                startOffset = 2000 // Start after 2 seconds
//            }
//
//            val set = AnimationSet(true).apply {
//                addAnimation(slideIn)
//                addAnimation(slideOut)
//            }
//
//            val toast = android.widget.Toast(context).apply {
//                setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 100)
//                duration = android.widget.Toast.LENGTH_LONG
//                view = this@CustomToast
//            }
//
//            set.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationStart(animation: Animation?) = toast.show()
//                override fun onAnimationRepeat(animation: Animation?) {}
//                override fun onAnimationEnd(animation: Animation?) {}
//            })
//
//            startAnimation(set)
//        }
//    }

    fun showToast() {
        try {
            Log.d("CustomToast", "showToast called for type: $toastType, message: $toastMessage")

            // Measure the view first
            measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            val viewHeight = measuredHeight.toFloat()
            Log.d("CustomToast", "Measured height: $viewHeight")

            if (viewHeight <= 0) {
                // Fallback if measurement fails
                Log.w("CustomToast", "Height is 0, using simple toast")
                android.widget.Toast.makeText(context, toastMessage, android.widget.Toast.LENGTH_LONG).show()
                return
            }

            visibility = View.VISIBLE
            translationY = viewHeight // Start below screen

//            val slideIn = TranslateAnimation(0f, 100f, height.toFloat(), 0f).apply {
//                duration = 300
//                interpolator = DecelerateInterpolator()
//            }
//
//            val slideOut = TranslateAnimation(100f, 0f, 0f, height.toFloat()).apply {
//                duration = 300
//                interpolator = AccelerateInterpolator()
//                startOffset = 2000 // Start after 2 seconds
//            }
//
//            val set = AnimationSet(true).apply {
//                addAnimation(slideIn)
//                addAnimation(slideOut)
//            }

            val animator = ValueAnimator.ofFloat(viewHeight, 0f).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    translationY = it.animatedValue as Float
                }
            }

            val toast = android.widget.Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 100)
                duration = android.widget.Toast.LENGTH_LONG
                view = this@CustomToast
            }

            animator.start()
            toast.show()

        } catch (e: Exception) {
            Log.e("CustomToast", "Error showing toast", e)
            android.widget.Toast.makeText(context, toastMessage, android.widget.Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun success(context: Context, message: String) {
            val toast = CustomToast(context)
            toast.setToast(Type.SUCCESS, message)
            toast.showToast()
        }

        fun error(context: Context, message: String) {
            val toast = CustomToast(context)
            toast.setToast(Type.ERROR, message)
            toast.showToast()
        }

        fun info(context: Context, message: String) {
            val toast = CustomToast(context)
            toast.setToast(Type.INFO, message)
            toast.showToast()
        }

        fun general(context: Context, message: String) {
            val toast = CustomToast(context)
            toast.setToast(Type.GENERAL, message)
            toast.showToast()
        }
    }
}