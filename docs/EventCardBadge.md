# Event Card: Badge

| Feature / Variation | Preview                                                                                                                                    |
| ------------------- |--------------------------------------------------------------------------------------------------------------------------------------------|
| Large - Live | ![Berlangsung (Red background, white text)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228472/ecb_large_live_rppjjo.png)       |
| Large - Invited | ![Diundang (Yellow background, white text)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228477/ecb_large_invited_qw0o6p.png)    |
| Large - Registered | ![Terdaftar (Green background, white text)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228471/ecb_large_registered_vaddhy.png)                                                                                              |
| Small - Registered | ![Terdaftar (Green background, white text)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228474/ecb_small_registered_qgekey.png) |
| Small - Live | ![Berlangsung (Compact, red)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228477/ecb_small_live_podjzu.png)                     |
| Small - Attended | ![Hadir (Compact, gray)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228471/ecb_small_attended_afvu5k.png)                      |
| Small - Not Attended | ![Tidak Hadir (Compact, gray)](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228475/ecb_small_not_attended_skj2dv.png)            |

| **BadgeType** | **Color Theme** |
| ------------- | --------------- |
| **LIVE** | Attention Intense (Red) |
| **INVITED** | Warning Intense (Yellow) |
| **REGISTERED** | Success Intense (Green) |
| **ATTENDED** | Tertiary Background |
| **NOTATTENDED** | Tertiary Background |

## Overview

*A status badge component designed for event cards that displays event states with distinct color schemes. Supports two sizes (Small and Large) and five badge types representing different event statuses.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.event.card.EventCardBadge
    android:id="@+id/eventBadge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="live"
    app:badgeSize="Large"
    app:eventCardBadgeText="Berlangsung" />
```

### 2. Initialize in Code

```kotlin
val eventBadge = binding.eventBadge

// Update badge dynamically
eventBadge.apply {
    badgeType = EventCardBadge.BadgeType.REGISTERED
    badgeSize = EventCardBadge.BadgeSize.LARGE
    badgeText = "Terdaftar"
}
```

## Display Modes

### Badge Sizes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `SMALL` | `0` | Compact badge with minimal padding (8dp horizontal, 0dp vertical) | Space-constrained layouts, list items |
| `LARGE` | `1` | Standard badge with full padding (8dp horizontal, 2dp vertical) | Featured cards, prominent status display |

```kotlin
eventBadge.badgeSize = EventCardBadge.BadgeSize.SMALL
```

### Badge Types

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `LIVE` | `0` | Red attention-grabbing style | Ongoing events |
| `INVITED` | `1` | Yellow warning style | Pending invitations |
| `REGISTERED` | `2` | Green success style | Confirmed registration |
| `ATTENDED` | `3` | Gray tertiary style | Past event attendance confirmed |
| `NOTATTENDED` | `4` | Gray tertiary style | Past event non-attendance |

```kotlin
eventBadge.badgeType = EventCardBadge.BadgeType.LIVE
```

## Properties Reference

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `badgeText` | `String?` | `null` | Text displayed on the badge |

### Visual Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `badgeType` | `BadgeType` | `LIVE` | Status type determining color scheme |
| `badgeSize` | `BadgeSize` | `LARGE` | Size variant affecting padding and prominence |

## Data Models

### BadgeType Enum

| Property | Type | Required | Description |
| -------- | ---- | -------- | ----------- |
| `LIVE` | `BadgeType` | ✅ | Active/ongoing event status (red) |
| `INVITED` | `BadgeType` | ✅ | Invitation pending status (yellow) |
| `REGISTERED` | `BadgeType` | ✅ | Registration confirmed status (green) |
| `ATTENDED` | `BadgeType` | ✅ | Past event attended status (gray) |
| `NOTATTENDED` | `BadgeType` | ✅ | Past event not attended status (gray) |

```kotlin
val badgeType = EventCardBadge.BadgeType.REGISTERED
```

### BadgeSize Enum

| Property | Type | Required | Description |
| -------- | ---- | -------- | ----------- |
| `SMALL` | `BadgeSize` | ✅ | Compact size with no vertical padding |
| `LARGE` | `BadgeSize` | ✅ | Standard size with 2dp vertical padding |

```kotlin
val badgeSize = EventCardBadge.BadgeSize.LARGE
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `fromValue()` | `value: Int` | Static method to convert integer to BadgeType or BadgeSize enum |

## Usage Examples

```kotlin
// Basic setup with ViewBinding
binding.eventBadge.apply {
    badgeType = EventCardBadge.BadgeType.LIVE
    badgeSize = EventCardBadge.BadgeSize.LARGE
    badgeText = "Berlangsung"
}

// Dynamic status update based on event state
fun updateEventStatus(status: String) {
    binding.eventBadge.apply {
        when (status) {
            "live" -> {
                badgeType = EventCardBadge.BadgeType.LIVE
                badgeText = "Berlangsung"
            }
            "invited" -> {
                badgeType = EventCardBadge.BadgeType.INVITED
                badgeText = "Diundang"
            }
            "registered" -> {
                badgeType = EventCardBadge.BadgeType.REGISTERED
                badgeText = "Terdaftar"
            }
            "attended" -> {
                badgeType = EventCardBadge.BadgeType.ATTENDED
                badgeText = "Hadir"
            }
            "not_attended" -> {
                badgeType = EventCardBadge.BadgeType.NOTATTENDED
                badgeText = "Tidak Hadir"
            }
        }
    }
}

// Compact badge for list items
binding.compactBadge.apply {
    badgeSize = EventCardBadge.BadgeSize.SMALL
    badgeType = EventCardBadge.BadgeType.REGISTERED
    badgeText = "Terdaftar"
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Large Live Badge -->
<com.edts.components.event.card.EventCardBadge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="live"
    app:badgeSize="Large"
    app:eventCardBadgeText="Berlangsung" />

<!-- Large Invited Badge -->
<com.edts.components.event.card.EventCardBadge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="invited"
    app:badgeSize="Large"
    app:eventCardBadgeText="Diundang" />

<!-- Large Registered Badge -->
<com.edts.components.event.card.EventCardBadge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="registered"
    app:badgeSize="Large"
    app:eventCardBadgeText="Terdaftar" />

<!-- Small Registered Badge -->
<com.edts.components.event.card.EventCardBadge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="registered"
    app:badgeSize="Small"
    app:eventCardBadgeText="Terdaftar" />

<!-- Small Attended Badge -->
<com.edts.components.event.card.EventCardBadge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="attended"
    app:badgeSize="Small"
    app:eventCardBadgeText="Hadir" />

<!-- Small Not Attended Badge -->
<com.edts.components.event.card.EventCardBadge
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:badgeType="notattended"
    app:badgeSize="Small"
    app:eventCardBadgeText="Tidak Hadir" />
```

### Programmatic Customization

```kotlin
// Runtime property updates
eventBadge.apply {
    badgeType = EventCardBadge.BadgeType.LIVE
    badgeSize = EventCardBadge.BadgeSize.LARGE
    badgeText = "Now Live!"
}

// Convert from integer value (useful for API responses)
val typeFromApi = 2 // represents REGISTERED
eventBadge.badgeType = EventCardBadge.BadgeType.fromValue(typeFromApi)

// Conditional sizing based on context
eventBadge.badgeSize = if (isCompactLayout) {
    EventCardBadge.BadgeSize.SMALL
} else {
    EventCardBadge.BadgeSize.LARGE
}
```

## Color Theme Reference

### Badge Type Color Mappings

| Badge Type | Background Color Attribute | Text Color Attribute | Visual Description |
| ---------- | -------------------------- | -------------------- | ------------------ |
| `LIVE` | `colorBackgroundAttentionIntense` | `colorForegroundPrimaryInverse` | Red background, white text |
| `INVITED` | `colorBackgroundWarningIntense` | `colorForegroundPrimaryInverse` | Yellow background, white text |
| `REGISTERED` | `colorBackgroundSuccessIntense` | `colorForegroundPrimaryInverse` | Green background, white text |
| `ATTENDED` | `colorBackgroundTertiary` | `colorForegroundTertiary` | Gray background, gray text |
| `NOTATTENDED` | `colorBackgroundTertiary` | `colorForegroundTertiary` | Gray background, gray text |

### Text Appearance

| Badge Size | Text Appearance Attribute | Padding (H × V) |
| ---------- | ------------------------- | --------------- |
| `SMALL` | `l3SemiBold` | 8dp × 0dp |
| `LARGE` | `l3SemiBold` | 8dp × 2dp |

## Performance Considerations

- **Theme Attribute Resolution** — Uses TypedValue to resolve theme colors dynamically, adapting to light/dark mode automatically
- **Enum-Based State** — Type-safe badge configuration prevents invalid state combinations
- **ViewBinding** — Eliminates findViewById lookups for improved view access performance

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use SMALL size for list items and dense layouts | Use LARGE size in space-constrained areas |
| Keep badge text concise (1-2 words) | Use lengthy descriptions as badge text |
| Use appropriate badge types for semantic meaning | Mix badge types arbitrarily without status logic |
