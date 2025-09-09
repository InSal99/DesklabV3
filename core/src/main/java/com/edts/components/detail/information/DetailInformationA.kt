package com.edts.components.detail.information

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.edts.components.R
import com.edts.components.databinding.DetailInformationABinding

class DetailInformationA @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
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

    init {
        initAttrs(context, attrs)
        setupClickListeners()
        if (attrs == null) {
            hasAction = false
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            hasAction = false
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailInformationA, 0, 0)

        try {
            icon = typedArray.getDrawable(R.styleable.DetailInformationA_infoIcon)
            title = typedArray.getString(R.styleable.DetailInformationA_infoTitle)
            description = typedArray.getString(R.styleable.DetailInformationA_infoDescription)
            hasAction = typedArray.getBoolean(R.styleable.DetailInformationA_hasAction, false)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupClickListeners() {
        binding.btnDetailAction1.setOnClickListener {
            delegate?.onAction1Clicked(this)
        }

        binding.btnDetailAction2.setOnClickListener {
            delegate?.onAction2Clicked(this)
        }
    }

    private fun updateDescriptionConstraints() {
        val layoutParams = binding.tvDetailDescription.layoutParams as LayoutParams

        if (hasAction) {
            layoutParams.bottomToTop = R.id.btnDetailAction2
            layoutParams.bottomToBottom = LayoutParams.UNSET
            layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_4dp)
        } else {
            layoutParams.bottomToTop = LayoutParams.UNSET
            layoutParams.bottomToBottom = LayoutParams.PARENT_ID
            layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_12dp)
        }

        binding.tvDetailDescription.layoutParams = layoutParams
    }
}