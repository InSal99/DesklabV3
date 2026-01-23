package com.edts.components.detail.information

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.DetailInformationABinding
import com.edts.components.utils.dpToPx
import com.edts.components.utils.resolveColorAttribute
import com.google.android.material.card.MaterialCardView

class DetailInformationA @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding = DetailInformationABinding.inflate(LayoutInflater.from(context), this, true)

    var delegate: DetailInformationADelegate? = null

    var icon: Drawable?
        get() = binding.ivInfoIcon.drawable
        set(value) {
            binding.ivInfoIcon.setImageDrawable(value)
            binding.ivInfoIcon.isVisible = value != null
        }

    var title: CharSequence?
        get() = binding.tvInfoTitle.text
        set(value) {
            binding.tvInfoTitle.text = value
            binding.tvInfoTitle.isVisible = !value.isNullOrEmpty()
        }

    var description: CharSequence?
        get() = binding.tvDetailDescription.text
        set(value) {
            binding.tvDetailDescription.text = value
            binding.tvDetailDescription.isVisible = !value.isNullOrEmpty()
        }

    var hasAction: Boolean = false
        set(value) {
            field = value
            binding.btnDetailAction1.isVisible = value
            binding.btnDetailAction2.isVisible = value
            updateDescriptionConstraints()
        }

    val actionButton1: com.edts.components.button.Button
        get() = binding.btnDetailAction1

    val actionButton2: com.edts.components.button.Button
        get() = binding.btnDetailAction2

    var hasDescIcon: Boolean = false
        set(value) {
            field = value
            binding.ivDescIcon.isVisible = value
            updateRootClickability()
            updateDescriptionEndConstraint()
        }

    var descIcon: Drawable?
        get() = binding.ivDescIcon.drawable
        set(value) {
            if (value != null) {
                binding.ivDescIcon.setImageDrawable(value)
            }
            binding.ivDescIcon.isVisible = hasDescIcon
        }

    var urlInfo: String? = null
        set(value) {
            field = value
            updateRootClickability()
        }

    init {
        initAttrs(context, attrs)
        setupClickListeners()
        updateRootClickability()
        if (attrs == null) {
            hasAction = false
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            hasAction = false
            hasDescIcon = false
            return
        }

        rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.kitColorNeutralGrayDarkA5))
        elevation = 0f
        strokeWidth = 0
        setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailInformationA, 0, 0)

        try {
            icon = typedArray.getDrawable(R.styleable.DetailInformationA_infoIcon)
            title = typedArray.getString(R.styleable.DetailInformationA_infoTitle)
            description = typedArray.getString(R.styleable.DetailInformationA_infoDescription)
            hasAction = typedArray.getBoolean(R.styleable.DetailInformationA_hasAction, false)
            hasDescIcon = typedArray.getBoolean(R.styleable.DetailInformationA_hasDescIcon, false)
            val descDrawable = typedArray.getDrawable(R.styleable.DetailInformationA_descIcon)
            descIcon = descDrawable
            urlInfo = typedArray.getString(R.styleable.DetailInformationA_urlInfo)
        } finally {
            typedArray.recycle()
        }
    }

    private fun updateRootClickability() {
        isClickable = hasDescIcon || !urlInfo.isNullOrEmpty()
        isFocusable = isClickable
    }

    private fun updateDescriptionEndConstraint() {
        val layoutParams =
            binding.tvDetailDescription.layoutParams as ConstraintLayout.LayoutParams

        if (hasDescIcon) {
            layoutParams.endToStart = R.id.ivDescIcon
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET

            val rippleColor = ColorStateList.valueOf(context.resolveColorAttribute(R.attr.colorBackgroundModifierOnPress, R.color.kitColorNeutralGrayDarkA5))
            val rippleDrawable = RippleDrawable(rippleColor, null, null)
            rippleDrawable.radius = (16f.dpToPx).toInt()

            binding.ivDescIcon.background = rippleDrawable
        } else {
            layoutParams.endToStart = ConstraintLayout.LayoutParams.UNSET
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }

        binding.tvDetailDescription.layoutParams = layoutParams
    }

    private fun setupClickListeners() {
        binding.btnDetailAction1.setOnClickListener {
            delegate?.onAction1Clicked(this)
        }

        binding.btnDetailAction2.setOnClickListener {
            delegate?.onAction2Clicked(this)
        }

        binding.ivDescIcon.setOnClickListener {
            delegate?.onDescIconClick(this)
        }

        setOnClickListener {
            urlInfo?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            }
        }
    }

    private fun updateDescriptionConstraints() {
        val layoutParams =
            binding.tvDetailDescription.layoutParams as ConstraintLayout.LayoutParams

        if (hasAction) {
            layoutParams.bottomToTop = R.id.btnDetailAction2
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            layoutParams.bottomMargin =
                resources.getDimensionPixelSize(R.dimen.margin_4dp)
        } else {
            layoutParams.bottomToTop = ConstraintLayout.LayoutParams.UNSET
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.bottomMargin =
                resources.getDimensionPixelSize(R.dimen.margin_12dp)
        }

        binding.tvDetailDescription.layoutParams = layoutParams
    }
}