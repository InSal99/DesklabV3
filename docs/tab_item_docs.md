# Tab Item

| Feature / Variation | Preview |
| ---------------- | ------- |
| Without Badge | ![Label + Active Indicator + Badge] |
| With Badge | ![Label + No Indicator + Badge] |

| **TabState** | **Visual Effect** |
| ------------ | ----------------- |
| **ACTIVE** | Accent color text + visible indicator |
| **INACTIVE** | Tertiary color text + hidden indicator |

## Overview

*A customizable tab component that displays text with optional badge and animated state transitions. Features smooth color and indicator animations when switching between active and inactive states.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.tab.TabItem
    android:id="@+id/tabItem"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:tabItemText="Tab Label"
    app:tabItemBadgeText="3"
    app:tabItemShowBadge="true"
    app:tabItemState="active" />
```

### 2. Initialize in Code

```kotlin
val tabItem = binding.tabItem

// Set up delegate for state change handling
tabItem.delegate = object : TabDelegate {
    override fun onTabClick(
        tabItem: TabItem, 
        newState: TabItem.TabState, 
        previousState: TabItem.TabState
    ) {
        // Handle tab state change
        Log.d("MainActivity", "Tab ${tabItem.tabText} changed from $previousState to $newState")
        handleTabSelection(tabItem)
    }
}
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `ACTIVE` | `0` | Tab is selected with accent color and indicator | Currently selected tab |
| `INACTIVE` | `1` | Tab is unselected with tertiary color | Available but unselected tabs |

```kotlin
// Set tab state programmatically
tabItem.tabState = TabItem.TabState.ACTIVE

// Set state and trigger delegate
tabItem.setTabState(TabItem.TabState.ACTIVE, triggerDelegate = true)
```

## Properties Reference

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `tabText` | `String?` | `"Label"` | Main tab text displayed |
| `badgeText` | `String?` | `null` | Text displayed in the badge indicator |

### State Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `tabState` | `TabState` | `INACTIVE` | Current tab state (ACTIVE/INACTIVE) |
| `showBadge` | `Boolean` | `true` | Controls badge visibility |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `TabDelegate?` | `null` | State change event handler interface |
| `isFirstLaunch` | `Boolean` | `true` | Internal flag for initial state setup |

## Data Models

### TabState Enum

| State | Value | Description |
| ----- | ----- | ----------- |
| `ACTIVE` | `0` | Tab is currently selected |
| `INACTIVE` | `1` | Tab is available but not selected |

```kotlin
val activeState = TabItem.TabState.ACTIVE
val inactiveState = TabItem.TabState.INACTIVE

// Convert from integer value
val stateFromValue = TabItem.TabState.fromValue(0) // Returns ACTIVE
```

### TabDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onTabClick()` | `tabItem: TabItem`, `newState: TabState`, `previousState: TabState` | ✅ | Called when tab state changes |

```kotlin
val delegate = object : TabDelegate {
    override fun onTabClick(
        tabItem: TabItem, 
        newState: TabItem.TabState, 
        previousState: TabItem.TabState
    ) {
        when (newState) {
            TabItem.TabState.ACTIVE -> showTabContent(tabItem)
            TabItem.TabState.INACTIVE -> hideTabContent(tabItem)
        }
    }
}
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setTabState()` | `state: TabState`, `triggerDelegate: Boolean = false` | Sets tab state with optional delegate trigger |
| `resetClickCount()` | None | Resets internal click counter to zero |
| `getClickCount()` | None | Returns current click count for debugging |
| `resetForBinding()` | None | Resets first launch flag for proper binding behavior |
| `performClick()` | None | Programmatically triggers a click event |

## Usage Examples

```kotlin
// Basic setup with delegate
binding.tabItem.apply {
    tabText = "Home"
    badgeText = "5"
    showBadge = true
    tabState = TabItem.TabState.ACTIVE
    
    delegate = object : TabDelegate {
        override fun onTabClick(tabItem: TabItem, newState: TabItem.TabState, previousState: TabItem.TabState) {
            updateSelectedTab(tabItem)
        }
    }
}

// Dynamic badge updates
fun updateNotificationCount(tab: TabItem, count: Int) {
    tab.badgeText = if (count > 0) count.toString() else null
    tab.showBadge = count > 0
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Active tab with badge -->
<com.edts.components.tab.TabItem
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:tabItemText="Label"
    app:tabItemBadgeText="1"
    app:tabItemShowBadge="true"
    app:tabItemState="active" />

<!-- Inactive tab with badge -->
<com.edts.components.tab.TabItem
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:tabItemText="Label"
    app:tabItemBadgeText="1"
    app:tabItemShowBadge="true"
    app:tabItemState="inactive" />

<!-- Tab without badge -->
<com.edts.components.tab.TabItem
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:tabItemText="Label"
    app:tabItemShowBadge="false"
    app:tabItemState="active" />
```

### Programmatic Customization

```kotlin
// Runtime property updates
tabItem.apply {
    tabText = "Updated Label"
    badgeText = "99+"
    showBadge = true
}

// State management with animations
tabItem.tabState = TabItem.TabState.ACTIVE // Triggers smooth animation

// Immediate state change without animation (for initial setup)
tabItem.resetForBinding()
tabItem.tabState = TabItem.TabState.INACTIVE
```

## Performance Considerations

- **Animation Optimization** — Uses AnimatorSet for coordinated text color and indicator animations
- **Click Debouncing** — Built-in 200ms debounce prevents rapid successive clicks
- **Color Resolution Caching** — Efficiently resolves theme colors for state changes
- **ViewBinding** — Uses ViewBinding for type-safe view access

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| `Text Color Transition` | `150ms` | `AccelerateDecelerateInterpolator` | Smooth color change between active/inactive states |
| `Indicator Scale` | `200ms` | `AccelerateDecelerateInterpolator` | Scale X animation for bottom indicator appearance/disappearance |
| `Combined State Change` | `200ms` | `AccelerateDecelerateInterpolator` | Coordinated text and indicator animation |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use TabDelegate for coordinated multi-tab management | Handle tab selection without proper state coordination |
| Keep tab labels short and descriptive | Use long text that may wrap or truncate |
| Update badge text dynamically for notifications | Leave stale badge counts |
| Call resetForBinding() when setting up initial states | Rely on default animation for initial setup |
| Use consistent TabState management across tab groups | Mix manual and delegate-driven state changes |
| Test animations on different devices and API levels | Assume animation performance is uniform |

---

> **⚠️ Note**: This component automatically handles first-launch state setup without animation. For proper behavior when using with data binding or dynamic content, call `resetForBinding()` before setting initial states. The click debounce mechanism logs all interactions for debugging purposes.