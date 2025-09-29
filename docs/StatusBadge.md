# Status Badge

## Overview

StatusBadge is a custom Android view component that extends `AppCompatTextView` to display status indicators with predefined styles. It combines text, icons, and color-coded backgrounds to represent different states (Approved, Decline, Waiting, Cancel) with a consistent, badge-like appearance.

| Feature / Variation | Preview |
| ------------------- | ------- |
| **APPROVED** | Green background with success icon |
| **DECLINE** | Red/Orange background with error icon |
| **WAITING** | Blue background with alarm icon |
| **CANCEL** | Gray background with close icon |

---

## Basic Usage

### 1. Add to Layout

```xml
<your.edts.components.status.badge.StatusBadge
    android:id="@+id/statusBadge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:statusBadgeType="APPROVED"
    app:statusBadgeText="Approved" />
```

### 2. Initialize in Code

```kotlin
val statusBadge = findViewById<StatusBadge>(R.id.statusBadge)
statusBadge.chipType = StatusBadge.ChipType.APPROVED
statusBadge.text = "Approved"
```

## Display Modes (ChipType)

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `APPROVED` | `0` | Green badge with success icon | Display approved/successful status |
| `DECLINE` | `1` | Red/Orange badge with error icon | Display declined/rejected status |
| `WAITING` | `2` | Blue badge with alarm icon | Display pending/waiting status |
| `CANCEL` | `3` | Gray badge with close icon | Display cancelled/inactive status |

```kotlin
statusBadge.chipType = StatusBadge.ChipType.APPROVED
```

## Properties Reference

### Color Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `chipType.backgroundColorAttr` | `@AttrRes` | Varies by type | Background color attribute from theme |
| `chipType.textColorAttr` | `@AttrRes` | Varies by type | Text and icon color attribute from theme |

**Color Attributes by ChipType:**
- **APPROVED**: `colorBackgroundSuccessSubtle` (background), `colorForegroundSuccessIntense` (text/icon)
- **DECLINE**: `colorBackgroundAttentionSubtle` (background), `colorForegroundAttentionIntense` (text/icon)
- **WAITING**: `colorBackgroundInfoSubtle` (background), `colorForegroundInfoIntense` (text/icon)
- **CANCEL**: `colorBackgroundTertiary` (background), `colorForegroundSecondary` (text/icon)

### Dimension Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `compoundDrawablePadding` | `Int` | `4dp` | Space between icon and text |
| `horizontalPadding` | `Int` | `12dp` | Left and right padding |
| `verticalPadding` | `Int` | `8dp` | Top and bottom padding |

### Text Appearance Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `textAppearance` | `@StyleRes` | `TextMedium_Label3` | Text style for badge label |

### Icon Properties

| Property Name | Type | Icon Resource | Description |
| ------------- | ---- | ------------- | ----------- |
| `chipType.iconRes` | `@DrawableRes` | Varies by type | Left-aligned icon for the badge |

**Icons by ChipType:**
- **APPROVED**: `ic_success`
- **DECLINE**: `ic_error`
- **WAITING**: `ic_alarm`
- **CANCEL**: `ic_close`


## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setChipType()` | `chipType: ChipType` | Updates the badge type and applies corresponding style |
| `setText()` | `text: CharSequence` | Sets the badge text (inherited from TextView) |

## Usage Examples

```kotlin
// Example 1: Setting status programmatically
val statusBadge = findViewById<StatusBadge>(R.id.statusBadge)
statusBadge.chipType = StatusBadge.ChipType.APPROVED
statusBadge.text = "Approved"

// Example 2: Changing status dynamically
when (requestStatus) {
    "approved" -> {
        statusBadge.chipType = StatusBadge.ChipType.APPROVED
        statusBadge.text = "Approved"
    }
    "declined" -> {
        statusBadge.chipType = StatusBadge.ChipType.DECLINE
        statusBadge.text = "Declined"
    }
    "pending" -> {
        statusBadge.chipType = StatusBadge.ChipType.WAITING
        statusBadge.text = "Waiting"
    }
    "cancelled" -> {
        statusBadge.chipType = StatusBadge.ChipType.CANCEL
        statusBadge.text = "Cancelled"
    }
}

// Example 3: Using in RecyclerView
class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val statusBadge: StatusBadge = itemView.findViewById(R.id.statusBadge)
    
    fun bind(status: Status) {
        statusBadge.chipType = status.type
        statusBadge.text = status.label
    }
}
```

## Performance Considerations

- **View Recycling**: Extends `AppCompatTextView`, making it efficient for use in RecyclerViews
- **Drawable Mutation**: Uses `mutate()` on drawables to prevent shared state issues across multiple instances
- **Attribute Caching**: Resolves theme attributes once during style application
- **Memory Efficiency**: Reuses background drawable with color updates instead of creating new instances

## Implementation Details

### Background Drawable

The component uses `bg_status_badge` drawable (GradientDrawable) with:
- Dynamic background color based on ChipType
- 1dp stroke border with `colorStrokeInteractive`
- Rounded corners (defined in drawable resource)

### Icon Configuration

Icons are:
- Positioned to the left of text
- Tinted to match text color
- Automatically sized using intrinsic bounds
- Spaced 4dp from text

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Keep badge text concise (1-2 words) | Use long descriptive sentences |
| Use appropriate ChipType for status context | Mix up status types inconsistently |
| Define theme colors for consistent branding | Hardcode colors in layouts |
| Test with different text lengths | Assume single-line text fits all cases |
| Use in RecyclerView for list items | Create new instances unnecessarily |
| Ensure bg_status_badge drawable exists | Rely on default background behavior |

## Required Resources

### Drawables
- `bg_status_badge`: Background drawable (GradientDrawable with rounded corners)
- `ic_success`: Success/approved icon
- `ic_error`: Error/declined icon
- `ic_alarm`: Alarm/waiting icon
- `ic_close`: Close/cancel icon

### Dimensions
- `margin_4dp`: Icon-text spacing
- `margin_8dp`: Vertical padding
- `margin_12dp`: Horizontal padding
- `stroke_weight_1dp`: Border stroke width

### Styles
- `TextMedium_Label3`: Text appearance style

### Theme Attributes
- Color attributes listed in Color Properties section above

---

> **⚠️ Note**: Ensure all required drawable resources, dimensions, and theme attributes are defined in your project. Missing resources will cause runtime exceptions.