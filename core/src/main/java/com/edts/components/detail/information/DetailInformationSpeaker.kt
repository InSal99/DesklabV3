package com.edts.components.detail.information

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.edts.components.R
import com.edts.components.databinding.DetailInformationSpeakerBinding

class DetailInformationSpeaker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: DetailInformationSpeakerBinding

    init {
        binding = DetailInformationSpeakerBinding.inflate(LayoutInflater.from(context), this, true)
        initAttributes(attrs, defStyleAttr)
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.DetailInformationSpeaker,
            defStyleAttr,
            0
        ).apply {
            try {
                val imageRes = getResourceId(R.styleable.DetailInformationSpeaker_speakerImage, R.drawable.avatar_placeholder)
                setImageResource(imageRes)

                val name = getString(R.styleable.DetailInformationSpeaker_speakerName) ?: ""
                setName(name)

            } finally {
                recycle()
            }
        }
    }

    fun setImageResource(@DrawableRes resId: Int) {
        binding.ivDetailImage.setImageResource(resId)
    }

    fun setImageDrawable(drawable: Drawable?) {
        binding.ivDetailImage.setImageDrawable(drawable)
    }

    fun setName(name: String) {
        binding.tvDetailName.text = name
    }

    fun getName(): String {
        return binding.tvDetailName.text.toString()
    }

    fun getTextView(): TextView {
        return binding.tvDetailName
    }
}