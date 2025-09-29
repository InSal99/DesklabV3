# Tab

| Feature / Variation | Preview |
| ------------------- | ------- |
| With Badges | ![Home(3) + Profile(!) + Settings] |
| Mixed Badges | ![Active Tab + Badge Tab + Plain Tab] |
| Badge-free | ![Tab1 + Tab2 + Tab3] |

| **TabState** | **Visual Effect** |
| ------------ | ----------------- |
| **ACTIVE** | Selected state with active styling |
| **INACTIVE** | Default unselected state |

## Overview

*A horizontal scrollable tab component built on RecyclerView that supports tab selection, badges, and both XML and programmatic configuration. Features automatic item spacing and smooth selection handling.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.tab.Tab
    android:id="@+id/tabContainer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:initialSelectedPosition="0">

    <com.edts.components.tab.TabItem
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tabItemText="Home"
        app:tabItemBadgeText="3"
        app:tabItemShowBadge="true"
        app:tabItemState="active" />

    <com.edts.components.tab.TabItem
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tabItemText="Profile"
        app:tabItemBadgeText="!"
        app:tabItemShowBadge="true"
        app:tabItemState="inactive" />

    <com.edts.components.tab.TabItem
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tabItemText="Settings"
        app:tabItemShowBadge="false"
        app:tabItemState="inactive" />
</com.edts.components.tab.Tab>
```

### 2. Initialize in Code

```kotlin
val tabContainer = binding.tabContainer

// Set up click listener
tabContainer.setOnTabClickListener(object : Tab.OnTabClickListener {
    override fun onTabClick(position: Int, tabText: String) {
        // Handle tab selection
        Log.d("Tab", "Selected: $tabText at position $position")
    }
})
```

## Display Modes 

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `ACTIVE` | `TabItem.TabState.ACTIVE` | Selected/active tab state | Currently selected tab |
| `INACTIVE` | `TabItem.TabState.INACTIVE` | Unselected/inactive tab state | Available but not selected tabs |

```kotlin
// State is managed automatically on selection
// Manual state setting handled internally by the component
```

## Properties Reference

### Container Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `initialSelectedPosition` | `Int` | `0` | Starting selected tab position |
| `selectedPosition` | `Int` | `0` | Currently selected tab index |
| `onTabClickListener` | `OnTabClickListener?` | `null` | Click event handler interface |

### Tab Data Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `text` | `String` | Required | Main tab label text |
| `badgeText` | `String?` | `null` | Optional badge text content |
| `showBadge` | `Boolean` | `true` | Controls badge visibility |
| `state` | `TabItem.TabState` | `INACTIVE` | Current tab state |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `clipToPadding` | `Boolean` | `false` | Allows content to draw outside padding |
| `layoutManager` | `LinearLayoutManager` | Horizontal | Manages tab layout and scrolling |

## Data Models

### TabData

| Property | Type | Required | Description |
| -------- | ---- | -------- | ----------- |
| `text` | `String` | ✅ | Tab label text |
| `badgeText` | `String?` | ❌ | Badge content (numbers, symbols, text) |
| `showBadge` | `Boolean` | ❌ | Badge visibility flag |
| `state` | `TabItem.TabState` | ❌ | Tab selection state |

```kotlin
val tabData = TabData(
    text = "Notifications",
    badgeText = "5",
    showBadge = true,
    state = TabItem.TabState.INACTIVE
)
```

### OnTabClickListener Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onTabClick()` | `position: Int, tabText: String` | ✅ | Called when a tab is selected |

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setTabs()` | `tabs: List<TabData>, selected: Int = 0` | Sets tab data programmatically |
| `setSelectedPosition()` | `position: Int, notifyListener: Boolean = true` | Changes selected tab |
| `setOnTabClickListener()` | `listener: OnTabClickListener?` | Sets click event handler |
| `getSelectedPosition()` | None | Returns currently selected position |
| `getSelectedTabText()` | None | Returns selected tab's text |
| `getTabCount()` | None | Returns total number of tabs |
| `isPositionSelected()` | `position: Int` | Checks if position is selected |

## Usage Examples

```kotlin
// Programmatic tab setup
val tabs = listOf(
    TabData("Home", "3", true, TabItem.TabState.ACTIVE),
    TabData("Messages", "!", true, TabItem.TabState.INACTIVE),
    TabData("Profile", null, false, TabItem.TabState.INACTIVE)
)

binding.tabContainer.apply {
    setTabs(tabs, selectedPosition = 0)
    
    setOnTabClickListener(object : Tab.OnTabClickListener {
        override fun onTabClick(position: Int, tabText: String) {
            when(position) {
                0 -> showHomeFragment()
                1 -> showMessagesFragment()
                2 -> showProfileFragment()
            }
        }
    })
}

// Dynamic tab updates
fun updateNotificationBadge(count: Int) {
    val currentTabs = getCurrentTabs() // Your method to get current tabs
    val updatedTabs = currentTabs.mapIndexed { index, tab ->
        if (index == 1) { // Messages tab
            tab.copy(badgeText = count.toString(), showBadge = count > 0)
        } else tab
    }
    binding.tabContainer.setTabs(updatedTabs, binding.tabContainer.getSelectedPosition())
}

// Selection management
fun selectTabProgrammatically(position: Int) {
    if (position < binding.tabContainer.getTabCount()) {
        binding.tabContainer.setSelectedPosition(position)
    }
}
```

## Customization Examples

### XML Configuration

```xml
<!-- Basic tabs with mixed badge states -->
<com.edts.components.tab.Tab
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:initialSelectedPosition="1">

    <com.edts.components.tab.TabItem
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tabItemText="Home"
        app:tabItemBadgeText="3"
        app:tabItemShowBadge="true"
        app:tabItemState="inactive" />

    <com.edts.components.tab.TabItem
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tabItemText="Profile"
        app:tabItemBadgeText="!"
        app:tabItemShowBadge="true"
        app:tabItemState="active" />

    <com.edts.components.tab.TabItem
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tabItemText="Settings"
        app:tabItemShowBadge="false"
        app:tabItemState="inactive" />
</com.edts.components.tab.Tab>
```

### Programmatic Setup

```kotlin
// ViewBinding approach
binding.tabContainer.apply {
    val dynamicTabs = listOf(
        TabData("Dashboard", null, false),
        TabData("Analytics", "NEW", true),
        TabData("Reports", "2", true),
        TabData("Settings", null, false)
    )
    
    setTabs(dynamicTabs, selected = 0)
    
    setOnTabClickListener(object : Tab.OnTabClickListener {
        override fun onTabClick(position: Int, tabText: String) {
            handleTabSelection(position, tabText)
        }
    })
}
```

## Performance Considerations

- **RecyclerView Base** — Efficient view recycling for large tab sets with smooth horizontal scrolling
- **ViewHolder Pattern** — Optimized TabAdapter implementation reduces view inflation overhead
- **Automatic Item Decoration** — Built-in TabSpaceItemDecoration provides consistent 8dp spacing between tabs
- **State Management** — Efficient position tracking and state updates without full adapter refreshes

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use concise tab labels (1-2 words) | Create tabs with long text that may wrap |
| Keep badge text short (numbers, symbols) | Use lengthy badge text that affects layout |
| Handle tab clicks to update content areas | Leave tabs without corresponding functionality |
| Use badges to indicate notifications or count | Overuse badges for decoration purposes |
| Test horizontal scrolling with many tabs | Assume all tabs will fit on screen |
| Set initialSelectedPosition for default selection | Leave selection state undefined |

---

> **⚠️ Note**: This component extends RecyclerView and automatically handles XML child TabItem elements by converting them to TabData objects. The adapter manages ViewHolder recycling and state updates efficiently. Tab spacing is handled by the built-in TabSpaceItemDecoration with 8dp gaps between items.