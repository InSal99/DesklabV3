package com.edts.components.bottom.navigation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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
            } else if (value !in 0 until itemCount.value) {
                Log.w(TAG, "Invalid active item position: $value. Keeping current active item: $field")
            }
        }

    private companion object {
        const val TAG = "BottomNavigation"
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
        Log.d(TAG, "Updating item visibility for count: ${itemCount.value}")

        navigationItems.forEachIndexed { index, item ->
            val shouldBeVisible = index < itemCount.value
            item.visibility = if (shouldBeVisible) View.VISIBLE else View.GONE

            Log.d(TAG, "Item $index visibility: ${if (shouldBeVisible) "VISIBLE" else "GONE"}")
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

        Log.d(TAG, "Active item changed from $oldPosition to $newPosition")
    }

    private fun ensureActiveItemExists() {
        if (activeItemPosition !in 0 until itemCount.value && itemCount.value > 0) {
            activeItemPosition = 0
            Log.d(TAG, "No active item found, setting first item as active")
        }
    }

    fun getNavigationItem(position: Int): BottomNavigationItem? {
        return if (position in 0 until navigationItems.size && position < itemCount.value) {
            navigationItems[position]
        } else {
            Log.w(TAG, "Invalid position: $position")
            null
        }
    }

    fun setItemState(position: Int, state: BottomNavigationItem.NavState) {
        getNavigationItem(position)?.let { item ->
            if (state == BottomNavigationItem.NavState.ACTIVE) {
                setActiveItem(position)
            } else {
                if (position == activeItemPosition) {
                    Log.w(TAG, "Cannot set active item to inactive. Use setActiveItem() to change active item.")
                    return
                }
                item.navState = state
                Log.d(TAG, "Set item $position state to $state")
            }
        }
    }

    fun setItemIcon(position: Int, iconResId: Int) {
        getNavigationItem(position)?.let { item ->
            item.navIcon = iconResId
            Log.d(TAG, "Set item $position icon to $iconResId")
        }
    }

    fun setItemText(position: Int, text: String) {
        getNavigationItem(position)?.let { item ->
            item.navText = text
            Log.d(TAG, "Set item $position text to $text")
        }
    }

    fun setItemBadge(position: Int, showBadge: Boolean) {
        getNavigationItem(position)?.let { item ->
            item.showBadge = showBadge
            Log.d(TAG, "Set item $position badge visibility to $showBadge")
        }
    }

    fun setActiveItem(position: Int) {
        if (position in 0 until itemCount.value) {
            activeItemPosition = position
            Log.d(TAG, "Set active item to position: $position")
        } else {
            Log.w(TAG, "Invalid active item position: $position")
        }
    }

    fun getActiveItemPosition(): Int {
        return activeItemPosition
    }

    fun resetAllClickCounts() {
        navigationItems.forEach { item ->
            item.resetClickCount()
        }
        Log.d(TAG, "Reset all click counts")
    }

    fun getTotalClickCount(): Int {
        return navigationItems.sumOf { it.clickCount }
    }

    override fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int,
        clickCount: Int
    ) {
        Log.d(TAG, "Navigation item clicked:")
        Log.d(TAG, "  - Position: $position")
        Log.d(TAG, "  - Text: ${item.navText ?: "Unknown"}")
        Log.d(TAG, "  - Click Count: $clickCount")
        Log.d(TAG, "  - Total Clicks: ${getTotalClickCount()}")

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
        Log.d(TAG, "Navigation item state changed:")
        Log.d(TAG, "  - Position: ${item.itemPosition}")
        Log.d(TAG, "  - Text: ${item.navText ?: "Unknown"}")
        Log.d(TAG, "  - Previous State: $previousState")
        Log.d(TAG, "  - New State: $newState")

        if (item.itemPosition == activeItemPosition && newState == BottomNavigationItem.NavState.INACTIVE) {
            Log.w(TAG, "Cannot deactivate active item ${item.itemPosition}. Reverting to ACTIVE.")
            item.navState = BottomNavigationItem.NavState.ACTIVE
            return
        }

        if (newState == BottomNavigationItem.NavState.ACTIVE && item.itemPosition != activeItemPosition) {
            Log.w(TAG, "Attempt to manually activate item ${item.itemPosition}, but active item is $activeItemPosition. Reverting.")
            item.navState = BottomNavigationItem.NavState.INACTIVE
            return
        }

        delegate?.onBottomNavigationItemStateChanged(item, newState, previousState)
    }
}