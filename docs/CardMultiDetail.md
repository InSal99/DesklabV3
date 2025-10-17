# Card: Multi Detail Card

| Feature / Variation | Preview                                                                                                                                           |
| ------------------- |---------------------------------------------------------------------------------------------------------------------------------------------------|
| Full Featured (Icon) | ![Icon + Title + Info1 + Info2 + Right Arrow](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759227886/cmdc_full_featured_icon_m7uuar.gif)   |
| Single Info (Icon) | ![Icon + Title + Info1 + Right Arrow](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759227886/cmdc_single_info_icon_mqvb9c.gif)             |
| Full Featured (Image) | ![Image + Title + Info1 + Info2 + Right Arrow](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759227885/cmdc_full_featured_image_zngf7t.gif) |
| Non-Clickable (Image) | ![Image + Title + Info1 + Info2](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759227884/cmdc_non_clickable_image_icn2ej.png)               |
| Minimal | ![Title + Info1 + Info2](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759227884/cmdc_minimal_ha1zej.png)                                                                                                                        |

| **LeftSlotType** | **Display** |
| ---------------- | ----------- |
| **ICON** | Shows icon with background tint |
| **IMAGE** | Shows circular image/avatar |

## Overview

*A versatile multi-detail card component that displays structured information with customizable left slot (icon or image), title, dual info lines, and optional right navigation indicator. Features intelligent clickability based on right slot visibility and press state animations.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.card.multi.detail.CardMultiDetail
    android:id="@+id/cardMultiDetail"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cmdLeftSlotType="icon"
    app:cmdLeftSlotSrc="@drawable/ic_user_person"
    app:cmdLeftSlotTint="?attr/colorForegroundInfoIntense"
    app:cmdLeftSlotBackgroundColor="?attr/colorBackgroundInfoSubtle"
    app:cmdTitle="Main Title"
    app:cmdInfo1Text="Primary information"
    app:cmdInfo2Text="Secondary information"
    app:cmdShowInfo2="true"
    app:cmdRightSlotSrc="@drawable/ic_chevron_right"
    app:cmdRightSlotTint="?attr/colorForegroundAccentPrimaryIntense" />
```

### 2. Initialize in Code

```kotlin
val cardMultiDetail = binding.cardMultiDetail

// Set up delegate for click handling
cardMultiDetail.delegate = object : CardMultiDetailDelegate {
    override fun onCardClick(card: CardMultiDetail) {
        // Handle card click
        Log.d("MainActivity", "Card clicked: ${card.cmdTitle}")
        navigateToDetails()
    }
}
```

## Display Modes 

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `ICON` | `0` | Left slot displays an icon with tinted background | Status indicators, categories |
| `IMAGE` | `1` | Left slot displays a circular image | User profiles, avatars |

```kotlin
cardMultiDetail.leftSlotType = CardMultiDetail.LeftSlotType.ICON
// or
cardMultiDetail.leftSlotType = CardMultiDetail.LeftSlotType.IMAGE
```

## Properties Reference

### Content Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `cmdTitle` | `String?` | `null` | Main title text displayed prominently |
| `cmdInfo1Text` | `String?` | `null` | Primary information text |
| `cmdInfo2Text` | `String?` | `null` | Secondary information text |
| `cmdShowInfo2` | `Boolean` | `true` | Controls second info line visibility |

### Left Slot Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `leftSlotType` | `LeftSlotType` | `ICON` | Type of content in left slot (icon or image) |
| `leftSlotSrc` | `Int?` | `null` | Drawable resource ID for left slot content |
| `leftSlotUrl` | `String?` | `null` | Url image for left slot content          |
| `leftSlotBackgroundColor` | `Int?` | `null` | Background color for icon type left slot |
| `leftSlotTint` | `Int?` | `null` | Tint color applied to icon type left slot |
| `cmdShowLeftSlot` | `Boolean` | `true` | Controls left slot visibility |

### Right Slot Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `rightSlotSrc` | `Int?` | `null` | Drawable resource ID for right slot icon |
| `rightSlotTint` | `Int?` | `null` | Tint color applied to right slot icon |
| `cmdShowRightSlot` | `Boolean` | `true` | Controls right slot visibility and clickability |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `CardMultiDetailDelegate?` | `null` | Click event handler interface |
| `cardState` | `CardState` | `REST` | Current visual state (managed internally) |

## Data Models 

### CardMultiDetailDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onCardClick()` | `card: CardMultiDetail` | ✅ | Called when the card is clicked (only if clickable) |
| `onRightSlotClick()` | `card: CardMultiDetail` | ✅ | Called when the card right slot is clicked (only when interactive) |

```kotlin
val delegate = object : CardMultiDetailDelegate {
    override fun onCardClick(card: CardMultiDetail) {
        // Handle click event
        when (card.cmdTitle) {
            "User Profile" -> openProfile()
            "Settings" -> openSettings()
            else -> showGenericDetails()
        }
    }
}
```

### LeftSlotType Enum

| Property | Type | Required | Description |
| -------- | ---- | -------- | ----------- |
| `ICON` | `LeftSlotType` | N/A | Display icon with background color |
| `IMAGE` | `LeftSlotType` | N/A | Display circular image/avatar |

```kotlin
val slotType = CardMultiDetail.LeftSlotType.fromValue(0) // ICON
// or
val slotType = CardMultiDetail.LeftSlotType.IMAGE
```

## Usage Examples

```kotlin
// Full featured card with icon
binding.cardLeaveRequest.apply {
    leftSlotType = CardMultiDetail.LeftSlotType.ICON
    leftSlotSrc = R.drawable.ic_calendar
    leftSlotTint = ContextCompat.getColor(context, R.color.info_intense)
    leftSlotBackgroundColor = ContextCompat.getColor(context, R.color.info_subtle)
    cmdTitle = "21 Jun - 23 Jun 2025"
    cmdInfo1Text = "Cuti Tahunan"
    cmdInfo2Text = "3 Days"
    cmdShowInfo2 = true
    rightSlotSrc = R.drawable.ic_chevron_right
    rightSlotTint = ContextCompat.getColor(context, R.color.accent_primary)
    
    delegate = leaveCardDelegate
}

// Profile card with image
binding.cardProfile.apply {
    leftSlotType = CardMultiDetail.LeftSlotType.IMAGE
    leftSlotSrc = R.drawable.user_avatar
    cmdTitle = "John Doe"
    cmdInfo1Text = "Software Engineer"
    cmdInfo2Text = "Active"
    cmdShowInfo2 = true
    rightSlotSrc = R.drawable.ic_chevron_right
}

// Non-clickable display card
binding.cardInfo.apply {
    leftSlotType = CardMultiDetail.LeftSlotType.ICON
    leftSlotSrc = R.drawable.ic_info
    cmdTitle = "System Status"
    cmdInfo1Text = "All systems operational"
    cmdShowInfo2 = false
    cmdShowRightSlot = false // Makes card non-clickable
}

// Dynamic content updates
fun updateLeaveStatus(status: String, daysRemaining: Int) {
    binding.cardLeave.apply {
        cmdInfo1Text = status
        cmdInfo2Text = "$daysRemaining days remaining"
        cmdShowInfo2 = daysRemaining > 0
    }
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Icon-based card with full features -->
<com.edts.components.card.multi.detail.CardMultiDetail
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cmdLeftSlotType="icon"
    app:cmdLeftSlotTint="?attr/colorForegroundInfoIntense"
    app:cmdLeftSlotBackgroundColor="?attr/colorBackgroundInfoSubtle"
    app:cmdLeftSlotSrc="@drawable/ic_user_person"
    app:cmdTitle="21 Jun - 23 Jun 2025"
    app:cmdInfo1Text="Cuti Tahunan"
    app:cmdInfo2Text="3 Days"
    app:cmdShowInfo2="true"
    app:cmdRightSlotSrc="@drawable/ic_chevron_right"
    app:cmdRightSlotTint="?attr/colorForegroundAccentPrimaryIntense" />

<!-- Single info line variant -->
<com.edts.components.card.multi.detail.CardMultiDetail
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cmdLeftSlotType="icon"
    app:cmdLeftSlotTint="?attr/colorForegroundSuccessIntense"
    app:cmdLeftSlotBackgroundColor="?attr/colorBackgroundSuccessSubtle"
    app:cmdLeftSlotSrc="@drawable/ic_user_person"
    app:cmdRightSlotSrc="@drawable/ic_chevron_right"
    app:cmdTitle="7 - 8 Jul 2025"
    app:cmdShowInfo2="false"
    app:cmdInfo1Text="Izin Sakit - Dengan Surat Dokter" />

<!-- Image-based card -->
<com.edts.components.card.multi.detail.CardMultiDetail
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cmdLeftSlotType="image"
    app:cmdLeftSlotSrc="@drawable/avatar_placeholder"
    app:cmdTitle="21 Jun - 23 Jun 2025"
    app:cmdInfo1Text="Cuti Tahunan"
    app:cmdInfo2Text="3 Days"
    app:cmdShowInfo2="true"
    app:cmdRightSlotSrc="@drawable/ic_chevron_right" />

<!-- Non-clickable card (no right slot) -->
<com.edts.components.card.multi.detail.CardMultiDetail
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cmdLeftSlotType="image"
    app:cmdLeftSlotSrc="@drawable/avatar_placeholder"
    app:cmdTitle="21 Jun - 23 Jun 2025"
    app:cmdInfo1Text="Cuti Tahunan"
    app:cmdInfo2Text="3 Days"
    app:cmdShowInfo2="true"
    app:cmdShowRightSlot="false" />

<!-- Minimal card (no slots visible) -->
<com.edts.components.card.multi.detail.CardMultiDetail
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cmdLeftSlotType="image"
    app:cmdLeftSlotSrc="@drawable/avatar_placeholder"
    app:cmdTitle="21 Jun - 23 Jun 2025"
    app:cmdInfo1Text="Cuti Tahunan"
    app:cmdInfo2Text="3 Days"
    app:cmdShowInfo2="true"
    app:cmdShowLeftSlot="false"
    app:cmdShowRightSlot="false" />
```

### Programmatic Customization

```kotlin
// Theme-based color updates
cardMultiDetail.apply {
    leftSlotTint = getCachedColor(R.attr.colorForegroundWarningIntense)
    leftSlotBackgroundColor = getCachedColor(R.attr.colorBackgroundWarningSubtle)
    rightSlotTint = getCachedColor(R.attr.colorForegroundAccentPrimaryIntense)
}

// Content updates with animations
fun animateContentChange(newTitle: String, newInfo1: String, newInfo2: String?) {
    cardMultiDetail.apply {
        cmdTitle = newTitle
        cmdInfo1Text = newInfo1
        cmdInfo2Text = newInfo2
        cmdShowInfo2 = !newInfo2.isNullOrEmpty()
    }
}

// Toggle clickability
fun setCardClickable(clickable: Boolean) {
    cardMultiDetail.cmdShowRightSlot = clickable
    // Clickability is automatically managed based on right slot visibility
}
```

## Performance Considerations

- **ViewBinding** — Uses ViewBinding for efficient view access with nested custom components
- **Intelligent Clickability** — Automatically manages click states based on right slot visibility to prevent unnecessary touch handling

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| `Press State` | Instant | N/A | Background color change on touch down/up |
| `Foreground Overlay` | Instant | N/A | Overlay drawable for pressed state feedback |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use consistent left slot types within the same list | Mix icon and image types randomly |
| Keep titles concise and meaningful | Use overly long titles that wrap multiple lines |
| Use semantic colors for different states/categories | Apply random colors without meaning |
| Set cmdShowRightSlot=false for non-interactive cards | Leave right slot visible on non-clickable cards |
| Implement CardMultiDetailDelegate for clickable cards | Forget to handle click events |
| Use cmdShowInfo2=false when second info is not needed | Leave empty or null info2 with showInfo2=true |

---

> **⚠️ Note**: The card's clickability is automatically determined by the cmdShowRightSlot property. When set to false, the card becomes non-clickable and removes touch event handling for better performance. The component uses nested custom components (CardLeftSlot) for the left slot functionality.