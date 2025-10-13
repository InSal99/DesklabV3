package com.edts.components.bottom.navigation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.edts.components.R
import com.edts.components.databinding.BottomNavigationBinding

class BottomNavigation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BottomNavigationDelegate {
    private val binding: BottomNavigationBinding = BottomNavigationBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    enum class ItemCount(val value: Int) {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        companion object {
            fun fromValue(value: Int): ItemCount {
                return values().find { it.value == value } ?: THREE
            }
        }
    }

    var itemCount: ItemCount = ItemCount.THREE
        set(value) {
            field = value
            updateItemVisibility()
        }

    var delegate: BottomNavigationDelegate? = null

    private val navigationItems: List<BottomNavigationItem> by lazy {
        listOf(
            binding.cvItem1,
            binding.cvItem2,
            binding.cvItem3,
            binding.cvItem4,
            binding.cvItem5
        )
    }

    private var activeItemPosition: Int = 0
        set(value) {
            if (field != value && value in 0 until itemCount.value) {
                val oldValue = field
                field = value
                updateActiveItemState(oldValue, value)
            }
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BottomNavigation,
            0, 0
        ).apply {
            try {
                val itemCountValue = getInt(R.styleable.BottomNavigation_itemCount, 3)
                itemCount = ItemCount.fromValue(itemCountValue)

                setupNavigationItems(this)

                updateItemVisibility()

                ensureActiveItemExists()
            } finally {
                recycle()
            }
        }

        setupNavigationItemDelegates()
    }

    private fun setupNavigationItems(typedArray: android.content.res.TypedArray) {
        navigationItems.forEachIndexed { index, item ->
            item.itemPosition = index

            when (index) {
                0 -> setupItemFromAttributes(item, typedArray, "item1")
                1 -> setupItemFromAttributes(item, typedArray, "item2")
                2 -> setupItemFromAttributes(item, typedArray, "item3")
                3 -> setupItemFromAttributes(item, typedArray, "item4")
                4 -> setupItemFromAttributes(item, typedArray, "item5")
            }
        }
    }

    private fun setupItemFromAttributes(
        item: BottomNavigationItem,
        typedArray: android.content.res.TypedArray,
        itemPrefix: String
    ) {
        val stateAttr = getAttributeIndex("${itemPrefix}State")
        val iconAttr = getAttributeIndex("${itemPrefix}Icon")
        val textAttr = getAttributeIndex("${itemPrefix}Text")
        val badgeAttr = getAttributeIndex("${itemPrefix}ShowBadge")

        if (stateAttr != -1) {
            val stateValue = typedArray.getInt(stateAttr, 1)
            val navState = BottomNavigationItem.NavState.fromValue(stateValue)
            item.navState = navState

            if (navState == BottomNavigationItem.NavState.ACTIVE) {
                activeItemPosition = item.itemPosition
            }
        }

        if (iconAttr != -1) {
            val iconResId = typedArray.getResourceId(iconAttr, -1)
            if (iconResId != -1) {
                item.navIcon = iconResId
            }
        }

        if (textAttr != -1) {
            val text = typedArray.getString(textAttr)
            item.navText = text
        }

        if (badgeAttr != -1) {
            val showBadge = typedArray.getBoolean(badgeAttr, false)
            item.showBadge = showBadge
        }
    }

    private fun getAttributeIndex(attrName: String): Int {
        return when (attrName) {
            "item1State" -> R.styleable.BottomNavigation_item1State
            "item1Icon" -> R.styleable.BottomNavigation_item1Icon
            "item1Text" -> R.styleable.BottomNavigation_item1Text
            "item1ShowBadge" -> R.styleable.BottomNavigation_item1ShowBadge
            "item2State" -> R.styleable.BottomNavigation_item2State
            "item2Icon" -> R.styleable.BottomNavigation_item2Icon
            "item2Text" -> R.styleable.BottomNavigation_item2Text
            "item2ShowBadge" -> R.styleable.BottomNavigation_item2ShowBadge
            "item3State" -> R.styleable.BottomNavigation_item3State
            "item3Icon" -> R.styleable.BottomNavigation_item3Icon
            "item3Text" -> R.styleable.BottomNavigation_item3Text
            "item3ShowBadge" -> R.styleable.BottomNavigation_item3ShowBadge
            "item4State" -> R.styleable.BottomNavigation_item4State
            "item4Icon" -> R.styleable.BottomNavigation_item4Icon
            "item4Text" -> R.styleable.BottomNavigation_item4Text
            "item4ShowBadge" -> R.styleable.BottomNavigation_item4ShowBadge
            "item5State" -> R.styleable.BottomNavigation_item5State
            "item5Icon" -> R.styleable.BottomNavigation_item5Icon
            "item5Text" -> R.styleable.BottomNavigation_item5Text
            "item5ShowBadge" -> R.styleable.BottomNavigation_item5ShowBadge
            else -> -1
        }
    }

    private fun setupNavigationItemDelegates() {
        navigationItems.forEach { item ->
            item.delegate = this
        }
    }

    private fun updateItemVisibility() {
        navigationItems.forEachIndexed { index, item ->
            val shouldBeVisible = index < itemCount.value
            item.visibility = if (shouldBeVisible) View.VISIBLE else View.GONE
        }

        if (activeItemPosition >= itemCount.value) {
            activeItemPosition = 0
        }

        ensureActiveItemExists()
    }

    private fun updateActiveItemState(oldPosition: Int, newPosition: Int) {
        if (oldPosition in 0 until navigationItems.size &&
            oldPosition < itemCount.value) {
            navigationItems[oldPosition].navState = BottomNavigationItem.NavState.INACTIVE
        }

        if (newPosition in 0 until navigationItems.size) {
            navigationItems[newPosition].post {
                navigationItems[newPosition].navState = BottomNavigationItem.NavState.ACTIVE
            }
        }
    }

    private fun ensureActiveItemExists() {
        if (activeItemPosition !in 0 until itemCount.value && itemCount.value > 0) {
            activeItemPosition = 0
        }
    }

    fun getNavigationItem(position: Int): BottomNavigationItem? {
        return if (position in 0 until navigationItems.size && position < itemCount.value) {
            navigationItems[position]
        } else {
            null
        }
    }

    fun setItemState(position: Int, state: BottomNavigationItem.NavState) {
        getNavigationItem(position)?.let { item ->
            if (state == BottomNavigationItem.NavState.ACTIVE) {
                setActiveItem(position)
            } else {
                if (position == activeItemPosition) {
                    return
                }
                item.navState = state
            }
        }
    }

    fun setItemIcon(position: Int, iconResId: Int) {
        getNavigationItem(position)?.let { item ->
            item.navIcon = iconResId
        }
    }

    fun setItemText(position: Int, text: String) {
        getNavigationItem(position)?.let { item ->
            item.navText = text
        }
    }

    fun setItemBadge(position: Int, showBadge: Boolean) {
        getNavigationItem(position)?.let { item ->
            item.showBadge = showBadge
        }
    }

    fun setActiveItem(position: Int) {
        if (position in 0 until itemCount.value) {
            activeItemPosition = position
        }
    }

    fun getActiveItemPosition(): Int {
        return activeItemPosition
    }

    fun resetAllClickCounts() {
        navigationItems.forEach { item ->
            item.resetClickCount()
        }
    }

    fun getTotalClickCount(): Int {
        return navigationItems.sumOf { it.clickCount }
    }

    override fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int,
        clickCount: Int
    ) {
        if (position != activeItemPosition) {
            setActiveItem(position)
        }

        delegate?.onBottomNavigationItemClicked(item, position, clickCount)
    }

    override fun onBottomNavigationItemStateChanged(
        item: BottomNavigationItem,
        newState: BottomNavigationItem.NavState,
        previousState: BottomNavigationItem.NavState
    ) {
        if (item.itemPosition == activeItemPosition && newState == BottomNavigationItem.NavState.INACTIVE) {
            item.navState = BottomNavigationItem.NavState.ACTIVE
            return
        }

        if (newState == BottomNavigationItem.NavState.ACTIVE && item.itemPosition != activeItemPosition) {
            item.navState = BottomNavigationItem.NavState.INACTIVE
            return
        }
        delegate?.onBottomNavigationItemStateChanged(item, newState, previousState)
    }
}