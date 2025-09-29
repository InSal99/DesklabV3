# Toast

## Overview

Toast is a custom Android view component that extends `MaterialCardView` to display temporary notification messages with animated slide-in/slide-out effects. It provides a Material Design-compliant alternative to Android's default Toast with better customization, including type-based styling (Success, Error, Info, General) and automatic dismissal with smooth animations.

| Feature / Variation | Preview |
| ------------------- | ------- |
| **SUCCESS** | Green background with success icon |
| **ERROR** | Red/Orange background with attention icon |
| **INFO** | Blue background with information icon |
| **GENERAL** | Dark background with custom/placeholder icon |

---

## Basic Usage

### 1. Add to Layout (Optional - Programmatic Usage Recommended)

```xml
<your.edts.components.toast.Toast
    android:id="@+id/customToast"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:toastType="SUCCESS"
    app:toastMessage="Operation successful!" />
```

### 2. Initialize in Code

```kotlin
// Method 1: Using static helper methods (Simplest)
Toast.success(context, "Operation successful!")
Toast.error(context, "Something went wrong!")
Toast.info(context, "Here's some information")
Toast.general(context, "General notification")

// Method 2: Using instance
val toast = Toast(context)
toast.setToast(Toast.Type.SUCCESS, "Operation completed!")
val parent = findViewById<ViewGroup>(android.R.id.content)
toast.showIn(parent)
```

## Display Modes (Type)

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `SUCCESS` | `0` | Green toast with success icon | Display successful operations/confirmations |
| `ERROR` | `1` | Red/Orange toast with attention icon | Display errors/failures/warnings |
| `INFO` | `2` | Blue toast with information icon | Display informational messages |
| `GENERAL` | `3` | Dark toast with placeholder icon | Display general notifications |

```kotlin
toast.setToast(Toast.Type.SUCCESS, "Your message here")
```

## Properties Reference

### Color Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `toastType.colorAttr` | `@AttrRes` | Varies by type | Background color attribute from theme |
| `strokeColor` | `@ColorInt` | `colorStrokeInteractive` | Border stroke color |
| `outlineAmbientShadowColor` | `@ColorInt` | `colorShadowNeutralAmbient` | Ambient shadow color (API 28+) |
| `outlineSpotShadowColor` | `@ColorInt` | `colorShadowNeutralKey` | Spot shadow color (API 28+) |

**Background Colors by Type:**
- **SUCCESS**: `colorBackgroundSuccessIntense`
- **ERROR**: `colorBackgroundAttentionIntense`
- **INFO**: `colorBackgroundInfoIntense`
- **GENERAL**: `colorBackgroundPrimaryInverse`

### Dimension Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `radius` | `Float` | `12dp` | Corner radius of the toast card |
| `cardElevation` | `Float` | `2dp` | Elevation/shadow depth |
| `strokeWidth` | `Int` | `1dp` | Border stroke width |
| `bottomMargin` | `Int` | `100dp` | Distance from bottom of screen |
| `leftMargin` / `rightMargin` | `Int` | `16dp` | Horizontal margins |

### Icon Properties

| Property Name | Type | Icon Resource | Description |
| ------------- | ---- | ------------- | ----------- |
| `toastIcon` | `Int?` | `null` | Custom icon (overrides type default) |
| `Type.iconRes` | `@DrawableRes` | Varies by type | Default icon for each type |

**Default Icons by Type:**
- **SUCCESS**: `ic_success`
- **ERROR**: `ic_attention`
- **INFO**: `ic_information`
- **GENERAL**: `placeholder`

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `onToastClickListener` | `(() -> Unit)?` | `null` | Click listener for toast interaction |
| `toastMessage` | `String` | `""` | Message text displayed in toast |


## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setToast()` | `type: Type, message: String` | Updates toast type and message, applies styling |
| `showIn()` | `parent: ViewGroup` | Displays toast in specified parent with animations |
| `success()` | `context: Context, message: String` | Static method to show success toast |
| `error()` | `context: Context, message: String` | Static method to show error toast |
| `info()` | `context: Context, message: String` | Static method to show info toast |
| `general()` | `context: Context, message: String` | Static method to show general toast |

## Usage Examples

```kotlin
// Example 1: Quick success message
Toast.success(this, "Profile updated successfully!")

// Example 2: Error notification
Toast.error(this, "Failed to connect to server")

// Example 3: Information message
Toast.info(this, "Swipe right to view more details")

// Example 4: General notification
Toast.general(this, "New message received")

// Example 5: Custom icon toast
val toast = Toast(this)
toast.toastIcon = R.drawable.ic_custom_icon
toast.setToast(Toast.Type.SUCCESS, "Custom icon message")
val parent = findViewById<ViewGroup>(android.R.id.content)
toast.showIn(parent)

// Example 6: With click listener
val toast = Toast(this)
toast.setToast(Toast.Type.INFO, "Tap to view details")
toast.onToastClickListener = {
    // Handle click action
    startActivity(Intent(this, DetailsActivity::class.java))
}
toast.showIn(findViewById(android.R.id.content))

// Example 7: In Fragment
Toast.success(requireContext(), "Item added to cart")

// Example 8: With ViewModel/Repository
viewModel.operationResult.observe(this) { result ->
    when (result) {
        is Success -> Toast.success(this, "Operation completed!")
        is Error -> Toast.error(this, result.message)
    }
}
```

## Performance Considerations

- **View Reuse**: Automatically removes existing toast before showing new one to prevent stacking
- **Animation Optimization**: Uses hardware-accelerated property animations (translationY)
- **Memory Efficiency**: Removes view from hierarchy after dismissal to prevent memory leaks
- **Tag-based Identification**: Uses unique tag for efficient toast lookup and replacement
- **Single Instance**: Ensures only one toast is visible at a time per parent container

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| Slide In | `300ms` | `DecelerateInterpolator` | Toast slides up from bottom with deceleration |
| Slide Out | `300ms` | `AccelerateInterpolator` | Toast slides down and disappears with acceleration |
| Auto Dismiss | `2500ms` | N/A | Delay before auto-dismissal starts |

### Animation Flow

1. **Entry Animation** (300ms): Toast translates from below screen to visible position (100dp from bottom screen)
2. **Display Duration** (2500ms): Toast remains visible
3. **Exit Animation** (300ms): Toast translates back down and is removed from hierarchy

## Implementation Details

### Layout Structure

The Toast component uses a binding layout (`LayoutToastViewBinding`) with:
- `ivIcon`: ImageView for the type-specific icon
- `tvMessage`: TextView for the message text

### Parent Container

- Automatically uses `android.R.id.content` when called via static methods
- Can accept any `ViewGroup` as parent via `showIn(parent)`
- Positioned at bottom with configurable margins

### Material Design Elements

- **Elevation**: 2dp shadow for depth
- **Corner Radius**: 12dp for modern rounded appearance
- **Stroke**: 1dp border for definition
- **Shadow Colors**: Supports API 28+ shadow color customization

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use static methods for quick notifications | Create multiple instances simultaneously |
| Keep messages concise (1-2 lines) | Display long paragraphs of text |
| Use appropriate type for context | Mix up type meanings (e.g., error for success) |
| Test on different screen sizes | Assume fixed positioning works everywhere |
| Use in Activity or Fragment contexts | Call from non-UI contexts without proper handling |
| Let toast auto-dismiss | Manually dismiss too quickly |
| Provide meaningful messages | Show generic "Error" without context |

## Required Resources

### Layout
- `layout_toast_view.xml`: Contains icon (ImageView) and message (TextView) layout structure

### Drawables
- `ic_success`: Success/checkmark icon
- `ic_attention`: Error/warning icon
- `ic_information`: Info icon
- `placeholder`: Default/general icon

### Dimensions
- `radius_12dp`: Corner radius
- `stroke_weight_1dp`: Border width
- `margin_16dp`: Horizontal margins

### Theme Attributes
- `colorBackgroundSuccessIntense`: Success background color
- `colorBackgroundAttentionIntense`: Error background color
- `colorBackgroundInfoIntense`: Info background color
- `colorBackgroundPrimaryInverse`: General background color
- `colorStrokeInteractive`: Border stroke color
- `colorShadowNeutralAmbient`: Ambient shadow color (API 28+)
- `colorShadowNeutralKey`: Spot shadow color (API 28+)

---

> **⚠️ Note**: Ensure `LayoutToastViewBinding` is properly generated from your layout XML. The layout must contain `ivIcon` (ImageView) and `tvMessage` (TextView) with proper IDs. This component requires Activity context for proper display in `android.R.id.content`.