# Bottom Tray

## Overview

BottomTray is a highly customizable Android BottomSheetDialogFragment that provides a Material Design-compliant bottom sheet with advanced features including keyboard handling, snap points, edge-to-edge display, customizable styling, and footer actions. It extends `BottomSheetDialogFragment` to offer a flexible container for bottom sheet content with automatic window inset handling and smooth animations.

### Preview
![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_600/v1759228566/traygif_whtbkz.gif)

---

## Basic Usage

### 1. Create Instance

```kotlin
// Method 1: Simple instance
val bottomTray = BottomTray.newInstance()

// Method 2: Instance with configuration
val bottomTray = BottomTray.newInstance(
    title = "Select Option",
    showDragHandle = true,
    showFooter = true,
    hasShadow = true,
    hasStroke = true
)

// Method 3: Manual configuration
val bottomTray = BottomTray().apply {
    setTitle("Custom Title")
    dragHandleVisibility = true
    showFooter = true
    hasShadow = true
    hasStroke = true
}
```

### 2. Set Content View

```kotlin
val contentView = layoutInflater.inflate(R.layout.bottom_sheet_content, null)
bottomTray.setTrayContentView(contentView)
```

### 3. Show Bottom Sheet

```kotlin
bottomTray.show(supportFragmentManager, "BottomTray")
```

## Properties Reference

### Display Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `dragHandleVisibility` | `Boolean` | `true` | Shows/hides drag handle and controls draggability |
| `showFooter` | `Boolean` | `true` | Shows/hides footer section |
| `hasShadow` | `Boolean` | `true` | Enables 8dp elevation shadow |
| `hasStroke` | `Boolean` | `true` | Enables 1dp border stroke |
| `isCancelableOnTouchOutside` | `Boolean` | `true` | Allows dismissal by tapping outside |
| `customAnimationsEnabled` | `Boolean` | `true` | Enables custom animations (extensible) |

### Styling Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `titleTextAppearance` | `@StyleRes Int?` | `TextSemiBold_Heading1` | Text style for title |
| `titleTextColor` | `@ColorRes Int?` | `color000` | Text color for title |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `snapPoints` | `IntArray` | `intArrayOf()` | Array of snap point heights in pixels |
| `delegate` | `BottomTrayDelegate?` | `null` | Callback delegate for lifecycle events |

### Dimension Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| Corner Radius | `Float` | `16dp` | Top corner radius |
| Shadow Elevation | `Float` | `8dp` | Elevation when shadow enabled |
| Stroke Width | `Float` | `1dp` | Border stroke width |
| Shadow Padding | `Int` | `8dp` | Top padding for shadow space |

## Methods Reference

### Configuration Methods

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setTitle()` | `text: String` | Sets the title text |
| `setTitle()` | `resId: Int` | Sets title from string resource |
| `getTitle()` | - | Returns current title text |
| `setTrayContentView()` | `view: View` | Sets the main content view |
| `getTrayContentView()` | - | Returns the content container FrameLayout |
| `setDragHandleVisible()` | `visible: Boolean` | Shows/hides drag handle |
| `isDragHandleVisible()` | - | Returns drag handle visibility |
| `setFooterVisible()` | `visible: Boolean` | Shows/hides footer |
| `isFooterVisible()` | - | Returns footer visibility |
| `configureFooter()` | `configure: (Footer) -> Unit` | Configures footer with lambda |
| `setShadowEnabled()` | `enabled: Boolean` | Enables/disables shadow |
| `setStrokeEnabled()` | `enabled: Boolean` | Enables/disables stroke |
| `getBottomSheetBehavior()` | - | Returns BottomSheetBehavior instance |

### Notification Methods (Delegate)

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `notifyOnShow()` | `dialogInterface: DialogInterface` | Notifies delegate of show event |
| `notifyOnDismiss()` | `dialogInterface: DialogInterface` | Notifies delegate of dismiss event |
| `notifyOnStateChanged()` | `newState: Int` | Notifies delegate of state change |
| `notifyOnSlide()` | `slideOffset: Float` | Notifies delegate of slide event |

### Factory Methods

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `newInstance()` | - | Creates default instance |
| `newInstance()` | `title, showDragHandle, showFooter, hasShadow, hasStroke` | Creates configured instance |

## BottomTrayDelegate Interface

Implement this interface to receive lifecycle callbacks:

```kotlin
interface BottomTrayDelegate {
    fun onShow(dialogInterface: DialogInterface) {}
    fun onDismiss(dialogInterface: DialogInterface) {}
    fun onStateChanged(bottomSheet: View, newState: Int) {}
    fun onSlide(bottomSheet: View, slideOffset: Float) {}
}
```

## Usage Examples

### Example 1: Simple Bottom Sheet

```kotlin
val bottomTray = BottomTray.newInstance(title = "Select an Option")

val contentView = layoutInflater.inflate(R.layout.options_list, null)
bottomTray.setTrayContentView(contentView)

bottomTray.show(supportFragmentManager, "BottomTray")
```

### Example 2: With Footer Configuration

```kotlin
val bottomTray = BottomTray.newInstance(
    title = "Confirm Action",
    showFooter = true
)

val contentView = layoutInflater.inflate(R.layout.confirmation_content, null)
bottomTray.setTrayContentView(contentView)

bottomTray.configureFooter { footer ->
    footer.setPrimaryButtonText("Confirm")
    footer.setSecondaryButtonText("Cancel")
    footer.setPrimaryButtonClickListener {
        // Handle confirm action
        bottomTray.dismiss()
    }
    footer.setSecondaryButtonClickListener {
        bottomTray.dismiss()
    }
}

bottomTray.show(supportFragmentManager, "ConfirmTray")
```

### Example 3: With Delegate Callbacks

```kotlin
val bottomTray = BottomTray.newInstance(title = "Interactive Sheet")

bottomTray.delegate = object : BottomTrayDelegate {
    override fun onShow(dialogInterface: DialogInterface) {
        Log.d("BottomTray", "Sheet shown")
    }
    
    override fun onStateChanged(bottomSheet: View, newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                Log.d("BottomTray", "Fully expanded")
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                Log.d("BottomTray", "Collapsed")
            }
        }
    }
    
    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        // Handle slide animation
        // slideOffset: -1 to 1 (collapsed to expanded)
    }
}

val contentView = layoutInflater.inflate(R.layout.content, null)
bottomTray.setTrayContentView(contentView)
bottomTray.show(supportFragmentManager, "InteractiveTray")
```

### Example 4: Non-Draggable Sheet

```kotlin
val bottomTray = BottomTray.newInstance(
    title = "Read Only",
    showDragHandle = false
)

bottomTray.isCancelableOnTouchOutside = false

val contentView = layoutInflater.inflate(R.layout.info_content, null)
bottomTray.setTrayContentView(contentView)

bottomTray.show(supportFragmentManager, "ReadOnlyTray")
```

### Example 5: Programmatic Control

```kotlin
val bottomTray = BottomTray.newInstance(title = "Controlled Sheet")

val contentView = layoutInflater.inflate(R.layout.content, null)
bottomTray.setTrayContentView(contentView)

bottomTray.show(supportFragmentManager, "ControlledTray")

// Control behavior programmatically
bottomTray.getBottomSheetBehavior()?.apply {
    state = BottomSheetBehavior.STATE_EXPANDED
    isHideable = false
    isDraggable = true
}
```

## Customization Examples

### Dynamic Title Update

```kotlin
// Update title after creation
bottomTray.setTitle("Updated Title")
bottomTray.setTitle(R.string.new_title)
```

## Performance Considerations

- **Drawable Caching**: Background and drag handle drawables are cached to prevent redundant creation
- **Color Caching**: Resolved theme colors are cached in a map
- **View Recycling**: Properly cleans up resources in `onDestroyView()`
- **Window Insets**: Efficiently handles keyboard and system bar insets
- **Edge-to-Edge**: Optimized for immersive display with proper inset handling
- **Memory Management**: Nullifies binding and clears caches on destroy

## Keyboard Handling

The BottomTray automatically handles keyboard visibility:

### Features
- **Footer Translation**: Footer moves up with keyboard
- **Content Padding**: Adds padding to scrollable content when keyboard appears
- **Auto-Scroll**: Automatically scrolls to focused EditText fields
- **ScrollView Detection**: Handles both `ScrollView` and `NestedScrollView`

### Best Practices for Keyboard Handling
- Wrap form content in `ScrollView` or `NestedScrollView`
- Use `clipToPadding = false` is handled automatically
- Focused field scrolls to upper 1/4 of visible area

## Edge-to-Edge Support

The component fully supports edge-to-edge display:

- Transparent status and navigation bars
- Automatic system bar inset handling
- Light appearance for status and navigation bars
- Soft input mode: `SOFT_INPUT_ADJUST_RESIZE`
- Proper padding for system gesture areas

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Wrap scrollable content in ScrollView/NestedScrollView | Use fixed height content exceeding screen size |
| Use `configureFooter()` for footer setup | Directly access footer binding |
| Set content view before showing | Modify content after showing without updates |
| Use snap points for multi-height sheets | Set snap points after showing |
| Implement BottomTrayDelegate for callbacks | Poll for state changes |
| Clean up delegates in onDestroy | Leave strong references |
| Use `newInstance()` factory methods | Directly instantiate with constructor args |
| Test with keyboard-visible scenarios | Ignore keyboard handling |

## Required Resources

### Layout
- `bottom_tray.xml` (BottomTrayBinding): Main layout containing:
  - `trayDragHandle`: Drag handle view
  - `trayTitle`: Title TextView
  - `trayContent`: Content container (FrameLayout)
  - `trayFooter`: Footer component

### Theme
- `ThemeOverlay_DesklabV3_UIKit_BottomSheetDialog`: Custom theme for bottom sheet dialog

### Styles
- `TextSemiBold_Heading1`: Default title text appearance

### Theme Attributes
- `colorBackgroundPrimary`: Background color
- `colorForegroundTertiary`: Drag handle color
- `colorShadowNeutralKey`: Shadow color
- `colorStrokeSubtle`: Stroke border color

### Dimensions
- `stroke_weight_1dp`: Stroke width
- 16dp: Corner radius 
- 8dp: Shadow elevation 

## Common Use Cases

### Form Input
```kotlin
val bottomTray = BottomTray.newInstance(
    title = "Add New Item",
    showFooter = true
)
// Add form fields in ScrollView
```

### Selection List
```kotlin
val bottomTray = BottomTray.newInstance(
    title = "Choose Category",
    showDragHandle = true
)
// Add RecyclerView with options
```

### Multi-Step Flow
```kotlin
val bottomTray = BottomTray.newInstance(title = "Step 1 of 3")
// Use setTitle() to update title as user progresses
```

### Information Display
```kotlin
val bottomTray = BottomTray.newInstance(
    title = "Details",
    showFooter = false
)
// Add detailed information content
```

## Architecture Notes

### Lifecycle Management
- Extends `BottomSheetDialogFragment` for proper lifecycle
- Handles configuration changes automatically
- Cleans up resources in `onDestroyView()`

### Window Insets Handling
- Uses `WindowInsetsCompat` for compatibility
- Handles IME (keyboard) insets
- Handles system bar insets (status/navigation)
- Supports edge-to-edge display

### Material Design Compliance
- Uses `MaterialShapeDrawable` for backgrounds
- Implements Material Design motion patterns
- Supports Material Design elevation/shadows
- Compatible with Material themes

---

> **⚠️ Note**: This component requires Material Components library and proper theme configuration. Ensure `BottomTrayBinding` is generated from your layout XML with the correct view IDs: `trayDragHandle`, `trayTitle`, `trayContent`, and `trayFooter`. The Footer component must be a custom view with appropriate configuration methods.