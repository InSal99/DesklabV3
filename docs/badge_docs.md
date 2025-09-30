# Badge

| Feature / Variation | Preview |
| ------------------- | ------- |
| With Text | ![Badge with number "5"] |
| Dot Only | ![Small circular indicator] |
| Color Variants | ![Info / Attention / Warning styles] |

| **Display Mode** | **Visual** |
| ---------------- | ---------- |
| **Text Badge** | Circle with text content |
| **Dot Badge** | Small indicator dot without text |

## Overview

*A lightweight badge component for displaying notification counts, status indicators, or small labels. Supports both text and dot-only modes with customizable colors for background, stroke, and text.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.badge.Badge
    android:id="@+id/badge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeText="5"
    app:badgeBackgroundTint="?attr/colorBackgroundInfoSubtle"
    app:badgeStrokeColor="@android:color/transparent"
    app:badgeTextColor="?attr/colorForegroundInfoIntense"
    app:showText="true" />
```

### 2. Initialize in Code

```kotlin
val badge = binding.badge

// Update badge text
badge.badgeText = "10"

// Change badge colors
badge.badgeBackgroundTint = ContextCompat.getColor(context, R.color.custom_background)
badge.badgeTextColor = ContextCompat.getColor(context, R.color.custom_text)
```

## Display Modes 

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `Text Badge` | `showText = true` | Displays badge with text content | Notification counts, numerical indicators |
| `Dot Badge` | `showText = false` | Shows small circular indicator only | Status dots, presence indicators |

```kotlin
// Text badge mode
badge.showText = true
badge.badgeText = "99+"

// Dot badge mode
badge.showText = false
```

## Properties Reference

### Color Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `badgeBackgroundTint` | `@ColorInt?` | `?attr/colorBackgroundAttentionIntense` | Background fill color of the badge circle |
| `badgeStrokeColor` | `@ColorInt?` | `?attr/colorStrokeInteractive` | Border stroke color of the badge circle |
| `badgeTextColor` | `@ColorInt?` | `?attr/colorForegroundPrimaryInverse` | Text color for badge content |

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `badgeText` | `String?` | `null` | Text content displayed in the badge |
| `showText` | `Boolean` | `true` | Controls text visibility (true = text badge, false = dot badge) |

## Usage Examples

```kotlin
// Info badge with text
binding.infoBadge.apply {
    badgeText = "5"
    badgeBackgroundTint = getThemeColor(R.attr.colorBackgroundInfoSubtle)
    badgeStrokeColor = Color.TRANSPARENT
    badgeTextColor = getThemeColor(R.attr.colorForegroundInfoIntense)
    showText = true
}

// Attention badge with stroke
binding.attentionBadge.apply {
    badgeText = "12"
    badgeBackgroundTint = getThemeColor(R.attr.colorBackgroundAttentionSubtle)
    badgeStrokeColor = getThemeColor(R.attr.colorStrokeAttentionIntense)
    badgeTextColor = getThemeColor(R.attr.colorForegroundAttentionIntense)
    showText = true
}

// Warning dot indicator
binding.warningDot.apply {
    badgeStrokeColor = getThemeColor(R.attr.colorStrokeWarningSubtle)
    showText = false
}

// Dynamic count updates
fun updateNotificationCount(count: Int) {
    binding.notificationBadge.apply {
        badgeText = when {
            count > 99 -> "99+"
            count > 0 -> count.toString()
            else -> "0"
        }
        showText = count > 0
    }
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Info badge with text -->
<com.edts.components.badge.Badge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeBackgroundTint="?attr/colorBackgroundInfoSubtle"
    app:badgeStrokeColor="@android:color/transparent"
    app:badgeTextColor="?attr/colorForegroundInfoIntense"
    app:badgeText="5"
    app:showText="true" />

<!-- Attention badge with stroke -->
<com.edts.components.badge.Badge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeStrokeColor="?attr/colorStrokeAttentionIntense"
    app:badgeBackgroundTint="?attr/colorBackgroundAttentionSubtle"
    app:badgeTextColor="?attr/colorForegroundAttentionIntense"
    app:badgeText="5"
    app:showText="true" />

<!-- Warning dot indicator (no text) -->
<com.edts.components.badge.Badge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeStrokeColor="?attr/colorStrokeWarningSubtle"
    app:badgeText="5"
    app:showText="false" />
```

### Programmatic Customization

```kotlin
// Semantic color variants
fun applyInfoStyle(badge: Badge) {
    badge.apply {
        badgeBackgroundTint = getThemeColor(R.attr.colorBackgroundInfoSubtle)
        badgeTextColor = getThemeColor(R.attr.colorForegroundInfoIntense)
        badgeStrokeColor = Color.TRANSPARENT
    }
}

fun applyAttentionStyle(badge: Badge) {
    badge.apply {
        badgeBackgroundTint = getThemeColor(R.attr.colorBackgroundAttentionSubtle)
        badgeTextColor = getThemeColor(R.attr.colorForegroundAttentionIntense)
        badgeStrokeColor = getThemeColor(R.attr.colorStrokeAttentionIntense)
    }
}

// Custom colors
badge.apply {
    badgeBackgroundTint = Color.parseColor("#FF5722")
    badgeTextColor = Color.WHITE
    badgeStrokeColor = Color.parseColor("#D84315")
}
```

## Performance Considerations

- **ViewBinding** — Efficient view access using ViewBinding instead of findViewById
- **ColorStateList Optimization** — Uses ColorStateList.valueOf for single-state color assignments

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use semantic theme colors for consistency | Hardcode color values directly |
| Keep text content short (1-3 characters) | Display long strings that break layout |
| Use dot mode for simple indicators | Show empty badges with showText=true |
| Format large counts as "99+" | Display exact counts over 999 |
| Set transparent stroke when not needed | Leave stroke color unset if unused |
| Use appropriate color contrast | Use low-contrast color combinations |

---

> **⚠️ Note**: When using `showText="false"`, the `badgeText` value is ignored but can still be set for future toggling.