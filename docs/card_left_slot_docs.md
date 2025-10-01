# Card: Left Slot

| Feature / Variation | Preview                                                                                                                       |
| ------------------- |-------------------------------------------------------------------------------------------------------------------------------|
| Image Type | ![Avatar/Image fills full container](https://res.cloudinary.com/dacnnk5j4/image/upload/w_200,h_100,c_fit,q_auto,f_auto/v1759286743/cls_image_type_ngrzy6.png) |
| Icon Type | ![20dp icon centered with background](https://res.cloudinary.com/dacnnk5j4/image/upload/w_200,h_100,c_fit,q_auto,f_auto/v1759286743/cls_icon_type_seo2us.png)                                                                                       |

| **SlotType** | **Value** |
| ------------ | --------- |
| **IMAGE** | `0` |
| **ICON** | `1` |

## Overview

*A flexible card slot component that can display either full-size images or smaller icons with customizable background colors and tints. Automatically adjusts sizing and layout based on the slot type.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.card.multi.detail.CardLeftSlot
    android:id="@+id/cardLeftSlot"
    android:layout_width="44dp"
    android:layout_height="44dp"
    app:slotType="image"
    app:slotSrc="@drawable/avatar_placeholder" />
```

### 2. Initialize in Code

```kotlin
val cardLeftSlot = binding.cardLeftSlot

// Configure slot programmatically
cardLeftSlot.apply {
    slotType = CardLeftSlot.SlotType.ICON
    slotSrc = R.drawable.ic_star
    slotTint = ContextCompat.getColor(context, R.color.warning_color)
    slotBackgroundColor = ContextCompat.getColor(context, R.color.warning_background)
}
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `IMAGE` | `0` | Full container size, no background/tint | Profile pictures, thumbnails, media previews |
| `ICON` | `1` | 20dp centered icon with background support | Status indicators, action icons, categories |

```kotlin
cardLeftSlot.slotType = CardLeftSlot.SlotType.IMAGE
// or
cardLeftSlot.slotType = CardLeftSlot.SlotType.ICON
```

## Properties Reference

### Core Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `slotType` | `SlotType` | `IMAGE` | Determines sizing and layout behavior |
| `slotSrc` | `Int?` | `null` | Drawable resource ID for the image/icon |

### Styling Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `slotBackgroundColor` | `Int?` | `null` | Background color resource or direct color value |
| `slotTint` | `Int?` | `null` | Icon tint color resource or direct color value |

## Data Models

### SlotType Enum

| Value | Type | Description |
| ----- | ---- | ----------- |
| `IMAGE` | `Int = 0` | Full-size image mode with match_parent dimensions |
| `ICON` | `Int = 1` | Small icon mode with 20dp fixed dimensions |

## Usage Examples

```kotlin
// Image slot for avatars
binding.avatarSlot.apply {
    slotType = CardLeftSlot.SlotType.IMAGE
    slotSrc = R.drawable.user_avatar
    // No background color or tint needed for images
}

// Icon slot with themed colors
binding.statusSlot.apply {
    slotType = CardLeftSlot.SlotType.ICON
    slotSrc = R.drawable.ic_check_circle
    slotTint = R.attr.colorForegroundSuccess
    slotBackgroundColor = R.attr.colorBackgroundSuccess
}

// Dynamic slot type switching
fun updateSlotContent(isIcon: Boolean) {
    binding.cardLeftSlot.apply {
        slotType = if (isIcon) CardLeftSlot.SlotType.ICON else CardLeftSlot.SlotType.IMAGE
        slotSrc = if (isIcon) R.drawable.ic_placeholder else R.drawable.image_placeholder
        
        if (isIcon) {
            slotBackgroundColor = R.color.icon_background
            slotTint = R.color.icon_tint
        } else {
            slotBackgroundColor = null
            slotTint = null
        }
    }
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Image type slot -->
<com.edts.components.card.multi.detail.CardLeftSlot
    android:layout_width="44dp"
    android:layout_height="44dp"
    android:layout_marginTop="@dimen/margin_24dp"
    app:slotType="image"
    app:slotSrc="@drawable/avatar_placeholder" />

<!-- Icon type slot with theming -->
<com.edts.components.card.multi.detail.CardLeftSlot
    android:id="@+id/cvCardLeftSlot"
    android:layout_width="44dp"
    android:layout_height="44dp"
    android:layout_marginTop="@dimen/margin_16dp"
    app:slotType="icon"
    app:slotSrc="@drawable/ic_star"
    app:slotTint="?attr/colorForegroundWarningIntense"
    app:slotBackgroundColor="?attr/colorForegroundWarningSubtle" />

<!-- Large icon slot -->
<com.edts.components.card.multi.detail.CardLeftSlot
    android:layout_width="56dp"
    android:layout_height="56dp"
    app:slotType="icon"
    app:slotSrc="@drawable/ic_notification"
    app:slotTint="@color/primary_color"
    app:slotBackgroundColor="@color/primary_background" />
```

### Programmatic Customization

```kotlin
// Theme-aware color application
cardLeftSlot.apply {
    slotType = CardLeftSlot.SlotType.ICON
    slotSrc = R.drawable.ic_warning
    
    // Using theme attributes
    slotTint = resolveThemeAttribute(R.attr.colorError)
    slotBackgroundColor = resolveThemeAttribute(R.attr.colorErrorContainer)
}

// Conditional styling based on state
fun updateSlotForStatus(status: Status) {
    binding.statusSlot.apply {
        slotType = CardLeftSlot.SlotType.ICON
        
        when (status) {
            Status.SUCCESS -> {
                slotSrc = R.drawable.ic_check
                slotTint = R.attr.colorForegroundSuccess
                slotBackgroundColor = R.attr.colorBackgroundSuccess
            }
            Status.WARNING -> {
                slotSrc = R.drawable.ic_warning
                slotTint = R.attr.colorForegroundWarning
                slotBackgroundColor = R.attr.colorBackgroundWarning
            }
            Status.ERROR -> {
                slotSrc = R.drawable.ic_error
                slotTint = R.attr.colorForegroundError
                slotBackgroundColor = R.attr.colorBackgroundError
            }
        }
    }
}
```

## Performance Considerations

- **Automatic Sizing** — Icon mode automatically constrains ImageView to 20dp to prevent memory overhead from large drawables

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use IMAGE type for photos and complex graphics | Use ICON type for detailed images that need full resolution |
| Apply consistent sizing (44dp standard, 56dp for emphasis) | Mix different slot sizes in the same card layout |
| Use theme attributes for colors to support dark/light modes | Hardcode color values that won't adapt to theme changes |
| Provide both background and tint colors for icons | Use tint without background for low contrast scenarios |
