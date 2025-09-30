# Selection : Dropdown Filter

| Feature / Variation | Preview                                                                                                                                                                       |
| ------------------- |-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| With Badge & Description | ![Label + Description + Badge(1) + Icon](https://res.cloudinary.com/dacnnk5j4/image/upload/w_200,h_100,c_fit,q_auto,f_auto/v1759226807/sdf_with_badge_description_ngzkgh.gif) |
| With Badge Only | ![Label + Badge(1) + Icon](https://res.cloudinary.com/dacnnk5j4/image/upload/w_200,h_100,c_fit,q_auto,f_auto/v1759226807/sdf_with_badge_only_sso95s.gif)                      |
| Icon Only | ![Label + Icon](https://res.cloudinary.com/dacnnk5j4/image/upload/w_200,h_100,c_fit,q_auto,f_auto/v1759226806/sdf_icon_only_vcdpse.gif)                                                                                                                                                             |

| **CardState** | **Visual Effect** |
| ------------- | ----------------- |
| **REST** | Default elevated background |
| **ON_PRESS** | Pressed overlay effect |

## Overview

*A customizable dropdown filter component that displays a label, optional description, optional badge, and dropdown icon. Features press state animations and click handling with debounce functionality.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.chip.DropdownFilter
    android:id="@+id/dropdownFilter"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dropdownFilterLabel="Filter Label"
    app:dropdownFilterDesc="Filter Description"
    app:dropdownFilterBadgeText="3"
    app:dropdownFilterIcon="@drawable/ic_chevron_down"
    app:dropdownFilterShowBadge="true"
    app:dropdownFilterShowDesc="true" />
```

### 2. Initialize in Code

```kotlin
val dropdownFilter = binding.DropdownFilter

// Set up delegate for click handling
dropdownFilter.delegate = object : DropdownFilterDelegate {
    override fun onDropdownFilterClick(dropdownFilter: DropdownFilter) {
        // Handle dropdown filter click
        Log.d("MainActivity", "Dropdown filter clicked: ${dropdownFilter.dropdownFilterLabel}")
    }
}
```

## Display Modes 

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `REST` | `0` | Default state with elevated background | Normal display state |
| `ON_PRESS` | `1` | Pressed state with overlay effect | User interaction feedback |

```kotlin
// Component state is managed internally during touch events
// No manual state setting required
```

## Properties Reference

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `dropdownFilterLabel` | `String?` | `null` | Main label text displayed prominently |
| `dropdownFilterDesc` | `String?` | `null` | Secondary description text |
| `dropdownFilterBadgeText` | `String?` | `null` | Text displayed in the badge indicator |

### Visual Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `dropdownFilterIcon` | `Int?` | `null` | Drawable resource ID for the dropdown icon |
| `dropdownFilterShowBadge` | `Boolean` | `true` | Controls badge visibility |
| `dropdownFilterShowDesc` | `Boolean` | `true` | Controls description text visibility |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `DropdownFilterDelegate?` | `null` | Click event handler interface |
| `cardState` | `CardState` | `REST` | Current visual state (managed internally) |

## Data Models 

### DropdownFilterDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onDropdownFilterClick()` | `dropdownFilter: DropdownFilter` | ✅ | Called when the filter is clicked |

```kotlin
val delegate = object : DropdownFilterDelegate {
    override fun onDropdownFilterClick(dropdownFilter: DropdownFilter) {
        // Handle click event
        showDropdownMenu()
    }
}
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `resetClickCount()` | None | Resets internal click counter to zero |
| `getClickCount()` | None | Returns current click count for debugging |
| `performClick()` | None | Programmatically triggers a click event |

## Usage Examples

```kotlin
// Basic setup with all features
binding.dropdownFilter.apply {
    dropdownFilterLabel = "Category"
    dropdownFilterDesc = "Select a category"
    dropdownFilterBadgeText = "5"
    dropdownFilterIcon = R.drawable.ic_chevron_down
    dropdownFilterShowBadge = true
    dropdownFilterShowDesc = true
    
    delegate = object : DropdownFilterDelegate {
        override fun onDropdownFilterClick(dropdownFilter: DropdownFilter) {
            showCategorySelector()
        }
    }
}

// Minimal setup (icon only)
binding.simpleFilter.apply {
    dropdownFilterLabel = "Sort"
    dropdownFilterIcon = R.drawable.ic_sort
    dropdownFilterShowBadge = false
    dropdownFilterShowDesc = false
}

// Dynamic badge updates
fun updateFilterCount(count: Int) {
    binding.dropdownFilter.dropdownFilterBadgeText = count.toString()
    binding.dropdownFilter.dropdownFilterShowBadge = count > 0
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Full featured dropdown filter -->
<com.edts.components.chip.DropdownFilter
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dropdownFilterBadgeText="1"
    app:dropdownFilterDesc="Description"
    app:dropdownFilterIcon="@drawable/ic_chevron_down"
    app:dropdownFilterLabel="Label"
    app:dropdownFilterShowBadge="true"
    app:dropdownFilterShowDesc="true" />

<!-- Badge only variant -->
<com.edts.components.chip.DropdownFilter
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dropdownFilterBadgeText="1"
    app:dropdownFilterIcon="@drawable/ic_chevron_down"
    app:dropdownFilterLabel="Label"
    app:dropdownFilterShowBadge="true"
    app:dropdownFilterShowDesc="false" />

<!-- Minimal variant -->
<com.edts.components.chip.DropdownFilter
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dropdownFilterIcon="@drawable/ic_chevron_down"
    app:dropdownFilterLabel="Label"
    app:dropdownFilterShowBadge="false"
    app:dropdownFilterShowDesc="false" />
```

### Programmatic Customization

```kotlin
// Runtime property updates
dropdownFilter.apply {
    dropdownFilterLabel = "Updated Label"
    dropdownFilterDesc = "New description text"
    dropdownFilterBadgeText = "99+"
    dropdownFilterIcon = R.drawable.ic_new_icon
}

// Toggle visibility states
dropdownFilter.dropdownFilterShowBadge = hasActiveFilters
dropdownFilter.dropdownFilterShowDesc = hasDesc
```

## Performance Considerations

- **Color Caching** — Resolves and caches theme colors to avoid repeated attribute lookups during state changes
- **Click Debouncing** — Built-in 300ms debounce prevents accidental double-clicks and improves user experience
- **ViewBinding** — Uses ViewBinding for efficient view access and type safety

## Animation Details

| Animation Type | Duration | Description |
| -------------- | -------- | ----------- |
| `Press State` | Instant | Background color change on touch down/up |
| `Foreground Overlay` | Instant | Overlay drawable for pressed state feedback |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Keep labels concise and descriptive | Use overly long label text that wraps |
| Use consistent icons across similar filters | Mix different icon styles in the same interface |
| Implement DropdownFilterDelegate for all instances | Forget to set up click handling |
| Update badge text dynamically to reflect current state | Leave stale badge counts |

---

> **⚠️ Note**: This component uses MaterialCardView as its base and inherits elevation and corner radius properties. The click debounce mechanism prevents rapid successive clicks but logs all interaction attempts for debugging purposes.
