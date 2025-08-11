package com.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RadioGroup
import com.google.android.material.radiobutton.MaterialRadioButton

class CustomRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    private var onItemSelectedListener: ((position: Int, data: Any?) -> Unit)? = null
    private val radioButtonDataMap = mutableMapOf<Int, Any?>()

    fun <T> setData(dataList: List<T>, itemDisplayProvider: (T) -> String) {
        removeAllViews()
        radioButtonDataMap.clear()

        dataList.forEachIndexed { index, item ->
            val radioButton = CustomRadioButton(context).apply {
                id = View.generateViewId()
                text = itemDisplayProvider(item)
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16) // Add some margin between items
                }
            }

            radioButtonDataMap[radioButton.id] = item
            addView(radioButton)
        }

        setOnCheckedChangeListener { _, checkedId ->
            val position = getPositionById(checkedId)
            val data = radioButtonDataMap[checkedId]
            onItemSelectedListener?.invoke(position, data)
        }
    }

    fun setOnItemSelectedListener(listener: (position: Int, data: Any?) -> Unit) {
        this.onItemSelectedListener = listener
    }

    fun getSelectedData(): Any? {
        return radioButtonDataMap[checkedRadioButtonId]
    }

    fun getSelectedPosition(): Int {
        return getPositionById(checkedRadioButtonId)
    }

    fun selectItem(position: Int) {
        if (position >= 0 && position < childCount) {
            val radioButton = getChildAt(position) as? CustomRadioButton
            radioButton?.isChecked = true
        }
    }

    fun selectItemByData(data: Any) {
        val entry = radioButtonDataMap.entries.find { it.value == data }
        entry?.let { check(it.key) }
    }

    private fun getPositionById(id: Int): Int {
        for (i in 0 until childCount) {
            if (getChildAt(i).id == id) return i
        }
        return -1
    }
}