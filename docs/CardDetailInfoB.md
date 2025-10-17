# Card: Detail Information B

| Feature / Variation   | Preview                                                                                                                                                                                             |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Full Featured Image   | ![Left Slot + Title + Description + Right Slot Icon + Indicator](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1760330888/cdib_full_featured_image_z6qefj.gif)     |
| Full Featured Icon    | ![Icon Left Slot + Title + Description + Right Slot Icon + Indicator](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286625/cdib_full_featured_icon_lc4vsz.gif) |
| LeftSlot & Title      | ![Left Slot + Title + Right Slot Icon](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286624/cdib_left_slot_title_u09fab.png)                                       |
| Title Only            | ![Title Only](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759286625/cdib_title_only_muz4dr.png)                                                                 |
| Custom Right Slot | ![Title + Description + Right Slot Custom](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1760328290/ScreenRecording2025-10-08at09.24.17-ezgif.com-video-to-gif-converter_znxj3x.gif)                                                                                                                                                        |

| **CardState** | **Visual Effect** |
| ------------- | ----------------- |
| **REST** | Default elevated background with interaction enabled |
| **ON_PRESS** | Pressed overlay effect during touch |
| **DISABLED** | Non-interactive state when right slot hidden |

## Overview

*A comprehensive detail information card component with customizable left slot (image/icon), title, optional description, optional right slot (image/custom view), and optional left border indicator. Features adaptive layout, and press state animations.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.card.detail.CardDetailInfoB
    android:id="@+id/cardDetailInfo"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cdibLeftSlotType="image"
    app:cdibLeftSlotSrc="@drawable/avatar_placeholder"
    app:cdibTitleText="Card Title"
    app:cdibDescText="Card Content Description"
    app:cdibRightSlotSrc="@drawable/ic_chevron_right"
    app:cdibShowIndicator="true"
    app:cdibIndicatorColor="?attr/colorStrokeUtilityUlangTahunIntense" />
```

### 2. Initialize in Code

```kotlin
val cardDetailInfo = binding.cardDetailInfo

// Set up delegate for click handling
cardDetailInfo.delegate = object : CardDetailInfoBDelegate {
    override fun onCardClick(card: CardDetailInfoB) {
        // Handle card click
        Log.d("MainActivity", "Card clicked: ${card.titleText}")
    }
}

// Configure properties programmatically
cardDetailInfo.apply {
    titleText = "Dynamic Title"
    descText = "Dynamic Description"
    showIndicator = true
    indicatorColor = R.attr.colorBackgroundAttentionIntense
}
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `REST` | `0` | Default interactive state | Normal display with click handling |
| `ON_PRESS` | `1` | Pressed state with overlay | User touch feedback |
| `DISABLED` | `2` | Non-interactive state | When `showRightSlot` is false |

```kotlin
// Card state is managed automatically based on showRightSlot property
cardDetailInfo.showRightSlot = false // Sets card to disabled state
```

## Properties Reference

### Content Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `titleText` | `String?` | `null` | Primary title text displayed prominently |
| `descText` | `String?` | `null` | Secondary description text |
| `showDescription` | `Boolean` | `true` | Controls description text visibility |

### Left Slot Properties

| Property Name | Type | Default | Description                              |
| ------------- | ---- | ------- |------------------------------------------|
| `leftSlotType` | `LeftSlotType` | `ICON` | Type of left slot content (IMAGE/ICON)   |
| `leftSlotSrc` | `Int?` | `null` | Drawable resource for left slot content  |
| `leftSlotUrl` | `String?` | `null` | Url image for left slot content          |
| `leftSlotSize` | `Int` | `32dp` | Size of the left slot in pixels          |
| `leftSlotBackgroundColor` | `Int?` | `null` | Background color for left slot container |
| `leftSlotTint` | `Int?` | `null` | Tint color for left slot icon            |
| `showLeftSlot` | `Boolean` | `true` | Controls left slot visibility            |

### Right Slot Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `rightSlotType` | `RightSlotType` | `IMAGE` | Type of right slot content (IMAGE/CUSTOM) |
| `rightSlotSrc` | `Int?` | `null` | Drawable resource for right slot image |
| `rightSlotTint` | `Int?` | `null` | Tint color for right slot image |
| `showRightSlot` | `Boolean` | `true` | Controls right slot visibility and card interactivity |

### Visual Indicator Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `showIndicator` | `Boolean` | `true` | Controls left border indicator visibility |
| `indicatorColor` | `Int?` | `colorStrokeUtilityUlangTahunIntense` | Color for the left border indicator |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `CardDetailInfoBDelegate?` | `null` | Click event handler interface |
| `cardState` | `CardState` | `REST` | Current visual state (managed internally) |

## Data Models 

### CardDetailInfoBDelegate Interface

| Method | Parameters | Required | Description                                                        |
| ------ | ---------- | -------- |--------------------------------------------------------------------|
| `onCardClick()` | `card: CardDetailInfoB` | ✅ | Called when the card is clicked (only when interactive)            |
| `onRightSlotClick()` | `card: CardDetailInfoB` | ✅ | Called when the card right slot is clicked (only when interactive) |

```kotlin
val delegate = object : CardDetailInfoBDelegate {
    override fun onCardClick(card: CardDetailInfoB) {
        // Handle click event
        navigateToDetail(card.titleText)
    }
}
```

### LeftSlotType Enum

| Value | Description | Use Case |
| ----- | ----------- | -------- |
| `IMAGE` | Display image content | User avatars, photos |
| `ICON` | Display icon with background | Status icons, categories |

### RightSlotType Enum

| Value | Description | Use Case |
| ----- | ----------- | -------- |
| `IMAGE` | Display image resource | Navigation arrows, action icons |
| `CUSTOM` | Display custom view | Complex UI elements, badges |

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setRightSlotView()` | `view: View` | Sets a custom view for the right slot and switches to CUSTOM type |

## Usage Examples

```kotlin
// Full featured card with image left slot
binding.cardDetailInfo.apply {
    leftSlotType = CardDetailInfoB.LeftSlotType.IMAGE
    leftSlotSrc = R.drawable.avatar_placeholder
    titleText = "John Doe"
    descText = "Software Engineer"
    rightSlotSrc = R.drawable.ic_chevron_right
    showIndicator = true
    indicatorColor = R.attr.colorBackgroundAttentionIntense
    
    delegate = object : CardDetailInfoBDelegate {
        override fun onCardClick(card: CardDetailInfoB) {
            showUserProfile(card.titleText)
        }
    }
}

// Icon-based card with custom styling
binding.statusCard.apply {
    leftSlotType = CardDetailInfoB.LeftSlotType.ICON
    leftSlotSrc = R.drawable.ic_star
    leftSlotSize = (44 * resources.displayMetrics.density).toInt()
    leftSlotBackgroundColor = R.attr.colorForegroundWarningSubtle
    leftSlotTint = R.attr.colorBackgroundAttentionIntense
    titleText = "Premium Status"
    descText = "Active subscription"
    showIndicator = true
    indicatorColor = R.attr.colorBackgroundAttentionIntense
}

// Minimal non-interactive card
binding.simpleCard.apply {
    titleText = "Simple Title"
    showDescription = false
    showRightSlot = false
    showIndicator = false
}

// Card with custom right slot view
val customBadge = LayoutInflater.from(context)
    .inflate(R.layout.custom_badge, null)
binding.customCard.setRightSlotView(customBadge)
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Full featured card -->
<com.edts.components.card.detail.CardDetailInfoB
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cdibLeftSlotType="image"
    app:cdibLeftSlotSrc="@drawable/avatar_placeholder"
    app:cdibRightSlotSrc="@drawable/ic_chevron_right"
    app:cdibTitleText="Card Title"
    app:cdibDescText="Card Content Description" />

<!-- Icon with indicator and styling -->
<com.edts.components.card.detail.CardDetailInfoB
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cdibLeftSlotType="icon"
    app:cdibShowIndicator="true"
    app:cdibIndicatorColor="?attr/colorBackgroundAttentionIntense"
    app:cdibLeftSlotSrc="@drawable/ic_star"
    app:cdibLeftSlotBackgroundColor="?attr/colorForegroundWarningSubtle"
    app:cdibLeftSlotTint="?attr/colorBackgroundAttentionIntense"
    app:cdibTitleText="Card Title"
    app:cdibDescText="Card Content Description"
    app:cdibRightSlotSrc="@drawable/ic_chevron_right" />

<!-- Title-only minimal card -->
<com.edts.components.card.detail.CardDetailInfoB
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cdibShowIndicator="false"
    app:cdibShowRightSlot="false"
    app:cdibShowLeftSlot="false"
    app:cdibShowDescription="false"
    app:cdibTitleText="Card Title" />
```

### Dynamic Content Updates

```kotlin
// Update card content dynamically
fun updateCardContent(user: User) {
    binding.userCard.apply {
        titleText = user.name
        descText = user.role
        leftSlotSrc = user.avatarResId
        
        // Show indicator for premium users
        showIndicator = user.isPremium
        if (user.isPremium) {
            indicatorColor = R.attr.colorBackgroundSuccessIntense
        }
    }
}

// Toggle card interactivity
fun setCardInteractive(interactive: Boolean) {
    binding.cardDetailInfo.showRightSlot = interactive
    // This automatically enables/disables click handling
}
```

## Performance Considerations

- **Custom Drawing** — Uses efficient path-based drawing for the left border indicator
- **Layout Optimization** — Dynamically adjusts constraints based on visibility states to minimize layout passes
- **ViewBinding** — Uses ViewBinding for efficient view access and type safety

## Animation Details

| Animation Type | Duration | Description |
| -------------- | -------- | ----------- |
| `Press State` | Instant | Background overlay change on touch down/up |
| `Layout Transitions` | System Default | Smooth constraint changes when toggling visibility |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use consistent left slot sizes within the same list | Mix different slot sizes randomly |
| Provide meaningful titles and descriptions | Use placeholder text in production |
| Set appropriate indicator colors for different states | Use the same indicator color for all cards |
| Test visibility combinations thoroughly | Assume all combinations work without testing |
| Use theme colors for consistent styling | Hardcode color values |

---

> **⚠️ Note**: This component automatically manages its interactive state based on the `showRightSlot` property. When `showRightSlot` is false, the card becomes non-interactive and click events are disabled. The custom indicator drawing uses precise path calculations for smooth rendering at all densities.