# Bottom Navigation Item

| Feature / Variation | Preview                                                                                                                                     |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| With Badge          | ![Label + Icon (Active Color) + Indicator + Badge](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287066/bni_with_badge_aouhky.gif) |
| Without Badge       | ![Label + Icon (Inactive Color) + No Indicator](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287065/bni_without_badge_ig2xiy.gif)                                                                                           |

| **NavState** | **Visual Effect** |
| ------------ | ----------------- |
| **ACTIVE** | Accent colors + visible indicator with scale animation |
| **INACTIVE** | Tertiary colors + hidden indicator |

## Overview

*A customizable bottom navigation item component that supports active/inactive states, smooth color transitions, bounce animations, badge indicators, and comprehensive click tracking. Features state-based visual feedback and delegate-based event handling.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.bottom.navigation.BottomNavigationItem
    android:id="@+id/bottomNavItem"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    app:navState="active"
    app:navIcon="@drawable/ic_home"
    app:navText="Home"
    app:navShowBadge="true" />
```

### 2. Initialize in Code

```kotlin
val bottomNavItem = binding.bottomNavItem

// Set up delegate for event handling
bottomNavItem.delegate = object : BottomNavigationDelegate {
    override fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int,
        clickCount: Int
    ) {
        // Handle item click
        Log.d("MainActivity", "Item clicked: ${item.navText}, position: $position")
    }
    
    override fun onBottomNavigationItemStateChanged(
        item: BottomNavigationItem,
        newState: BottomNavigationItem.NavState,
        oldState: BottomNavigationItem.NavState
    ) {
        // Handle state change
        Log.d("MainActivity", "State changed from $oldState to $newState")
    }
}

// Set position for identification
bottomNavItem.itemPosition = 0
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `ACTIVE` | `0` | Selected state with accent colors and indicator | Current/selected navigation item |
| `INACTIVE` | `1` | Default state with tertiary colors | Unselected navigation items |

```kotlin
bottomNavItem.navState = BottomNavigationItem.NavState.ACTIVE
```

## Properties Reference

### State Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `navState` | `NavState` | `INACTIVE` | Current visual state (ACTIVE/INACTIVE) |
| `itemPosition` | `Int` | `-1` | Position identifier for delegate callbacks |
| `clickCount` | `Int` | `0` | Read-only click counter (private setter) |

### Content Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `navIcon` | `Int?` | `null` | Drawable resource ID for navigation icon |
| `navText` | `String?` | `null` | Label text displayed below icon |
| `showBadge` | `Boolean` | `false` | Controls badge indicator visibility |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `BottomNavigationDelegate?` | `null` | Event handler interface for clicks and state changes |

## Data Models

### BottomNavigationDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onBottomNavigationItemClicked()` | `item: BottomNavigationItem`, `position: Int`, `clickCount: Int` | ✅ | Called when item is clicked |
| `onBottomNavigationItemStateChanged()` | `item: BottomNavigationItem`, `newState: NavState`, `oldState: NavState` | ✅ | Called when nav state changes |

```kotlin
val delegate = object : BottomNavigationDelegate {
    override fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int,
        clickCount: Int
    ) {
        // Handle navigation
        navigateToScreen(position)
    }
    
    override fun onBottomNavigationItemStateChanged(
        item: BottomNavigationItem,
        newState: BottomNavigationItem.NavState,
        oldState: BottomNavigationItem.NavState
    ) {
        // Handle state transition
        updateAnalytics(item.navText, newState)
    }
}
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `performClick()` | `fromUser: Boolean = false` | Programmatically triggers click with optional user flag |
| `resetClickCount()` | None | Resets click counter to zero with logging |

## Usage Examples

```kotlin
// Basic navigation setup
binding.homeNavItem.apply {
    navIcon = R.drawable.ic_home
    navText = "Home"
    navState = BottomNavigationItem.NavState.ACTIVE
    itemPosition = 0
    showBadge = false
    
    delegate = navigationDelegate
}

// With badge indicator
binding.notificationsNavItem.apply {
    navIcon = R.drawable.ic_notifications
    navText = "Notifications"
    navState = BottomNavigationItem.NavState.INACTIVE
    itemPosition = 3
    showBadge = hasUnreadNotifications
}

// Programmatic state management
fun selectNavigationItem(position: Int) {
    navigationItems.forEachIndexed { index, item ->
        item.navState = if (index == position) {
            BottomNavigationItem.NavState.ACTIVE
        } else {
            BottomNavigationItem.NavState.INACTIVE
        }
    }
}

// Click tracking
fun handleNavigation(item: BottomNavigationItem) {
    Log.d("Navigation", "Item ${item.navText} clicked ${item.clickCount} times")
    // Perform navigation logic
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Active navigation item -->
<com.edts.components.bottom.navigation.BottomNavigationItem
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    app:navState="active"
    app:navIcon="@drawable/ic_home"
    app:navText="Home"
    app:navShowBadge="true" />

<!-- Inactive navigation item -->
<com.edts.components.bottom.navigation.BottomNavigationItem
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    app:navState="inactive"
    app:navIcon="@drawable/ic_profile"
    app:navText="Profile"
    app:navShowBadge="false" />
```

### Dynamic State Management

```kotlin
// Bottom navigation controller
class BottomNavigationController(private val items: List<BottomNavigationItem>) {
    
    fun selectItem(position: Int) {
        items.forEachIndexed { index, item ->
            item.navState = if (index == position) {
                BottomNavigationItem.NavState.ACTIVE
            } else {
                BottomNavigationItem.NavState.INACTIVE
            }
        }
    }
    
    fun updateBadge(position: Int, show: Boolean) {
        if (position in items.indices) {
            items[position].showBadge = show
        }
    }
}

// Usage in activity/fragment
val controller = BottomNavigationController(listOf(
    binding.homeNavItem,
    binding.searchNavItem,
    binding.favoritesNavItem,
    binding.profileNavItem
))

controller.selectItem(0) // Select home
controller.updateBadge(2, true) // Show badge on favorites
```

## Performance Considerations

- **Color Animation Caching** — Uses ValueAnimator for smooth color transitions without repeated color resolution
- **Animation Coordination** — AnimatorSet efficiently manages multiple simultaneous property animations
- **Click Tracking** — Lightweight click counting with debug logging for analytics integration
- **State Management** — Optimized state changes with conditional animation triggers

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| `State Transition` | `150ms` | `AccelerateDecelerateInterpolator` | Smooth color transition between active/inactive states |
| `Indicator Scale` | `200ms` | `AccelerateDecelerateInterpolator` | Scale in/out animation for bottom indicator |
| `Icon Bounce` | `600ms` | `OvershootInterpolator` | Bounce feedback on click with 1.15x scale factor |
| `Color Transition` | `150ms` | `AccelerateDecelerateInterpolator` | Text and icon color fade between states |

```kotlin
// Animation constants
private companion object {
    const val ANIMATION_DURATION = 150L
    const val BOUNCE_ANIMATION_DURATION = 600L
    const val INDICATOR_ANIMATION_DURATION = 200L
    const val BOUNCE_SCALE_FACTOR = 1.15f
    const val BOUNCE_OVERSHOOT_TENSION = 1.2f
}
```

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Set itemPosition for proper delegate callbacks | Leave itemPosition as default -1 in production |
| Use consistent icon styles across navigation items | Mix different icon design systems |
| Implement both delegate methods for complete event handling | Only implement one delegate method |
| Keep navigation labels short and descriptive | Use lengthy text that may wrap or truncate |
| Reset click counts periodically for analytics accuracy | Let click counts accumulate indefinitely |
| Test animations on different device performance levels | Assume animations perform uniformly |
| Use badge sparingly for important notifications only | Show badges on multiple items simultaneously |

---

> **⚠️ Note**: This component includes comprehensive animation support with bounce feedback, smooth state transitions, and color animations. The delegate pattern allows for centralized navigation management while maintaining individual item state. Click tracking and logging are built-in for analytics integration.