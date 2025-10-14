# Monthly Picker

A selectable chip-style button component for month selection interfaces, built on `MaterialCardView` with robust state management and visual feedback.

## Visual Anatomy

![Monthly Picker](https://res.cloudinary.com/fauzanspratama/image/upload/v1759299591/Monthly_Picker_vzp76f.png)

| Element       | Description                                      |
| :------------ | :----------------------------------------------- |
| **Container** | `MaterialCardView` defining shape and background |
| **Month Label** | Centered `MaterialTextView` displaying month name |

## Key Features
- **Three States**: `SELECTED`, `UNSELECTED`, and `DISABLED` with automatic visual updates
- **Interactive Feedback**: Visual cues for `ON_PRESS` and `ON_FOCUS` states
- **Self-Contained Toggle**: Automatically toggles between `SELECTED` and `UNSELECTED` on tap
- **Delegate Pattern**: Clean event handling via `MonthlyPickerDelegate` interface
- **Theme-Aware Styling**: Uses theme attributes for consistent theming
- **Performance Optimized**: Color caching and drawable reuse

## XML Implementation

```xml
<com.edts.components.dropdown.filter.MonthlyPicker
    android:id="@+id/januaryPicker"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:monthLabel="Jan"
    app:pickerType="selected"/>
```

#### XML Attributes
| Attribute | Format | Description |
| :--- | :--- | :--- |
| `monthLabel` | `string` | Text displayed inside the picker |
| `pickerType` | `enum` | Initial state: `unselected`, `selected`, `disabled` |

## Kotlin Implementation

#### Basic Configuration
```kotlin
val monthlyPicker = MonthlyPicker(requireContext())

// Set label and state
monthlyPicker.setMonthLabel("Jan")
monthlyPicker.type = MonthlyPicker.PickerType.SELECTED
```

#### Event Handling
```kotlin
monthlyPicker.delegate = object : MonthlyPickerDelegate {
    override fun onMonthClicked(picker: MonthlyPicker) {
        Log.d("MonthlyPicker", "Month clicked! New state: ${picker.type}")
    }
}
```

## Integration with Bottom Sheet

Commonly used in bottom sheets for month selection:

#### Preview
![Monthly Picker Bottom Sheet](https://res.cloudinary.com/fauzanspratama/image/upload/v1759308473/Monthly_Picker_emkoi0.gif)

```kotlin
private fun showMonthPickerBottomSheet() {
    val bottomSheet = BottomTray.newInstance(title = "Pilih Bulan")
    
    val container = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.VERTICAL
    }

    val monthGrid = GridLayout(requireContext()).apply {
        columnCount = 3
    }

    populateMonthGrid(monthGrid, selectedYear)
    
    container.addView(monthGrid)
    bottomSheet.setTrayContentView(container)
    bottomSheet.show(parentFragmentManager, "MonthPicker")
}

private fun populateMonthGrid(grid: GridLayout, year: Int) {
    grid.removeAllViews()

    monthNames.forEachIndexed { index, monthName ->
        val monthlyPicker = MonthlyPicker(requireContext()).apply {
            setMonthLabel(monthName)
            type = if (index == selectedMonthIndex && year == selectedYear) {
                MonthlyPicker.PickerType.SELECTED
            } else {
                MonthlyPicker.PickerType.UNSELECTED
            }
            
            delegate = object : MonthlyPickerDelegate {
                override fun onMonthClicked(picker: MonthlyPicker) {
                    selectedMonthIndex = index
                    selectedYear = year
                    updateFilterDisplay()
                    (parentFragmentManager.findFragmentByTag("MonthPicker") as? BottomTray)?.dismiss()
                }
            }
        }
        grid.addView(monthlyPicker)
    }
}
```

#### Memory Management
```kotlin
override fun onDestroyView() {
    // Clear delegates to prevent memory leaks
    monthlyPicker.delegate = null
    super.onDestroyView()
}
```

## Do's and Don'ts

### ✅ Do's
- Use for month selection in filters, calendars, and reports
- Group in grids (3x4 layout) for complete month selection
- Update state programmatically when selection changes
- Provide clear visual feedback for all states
- Use consistent month abbreviations (Jan, Feb, etc.)
- Test accessibility with screen readers

### ❌ Don'ts
- Use for non-month related selections
- Mix with other types of pickers in the same grid
- Override the built-in toggle behavior unnecessarily
- Use lengthy text labels that break the chip layout
- Ignore disabled state for unavailable months
- Create grids with inconsistent column counts

## Performance Considerations

- **Color Caching**: Preloads and caches theme colors for efficient reuse
- **Drawable Reuse**: Caches background drawables by state and style
- **Efficient Layout**: Uses fixed dimensions without complex measurements
- **Memory Management**: Clear delegates in lifecycle methods

```kotlin
// Color caching implementation in component
private fun preloadColors() {
    val colorAttrs = mapOf(
        R.attr.colorForegroundPrimary to R.color.color000,
        R.attr.colorForegroundPrimaryInverse to android.R.color.white,
        // ... more color mappings
    )
    
    colorAttrs.forEach { (attr, fallback) ->
        colorCache[attr] = resolveColorAttribute(attr, fallback)
    }
}
```

## Material Design Styling

The component implements Material Design with:

- **Shape**: 12dp corner radius for consistent rounded appearance
- **States**: Clear visual differentiation between selected/unselected/disabled
- **Interaction**: Press and focus states with visual feedback
- **Typography**: `TextMedium_Label2` style for clear readability
- **Colors**: Uses theme attributes for dynamic theming:
  - `colorForegroundPrimary` - Unselected text
  - `colorForegroundPrimaryInverse` - Selected text  
  - `colorBackgroundAccentPrimaryIntense` - Selected background
  - `colorStrokeSubtle` - Unselected border
  - `colorBackgroundModifierOnPress` - Press state overlay

## State Management

```kotlin
// Programmatic state control
monthlyPicker.type = MonthlyPicker.PickerType.SELECTED

// State-based visual feedback automatically applied:
// - SELECTED: Filled background with inverse text color
// - UNSELECTED: Transparent with subtle border  
// - DISABLED: Grayed out with disabled interactions
```

---

> [!note]
> This component works seamlessly with `DropdownFilterHorizontal` to create complete filtering interfaces. Typically, a `DropdownFilterHorizontal` triggers a bottom sheet containing a grid of `MonthlyPicker` components for intuitive month selection.
