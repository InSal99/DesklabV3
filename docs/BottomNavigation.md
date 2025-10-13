# Bottom Navigation

| Feature / Variation | Preview                                                                                                            |
| ------------------- |--------------------------------------------------------------------------------------------------------------------|
| One Item | ![Single navigation item](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286976/bn_one_item_lhdm0e.gif)    |
| Two Items | ![Two navigation items](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286976/bn_two_items_c2pplp.gif)                                                                                          |
| Three Items | ![Three navigation items](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286977/bn_three_items_pxyoga.gif) |
| Four Items | ![Four navigation items](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286977/bn_four_items_ghoggn.gif)   |
| Five Items | ![Five navigation items](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286977/bn_five_items_hxddnt.gif)   |

| **NavState** | **Visual Effect** |
| ------------ | ----------------- |
| **ACTIVE** | Highlighted selected state |
| **INACTIVE** | Default unselected state |

## Overview

*A customizable bottom navigation component that supports 1-5 navigation items with icons, text labels, badges, and state management. Features automatic active item tracking and comprehensive click handling.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.bottom.navigation.BottomNavigation
    android:id="@+id/bottomNavigation"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:itemCount="four"
    app:item1State="active"
    app:item1Icon="@drawable/ic_home"
    app:item1Text="Home"
    app:item1ShowBadge="false"
    app:item2State="inactive"
    app:item2Icon="@drawable/ic_search"
    app:item2Text="Search"
    app:item2ShowBadge="true"
    app:item3State="inactive"
    app:item3Icon="@drawable/ic_favorites"
    app:item3Text="Favorites"
    app:item3ShowBadge="false"
    app:item4State="inactive"
    app:item4Icon="@drawable/ic_profile"
    app:item4Text="Profile"
    app:item4ShowBadge="false" />
```

### 2. Initialize in Code

```kotlin
val bottomNavigation = binding.bottomNavigation

// Set up delegate for navigation events
bottomNavigation.delegate = object : BottomNavigationDelegate {
    override fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int,
        clickCount: Int
    ) {
        // Handle navigation item click
        when (position) {
            0 -> navigateToHome()
            1 -> navigateToSearch()
            2 -> navigateToFavorites()
            3 -> navigateToProfile()
        }
    }
    
    override fun onBottomNavigationItemStateChanged(
        item: BottomNavigationItem,
        newState: BottomNavigationItem.NavState,
        previousState: BottomNavigationItem.NavState
    ) {
        // Handle state changes
        Log.d("Navigation", "Item ${item.navText} changed from $previousState to $newState")
    }
}
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `ONE` | `1` | Single navigation item | Simple back/forward navigation |
| `TWO` | `2` | Two navigation items | Toggle between main views |
| `THREE` | `3` | Three navigation items | Standard tab navigation |
| `FOUR` | `4` | Four navigation items | Extended navigation options |
| `FIVE` | `5` | Five navigation items | Maximum navigation items |

```kotlin
bottomNavigation.itemCount = BottomNavigation.ItemCount.FOUR
```

## Properties Reference

### Navigation Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `itemCount` | `ItemCount` | `THREE` | Number of visible navigation items |
| `delegate` | `BottomNavigationDelegate?` | `null` | Event handler interface |

### Individual Item Properties (Per Item)

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `item[N]State` | `NavState` | `INACTIVE` | Visual state of the navigation item |
| `item[N]Icon` | `@DrawableRes` | `null` | Icon resource for the navigation item |
| `item[N]Text` | `String` | `null` | Text label for the navigation item |
| `item[N]ShowBadge` | `Boolean` | `false` | Controls badge visibility |

*Note: [N] represents item numbers 1-5*

## Data Models

### BottomNavigationDelegate Interface

| Method | Parameters | Description |
| ------ | ---------- | ----------- |
| `onBottomNavigationItemClicked()` | `item: BottomNavigationItem, position: Int, clickCount: Int` | Called when navigation item is clicked |
| `onBottomNavigationItemStateChanged()` | `item: BottomNavigationItem, newState: NavState, previousState: NavState` | Called when item state changes |

```kotlin
val delegate = object : BottomNavigationDelegate {
    override fun onBottomNavigationItemClicked(
        item: BottomNavigationItem,
        position: Int,
        clickCount: Int
    ) {
        handleNavigation(position)
    }
    
    override fun onBottomNavigationItemStateChanged(
        item: BottomNavigationItem,
        newState: BottomNavigationItem.NavState,
        previousState: BottomNavigationItem.NavState
    ) {
        updateUI(item, newState)
    }
}
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `getNavigationItem()` | `position: Int` | Returns navigation item at specified position |
| `setItemState()` | `position: Int, state: NavState` | Sets the state of a navigation item |
| `setItemIcon()` | `position: Int, iconResId: Int` | Updates icon for specified item |
| `setItemText()` | `position: Int, text: String` | Updates text label for specified item |
| `setItemBadge()` | `position: Int, showBadge: Boolean` | Controls badge visibility for specified item |
| `setActiveItem()` | `position: Int` | Sets the active navigation item |
| `getActiveItemPosition()` | None | Returns current active item position |
| `resetAllClickCounts()` | None | Resets click counters for all items |
| `getTotalClickCount()` | None | Returns total clicks across all items |

## Usage Examples

```kotlin
// Basic setup with ViewBinding
binding.bottomNavigation.apply {
    itemCount = BottomNavigation.ItemCount.FOUR
    
    delegate = object : BottomNavigationDelegate {
        override fun onBottomNavigationItemClicked(
            item: BottomNavigationItem,
            position: Int,
            clickCount: Int
        ) {
            when (position) {
                0 -> showHomeFragment()
                1 -> showSearchFragment()
                2 -> showFavoritesFragment()
                3 -> showProfileFragment()
            }
        }
        
        override fun onBottomNavigationItemStateChanged(
            item: BottomNavigationItem,
            newState: BottomNavigationItem.NavState,
            previousState: BottomNavigationItem.NavState
        ) {
            // Handle state change animations or logging
        }
    }
}

// Dynamic updates
fun updateNavigationBadges(notifications: Map<Int, Boolean>) {
    notifications.forEach { (position, showBadge) ->
        binding.bottomNavigation.setItemBadge(position, showBadge)
    }
}

// Programmatic navigation
fun navigateToProfile() {
    binding.bottomNavigation.setActiveItem(3)
}

// Update item properties
binding.bottomNavigation.apply {
    setItemText(0, "Dashboard")
    setItemIcon(1, R.drawable.ic_new_search)
    setItemBadge(2, true)
}
```

## Customization Examples

### XML Configuration

```xml
<!-- Five item navigation with badges -->
<com.edts.components.bottom.navigation.BottomNavigation
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:itemCount="five"
    app:item1State="active"
    app:item1Icon="@drawable/ic_home"
    app:item1Text="Home"
    app:item1ShowBadge="false"
    app:item2State="inactive"
    app:item2Icon="@drawable/ic_search"
    app:item2Text="Search"
    app:item2ShowBadge="true"
    app:item3State="inactive"
    app:item3Icon="@drawable/ic_camera"
    app:item3Text="Camera"
    app:item3ShowBadge="false"
    app:item4State="inactive"
    app:item4Icon="@drawable/ic_notifications"
    app:item4Text="Notifications"
    app:item4ShowBadge="true"
    app:item5State="inactive"
    app:item5Icon="@drawable/ic_profile"
    app:item5Text="Profile"
    app:item5ShowBadge="false" />

<!-- Minimal two item navigation -->
<com.edts.components.bottom.navigation.BottomNavigation
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:itemCount="two"
    app:item1State="active"
    app:item1Icon="@drawable/ic_list"
    app:item1Text="List"
    app:item2State="inactive"
    app:item2Icon="@drawable/ic_grid"
    app:item2Text="Grid" />
```

### Runtime Customization

```kotlin
// Dynamically change item count
binding.bottomNavigation.itemCount = BottomNavigation.ItemCount.THREE

// Bulk update navigation items
val navigationItems = listOf(
    Triple(R.drawable.ic_dashboard, "Dashboard", false),
    Triple(R.drawable.ic_analytics, "Analytics", true),
    Triple(R.drawable.ic_settings, "Settings", false)
)

navigationItems.forEachIndexed { index, (icon, text, badge) ->
    binding.bottomNavigation.apply {
        setItemIcon(index, icon)
        setItemText(index, text)
        setItemBadge(index, badge)
    }
}

// Handle navigation with fragment transactions
private fun handleNavigation(position: Int) {
    val fragment = when (position) {
        0 -> HomeFragment()
        1 -> SearchFragment()
        2 -> FavoritesFragment()
        3 -> ProfileFragment()
        else -> HomeFragment()
    }
    
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, fragment)
        .commit()
}
```

## Performance Considerations

- **Lazy Item Initialization** — Navigation items are created lazily to optimize startup performance
- **State Validation** — Prevents invalid state transitions and maintains consistent active item tracking
- **Click Debouncing** — Built-in click handling prevents rapid successive navigation events
- **ViewBinding** — Uses ViewBinding for efficient view access and type safety

## Animation Details

| Animation Type | Duration | Description |
| -------------- | -------- | ----------- |
| `State Change` | Instant | Immediate visual feedback for active/inactive states |
| `Item Visibility` | Instant | Items show/hide based on itemCount changes |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Keep navigation labels short and descriptive | Use long text that might wrap or truncate |
| Use consistent icon styles across all items | Mix different icon design languages |
| Set one item as active initially | Leave all items inactive |
| Handle both click and state change events | Ignore state change notifications |
| Use badges sparingly for important notifications | Overuse badges for every minor update |

---

> **⚠️ Note**: This component automatically manages active item states and prevents invalid state transitions. The component ensures only one item can be active at a time and maintains consistency across state changes.