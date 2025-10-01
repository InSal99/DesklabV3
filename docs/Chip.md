# Selection : Chip

| Feature / Variation     | Preview                                                                                                                                  |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| Small with Badge & Icon | ![Chip Label + Badge(3) + Close Icon](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759227714/sc_small_with_badge_icon_tleoib.gif) |
| Medium with Icon Only   | ![Chip Label + Close Icon](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759227715/sc_medium_with_icon_only_tgsxd6.gif)            |
| Text Only               | ![Chip Label](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759227714/sc_text_only_zhinm8.gif)                                     |
| Custom Active Color     | ![Chip Label with custom active color](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759227714/sc_custom_active_color_wudlql.gif)                                                                                                 |

| **ChipState** | **Visual Effect** |
| ------------- | ----------------- |
| **ACTIVE** | Inverted colors with animated transitions |
| **INACTIVE** | Default appearance with subtle styling |

## Overview

*A highly customizable selection chip component with smooth state animations, optional badge and icon support, and comprehensive click handling. Features automatic state toggling, custom background colors, and separate icon click handling.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.chip.Chip
    android:id="@+id/chip"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:chipText="Filter Option"
    app:chipState="inactive"
    app:chipSize="small"
    app:chipBadgeText="5"
    app:chipIcon="@drawable/ic_close"
    app:chipShowIcon="true"
    app:chipShowBadge="true" />
```

### 2. Initialize in Code

```kotlin
val chip = binding.Chip

// Set up delegate for click handling
chip.delegate = object : ChipDelegate {
    override fun onChipClick(chip: Chip, newState: Chip.ChipState) {
        Log.d("MainActivity", "Chip clicked: ${chip.chipText}, new state: $newState")
    }
    
    override fun onChipIconClick(chip: Chip) {
        Log.d("MainActivity", "Chip icon clicked: ${chip.chipText}")
    }
}
```

## Display Modes 

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `ACTIVE` | `0` | Selected state with inverted colors | User has selected this filter/option |
| `INACTIVE` | `1` | Default unselected state | Available but not currently selected |

```kotlin
// Component state is managed internally during touch events
// No manual state setting required
```

## Properties Reference

### State Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `chipState` | `ChipState` | `INACTIVE` | Current selection state (ACTIVE/INACTIVE) |
| `chipSize` | `ChipSize` | `SMALL` | Size variant (SMALL/MEDIUM) |

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `chipText` | `String?` | `null` | Main text displayed on the chip |
| `chipBadgeText` | `String?` | `null` | Text shown in the badge indicator |

### Visual Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `chipIcon` | `Int?` | `null` | Drawable resource ID for the chip icon |
| `chipShowIcon` | `Boolean` | `true` | Controls icon visibility |
| `chipShowBadge` | `Boolean` | `true` | Controls badge visibility |
| `customActiveBackgroundColor` | `Int?` | `null` | Custom background color for active state |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `ChipDelegate?` | `null` | Click event handler interface |
| `pressState` | `PressState` | `REST` | Current touch interaction state |

## Data Models 

### ChipDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onChipClick()` | `chip: Chip, newState: ChipState` | ✅ | Called when chip body is clicked |
| `onChipIconClick()` | `chip: Chip` | ✅ | Called when chip icon is clicked separately |

```kotlin
val chipDelegate = object : ChipDelegate {
    override fun onChipClick(chip: Chip, newState: Chip.ChipState) {
        when (newState) {
            Chip.ChipState.ACTIVE -> applyFilter(chip.chipText)
            Chip.ChipState.INACTIVE -> removeFilter(chip.chipText)
        }
    }
    
    override fun onChipIconClick(chip: Chip) {
        // Handle icon-specific action (e.g., remove filter)
        removeChipCompletely(chip)
    }
}
```

### ChipState Enum

| State | Value | Description |
| ----- | ----- | ----------- |
| `ACTIVE` | `0` | Selected/enabled state |
| `INACTIVE` | `1` | Unselected/default state |

### ChipSize Enum

| Size | Value | Description |
| ---- | ----- | ----------- |
| `SMALL` | `0` | Compact size (8dp horizontal, 4dp vertical padding) |
| `MEDIUM` | `1` | Larger size (10dp horizontal, 6dp vertical padding) |

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setChipState()` | `newState: ChipState, fromUser: Boolean = false` | Sets chip state with optional user interaction logging |
| `setActiveBackgroundColor()` | `color: Int` | Sets custom background color for active state |
| `setIconClickable()` | `clickable: Boolean` | Enables/disables icon click functionality |
| `resetClickCount()` | None | Resets internal click counter |
| `resetIconClickCount()` | None | Resets icon click counter |
| `getClickCount()` | None | Returns current click count |
| `getIconClickCount()` | None | Returns current icon click count |

## Usage Examples

```kotlin
// Basic filter chip setup
binding.filterChip.apply {
    chipText = "Category"
    chipBadgeText = "12"
    chipIcon = R.drawable.ic_close
    chipState = Chip.ChipState.INACTIVE
    chipSize = Chip.ChipSize.SMALL
    
    delegate = object : ChipDelegate {
        override fun onChipClick(chip: Chip, newState: Chip.ChipState) {
            updateFilterResults(chip.chipText, newState == Chip.ChipState.ACTIVE)
        }
        
        override fun onChipIconClick(chip: Chip) {
            removeFilter(chip.chipText)
        }
    }
}

// Programmatic state changes
fun selectAllFilters() {
    filterChips.forEach { chip ->
        chip.setChipState(Chip.ChipState.ACTIVE, fromUser = false)
    }
}

// Dynamic badge updates
fun updateBadgeCount(chip: Chip, count: Int) {
    chip.chipBadgeText = count.toString()
    chip.chipShowBadge = count > 0
}

// Custom styling
binding.premiumChip.apply {
    setActiveBackgroundColor(ContextCompat.getColor(context, R.color.premium_gold))
    chipText = "Premium"
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Small chip with badge and icon -->
<com.edts.components.chip.Chip
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:chipState="inactive"
    app:chipText="Chip Label"
    app:chipSize="small"
    app:chipBadgeText="3"
    app:chipShowIcon="true"
    app:chipShowBadge="true"
    app:chipIcon="@drawable/ic_close" />

<!-- Medium chip with icon only -->
<com.edts.components.chip.Chip
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:chipState="inactive"
    app:chipSize="medium"
    app:chipText="Chip Label"
    app:chipShowIcon="true"
    app:chipShowBadge="false"
    app:chipIcon="@drawable/ic_close" />

<!-- Text only chip -->
<com.edts.components.chip.Chip
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:chipState="inactive"
    app:chipText="Chip Label"
    app:chipShowIcon="false"
    app:chipShowBadge="false" />

<!-- Custom background color -->
<com.edts.components.chip.Chip
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:chipState="inactive"
    app:chipText="Chip Label"
    app:chipShowIcon="false"
    app:chipShowBadge="false"
    app:chipActiveBackgroundColor="?attr/colorBackgroundUtilityCutiIntense" />
```

### Programmatic Customization

```kotlin
// Runtime property updates
chip.apply {
    chipText = "Updated Filter"
    chipBadgeText = "99+"
    chipIcon = R.drawable.ic_new_filter
    chipShowIcon = hasRemoveOption
    chipShowBadge = badgeCount > 0
}

// State management
fun toggleChipSelection(chip: Chip) {
    val newState = when (chip.chipState) {
        Chip.ChipState.ACTIVE -> Chip.ChipState.INACTIVE
        Chip.ChipState.INACTIVE -> Chip.ChipState.ACTIVE
    }
    chip.setChipState(newState, fromUser = true)
}

// Icon click handling
chip.setIconClickable(isRemovable)
```

## Performance Considerations

- **Animation Management** — Cancels all running animations before starting new ones to prevent conflicts and ensure smooth transitions
- **Color Caching** — Caches resolved theme colors to avoid repeated attribute lookups during animations
- **Click Debouncing** — Built-in 300ms debounce for both chip and icon clicks prevents rapid-fire interactions
- **State Animation Optimization** — Checks if already animating to target state to prevent unnecessary animation restarts

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| `Background Color` | `200ms` | `ArgbEvaluator` | Smooth color transition between states |
| `Stroke Color` | `200ms` | `ArgbEvaluator` | Border color animation |
| `Text Color` | `200ms` | `ArgbEvaluator` | Text color inversion animation |
| `Icon Tint` | `200ms` | `ArgbEvaluator` | Icon color transition |
| `Badge Stroke` | `200ms` | `ArgbEvaluator` | Badge border animation |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Keep chip text concise (1-2 words) | Use long text that causes wrapping |
| Use consistent icon styles across related chips | Mix different icon families in the same group |
| Implement both onChipClick and onChipIconClick for full functionality | Ignore icon-specific interactions |
| Update badge text dynamically to reflect current counts | Leave stale badge numbers |
| Use debounced clicks for better UX | Handle rapid successive clicks manually |
| Cancel animations when appropriate (onDetachedFromWindow) | Let animations run indefinitely |

---

> **⚠️ Note**: This component features advanced state management with automatic toggling on click. The animation system uses 5 concurrent ValueAnimators with completion tracking to ensure smooth visual transitions. Icon clicks are handled separately from chip body clicks, allowing for different interaction patterns (e.g., icon for removal, body for selection).