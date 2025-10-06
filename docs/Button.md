# Button

## Overview
A customizable `Button` component extending **MaterialButton**, supporting different sizes, states, types, icons, and destructive variations. It is designed for flexible use across UI contexts with consistent styling, animations, and accessibility.


| Variation        | Default                                                                                                                           | Destructive | Disabled                                                                                                                          |
|------------------|-----------------------------------------------------------------------------------------------------------------------------------| ------- |-----------------------------------------------------------------------------------------------------------------------------------|
| Primary Button   | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759218901/Screenshot_2025-09-30_at_14.53.03_kyelw4.png) | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759219205/Screenshot_2025-09-30_at_14.59.37_pkilt5.png) | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759227287/Screenshot_2025-09-30_at_17.13.25_lz7p5z.png) |
| Secondary Button | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759227289/Screenshot_2025-09-30_at_17.13.39_dpxlso.png) | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759227287/Screenshot_2025-09-30_at_17.13.54_q1ueup.png) | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759227287/Screenshot_2025-09-30_at_17.13.25_lz7p5z.png) |

---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.button.Button
    android:id="@+id/btnPrimary"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:buttonType="PRIMARY"
    app:buttonCustomSize="MD"
    app:buttonState="REST"
    app:isButtonDisabled="false"
    app:isButtonDestructive="false"
    app:icon="@drawable/ic_star"
    app:iconPlacement="left"
/>
```

### 2. Initialize in Code

```kotlin
val button = findViewById<Button>(R.id.btnPrimary)

button.apply {
    setLabel("Click Me")
    setButtonSize(Button.ButtonSize.MD)
    setIcon(R.drawable.ic_star, Button.IconPlacement.LEFT)
}
```

---

## Display Modes

### 1. Type

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `PRIMARY` | `0` | Solid accent background with white text/icon | Primary action |
| `SECONDARY` | `1` | Transparent/outlined style with accent text | Secondary action |

### 2. State

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `DESTRUCTIVE` | flag | Red background or border style | Delete/remove actions |
| `DISABLED` | flag | Muted gray style, not clickable | Inactive states |
| `STATE_REST` | `0` | Default state | Idle |
| `STATE_ON_PRESS` | `1` | Pressed style + scale-down animation | Touch feedback |
| `STATE_ON_FOCUS` | `2` | Focused style with outline | Keyboard / accessibility |

---

## Properties Reference

### Size Properties

| Enum | Height (dp) | Horizontal Padding (dp) | Icon Size (dp) | Text Padding (dp) |
| ---- | ----------- | ----------------------- | -------------- | ----------------- |
| `XS` | 28 | 12 | 16 | 4 |
| `SM` | 36 | 12 | 20 | 4 |
| `MD` | 40 | 16 | 24 | 6 |
| `LG` | 48 | 24 | 24 | 8 |

### Behavior Properties

| Property | Type | Default | Description |
| -------- | ---- | ------- | ----------- |
| `isButtonDisabled` | `Boolean` | `false` | Makes the button unclickable |
| `isButtonDestructive` | `Boolean` | `false` | Applies destructive styling (red) |
| `iconPlacement` | `Enum` (`LEFT`, `RIGHT`) | `LEFT` | Icon alignment relative to text |
| `buttonDelegate` | `ButtonDelegate?` | `null` | Optional delegate for callbacks |

---

## Methods Reference

| Method | Parameters | Description |
| ------ | --------- | ----------- |
| `setButtonSize()` | `size: ButtonSize` | Changes the button size |
| `setButtonState()` | `state: ButtonState` | Updates button state (rest, pressed, focus) |
| `setButtonDisabled()` | `disabled: Boolean` | Enables/disables the button |
| `setDestructive()` | `destructive: Boolean` | Toggles destructive style |
| `setLabel()` | `label: String` | Updates the button text |
| `setCornerRadius()` | `radiusDp: Float` | Customizes corner radius |
| `setIcon()` | `drawable: Drawable?, placement: IconPlacement` | Sets icon dynamically |
| `setIcon()` | `@DrawableRes drawableRes: Int?, placement: IconPlacement` | Sets icon by resource |
| `setIconPlacement()` | `placement: IconPlacement` | Moves icon left/right |
| `clearIcon()` | – | Removes current icon |

---

## Usage Examples

```kotlin
// Primary button with icon
button.apply {
    setLabel("Continue")
    setButtonSize(Button.ButtonSize.LG)
    setIcon(R.drawable.ic_arrow_right, Button.IconPlacement.RIGHT)
}

// Secondary destructive button
button.apply {
    setLabel("Delete")
    setDestructive(true)
    setButtonSize(Button.ButtonSize.SM)
}
```

---

## Animation Details

![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759304663/buttongif_jxcgwd.gif)

| Animation | Duration | Interpolator | Description |
| --------- | -------- | ------------ | ----------- |
| `scaleDown` | 150ms | AccelerateDecelerate | Shrinks button on press |
| `scaleUp` | 150ms | AccelerateDecelerate | Restores button size on release |

---

## Performance Considerations
- **Drawable caching** → reduces allocations by reusing background styles.  
- **Color caching** → avoids repeated attribute lookups.  
- **Animation lightweight** → only scale transforms, no heavy redraws.  

---

## Best Practices

| ✅ Do | ❌ Don’t |
| ----- | ------- |
| Use `PRIMARY` for main actions | Overuse destructive mode |
| Pair icon with short text | Add long labels that break layout |
| Test across states (focus, press, disabled) | Assume only touch interaction |

---

⚠️ **Note**: This component leverages MaterialButton base but overrides default styling. Ensure your theme defines the required color attributes (`colorBackgroundDisabled`, `colorForegroundWhite`, etc.`).
