# Dropdown Filter Horizontal

The `DropdownFilterHorizontal` is a custom Android view component built on `MaterialCardView` that functions as a compact, clickable filter button. It displays a primary title with an optional description and provides visual feedback when interacted with.

## Visual Anatomy

![Dropdown Filter Horizontal](https://res.cloudinary.com/fauzanspratama/image/upload/v1759299514/Dropdown_Filter_Horizontal_bmyhjt.png)

| Element         | View ID              | Description                                                                 |
| :-------------- | :------------------- | :-------------------------------------------------------------------------- |
| **Container**   | (root)               | `MaterialCardView` with pill shape, stroke, elevation, and ripple effect    |
| **Title**       | `tvTitleLabel`       | Primary filter value (e.g., "Pekan 5")                                      |
| **Separator**   | `tvDotSpacer`        | Dot-shaped separator, visible only when description is provided             |
| **Description** | `tvDescriptionLabel` | Optional secondary information (e.g., "26 Jan - 1 Feb")                     |
| **Indicator**   | `ivChevronDown`      | Chevron icon indicating interactivity                                       |

## Key Features
- **Dual-Text Display**: Primary title with optional description
- **Smart Separator**: Dot separator auto-shows/hides based on description content
- **Delegate Pattern**: Clean click event handling via `DropdownFilterHorizontalDelegate`
- **Material Design**: Pill-shaped card with stroke, elevation, and ripple effect
- **Flexible Configuration**: XML attributes or programmatic Kotlin setup

## XML Implementation

```xml
<com.edts.components.dropdown.filter.DropdownFilterHorizontal
    android:id="@+id/weekly_filter"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dropdownTitle="Pekan 5"
    app:dropdownDescription="26 Jan - 1 Feb" />
```

#### XML Attributes
| Attribute             | Format   | Description                                                                 |
| :-------------------- | :------- | :-------------------------------------------------------------------------- |
| `dropdownTitle`       | `string` | Primary text displayed on the left side                                     |
| `dropdownDescription` | `string` | Optional secondary text (hides dot separator when empty)                    |

## Kotlin Implementation

#### Basic Configuration
```kotlin
val weeklyFilter = findViewById<DropdownFilterHorizontal>(R.id.date_filter)
weeklyFilter.title = "Pekan 5"
weeklyFilter.description = "26 Jan - 1 Feb"

// Hide description and separator
weeklyFilter.description = null
```

#### View Binding with Delegate
```kotlin
private lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    setupDropdownFilters()
}

private fun setupDropdownFilters() {
    binding.weeklyFilter.apply {
        title = "Pekan 5"
        description = "26 Jan - 1 Feb"
        dropdownFilterHorizontalDelegate = object : DropdownFilterHorizontalDelegate {
            override fun onClick(dropdown: DropdownFilterHorizontal) {
                showDatePickerBottomSheet()
            }
        }
    }
}
```

#### Memory Management
```kotlin
override fun onDestroyView() {
    binding.weeklyFilter.dropdownFilterHorizontalDelegate = null
    super.onDestroyView()
}
```

## Do's and Don'ts

### ✅ Do's
- Use for filter selection scenarios
- Provide clear, concise labels
- Update title/description to reflect current selection
- Maintain adequate spacing between multiple filters
- Clear delegates in `onDestroyView()` to prevent memory leaks
- Test accessibility for screen readers

### ❌ Don'ts
- Use for primary actions like "Submit" or "Save"
- Overload with lengthy text
- Ignore touch target size (minimum 48dp)
- Assign multiple delegate instances
- Block main thread in selection dialogs

## Performance Considerations

- **RecyclerView Usage**: Clear delegates in `onViewRecycled()`
- **Batch Updates**: Use `apply{}` for multiple property changes
- **Efficient Layout**: Uses `wrap_content` without complex measurements
- **Memory Management**: Always nullify delegates in lifecycle methods

## Material Design Styling

The component implements Material Design with:

- **Shape**: Pill-shaped using `radius_999dp` corner radius
- **Elevation**: Subtle 1dp elevation for hierarchy
- **Stroke**: 1dp border using `colorStrokeSubtle` attribute
- **Ripple Effect**: Touch feedback using `colorBackgroundModifierOnPress`
- **Typography**: `l3SemiBold` for title, `l3Regular` for description
- **Colors**: Uses theme attributes for dynamic theming support
- **Icons**: Standard Material chevron-down icon

---

> [!note]
> This component seamlessly integrates with the `MonthlyPicker` component to provide a complete filter selection experience. When users tap the `DropdownFilterHorizontal`, it typically opens a bottom sheet containing a grid of `MonthlyPicker` chips for intuitive month selection, creating a cohesive and user-friendly filtering interface.
