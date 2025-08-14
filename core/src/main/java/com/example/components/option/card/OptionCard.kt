package com.example.components.option.card

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.components.R
import com.example.components.databinding.OptionCardBinding
import com.google.android.material.card.MaterialCardView

class OptionCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = OptionCardBinding.inflate(LayoutInflater.from(context), this, true)

    var delegate: OptionCardDelegate? = null

    var titleText: CharSequence? = null
        set(value) {
            field = value
            binding.tvOptionTitle.text = value
        }

    var iconDrawable: Drawable? = null
        set(value) {
            field = value
            binding.ivOptionAction.setImageDrawable(value)
        }

    var iconResource: Int? = null
        set(value) {
            field = value
            value?.let { binding.ivOptionAction.setImageResource(it) }
        }

    private val shadowPaint1 = Paint()
    private val shadowPaint2 = Paint()

    init {
        initAttrs(attrs)
        setupShadowPaints()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.OptionCard).use {
                titleText = it.getString(R.styleable.OptionCard_cardText)
                iconDrawable = it.getDrawable(R.styleable.OptionCard_cardIcon)
            }
        }
    }

    private fun setupShadowPaints() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        shadowPaint1.apply {
            setShadowLayer(
                4f,
                0f,
                2f,
                context.theme.obtainStyledAttributes(intArrayOf(R.attr.colorShadowNeutralAmbient))
                    .getColor(0, Color.TRANSPARENT)
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        shadowPaint2.apply {
            setShadowLayer(
                0f,
                0f,
                2f,
                context.theme.obtainStyledAttributes(intArrayOf(R.attr.colorShadowNeutralKey))
                    .getColor(0, Color.TRANSPARENT)
            )
            color = Color.TRANSPARENT
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private fun bindClickActions() {
        binding.root.setOnClickListener {
            delegate?.onCardClick(it)
        }
    }
}
