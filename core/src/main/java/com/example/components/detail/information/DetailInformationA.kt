package com.example.components.detail.information

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.components.R
import com.example.components.databinding.DetailInformationABinding

class DetailInformationA @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = DetailInformationABinding.inflate(LayoutInflater.from(context), this, true)

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

    init {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DetailInformationA, 0, 0)

        try {
            icon = typedArray.getDrawable(R.styleable.DetailInformationA_infoIcon)
            title = typedArray.getString(R.styleable.DetailInformationA_infoTitle)
            description = typedArray.getString(R.styleable.DetailInformationA_infoDescription)
        } finally {
            typedArray.recycle()
        }
    }
}