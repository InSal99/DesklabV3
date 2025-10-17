# Sort Button

| Feature / Variation | Preview                                                                                                                                        |
| ------------------- |------------------------------------------------------------------------------------------------------------------------------------------------|
| Default Sort Icon | ![Sort icon button](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759287518/sb_default_sort_icon_a36lp3.gif) |

| **CardState** | **Visual Effect** |
| ------------- | ----------------- |
| **REST** | Default elevated background, scale 1.0 |
| **ON_PRESS** | Elevated background, scale 0.95 |

## Overview

*An interactive sort/filter button component with smooth scale animations and press state feedback. Features customizable icon and delegate pattern for event handling.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.sort.button.SortButton
    android:id="@+id/sortButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:sortIcon="@drawable/ic_sort" />
```

### 2. Initialize in Code

```kotlin
val sortButton = binding.sortButton

// Set up delegate for click handling
sortButton.delegate = object : SortButtonDelegate {
    override fun onSortButtonClick(sortButton: SortButton) {
        // Handle sort button click
        showSortOptions()
    }
}
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `REST` | `0` | Default state with normal scale (1.0) and elevated background | Normal display state |
| `ON_PRESS` | `1` | Pressed state with reduced scale (0.95) | User interaction feedback |

```kotlin
// Card state is managed internally during touch events
// State transitions are animated automatically
```

## Properties Reference

### Visual Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `sortIcon` | `Int` | `R.drawable.ic_sort` | Drawable resource ID for the button icon |
| `cornerRadiusPx` | `Float` | `12dp` | Corner radius in pixels (calculated from dp) |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `SortButtonDelegate?` | `null` | Click event handler interface |
| `cardState` | `CardState` | `REST` | Current visual state (managed internally) |

### Animation Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `scaleX` | `Float` | `1.0f` | Horizontal scale factor (animated) |
| `scaleY` | `Float` | `1.0f` | Vertical scale factor (animated) |

## Data Models

### SortButtonDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onSortButtonClick()` | `sortButton: SortButton` | ✅ | Called when the button is clicked |

```kotlin
val delegate = object : SortButtonDelegate {
    override fun onSortButtonClick(sortButton: SortButton) {
        // Handle click event
        Log.d("MainActivity", "Sort button clicked ${sortButton.getClickCount()} times")
    }
}
```

## Usage Examples

```kotlin
// Basic setup with ViewBinding
binding.sortButton.apply {
    sortIcon = R.drawable.ic_sort
    
    delegate = object : SortButtonDelegate {
        override fun onSortButtonClick(sortButton: SortButton) {
            showSortBottomSheet()
        }
    }
}

// Custom filter icon
binding.filterButton.apply {
    sortIcon = R.drawable.ic_filter
    
    delegate = object : SortButtonDelegate {
        override fun onSortButtonClick(sortButton: SortButton) {
            openFilterDialog()
        }
    }
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Default sort button -->
<com.edts.components.sort.button.SortButton
    android:id="@+id/sortButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:sortIcon="@drawable/ic_sort" />

<!-- Filter button variant -->
<com.edts.components.sort.button.SortButton
    android:id="@+id/filterButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:sortIcon="@drawable/ic_filter"
    android:layout_marginTop="@dimen/margin_24dp" />

<!-- Custom icon button -->
<com.edts.components.sort.button.SortButton
    android:id="@+id/customButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:sortIcon="@drawable/ic_custom_action" />
```

### Programmatic Customization

```kotlin
// Runtime icon updates
sortButton.sortIcon = R.drawable.ic_sort_ascending

// Dynamic delegate assignment
sortButton.delegate = when (currentSortMode) {
    SortMode.ASCENDING -> ascendingDelegate
    SortMode.DESCENDING -> descendingDelegate
    else -> defaultDelegate
}
```

## Performance Considerations

- **ViewBinding** — Uses ViewBinding for efficient view access and type safety
- **Animation Optimization** — Uses ObjectAnimator with hardware acceleration for smooth 60fps animations

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| `Scale Down` | `100ms` | `AccelerateDecelerateInterpolator` | Animates from 1.0 to 0.95 scale on press |
| `Scale Up` | `100ms` | `AccelerateDecelerateInterpolator` | Animates back to 1.0 scale on release |
| `Press State` | Instant | N/A | Background overlay change on touch |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use semantic icons (sort, filter) that match function | Use ambiguous or unrelated icons |
| Implement SortButtonDelegate for all instances | Rely solely on click listeners |
| Test animations on lower-end devices | Assume animations perform uniformly |
| Use consistent button placement in layouts | Randomly position sort buttons |

> **⚠️ Note**: This component uses MaterialCardView as its base and features smooth scale animations using ObjectAnimator.