# Dropdown Filter Horizontal
The `DropdownFilterHorizontal` is a custom Android view component designed to function as a compact, clickable filter button. It provides a clear and intuitive way for users to see the currently selected filter value and tap to open a more detailed selection interface, such as a bottom sheet or a dialog.

Built on `MaterialCardView`, it is styled as a modern, pill-shaped chip with a title, an optional description, and a chevron icon to indicate interactivity.

## Visual Anatomy
The component is composed of several key elements laid out horizontally.

![Dropdown Filter Horizontal](https://res.cloudinary.com/fauzanspratama/image/upload/v1759227687/Dropdown_Filter_Horizontal_bmyhjt.png)

| Element         | View ID              | Description                                                                                                                         |
| :-------------- | :------------------- | :---------------------------------------------------------------------------------------------------------------------------------- |
| **Container**   | (root)               | The root `MaterialCardView` which provides the pill shape, stroke, elevation, and ripple effect.                                    |
| **Title**       | `tvTitleLabel`       | A `MaterialTextView` that displays the primary filter value (e.g., "Pekan 5").                                                      |
| **Separator**   | `tvDotSpacer`        | An `ImageView` shaped as a dot. It visually separates the title and description and is only visible when a description is provided. |
| **Description** | `tvDescriptionLabel` | A `MaterialTextView` for optional, secondary information (e.g., "26 Jan - 1 Feb").                                                  |
| **Indicator**   | `ivChevronDown`      | An `ImageView` with a chevron icon that signals to the user that the component is clickable.                                        |

## Key Features
- **Dual-Text Display**: Capable of showing both a primary `title` and an optional secondary `description` to provide clear context for the current filter selection.
- **Smart Separator**: The dot separator automatically shows and hides itself based on whether the `description` text is present, ensuring a clean UI.
- **Delegate Pattern**: Utilizes the `DropdownFilterHorizontalDelegate` interface for clean and decoupled handling of click events.
- **Material Design Styling**: Comes pre-styled as a rounded, pill-shaped card with a subtle stroke, elevation, and a built-in ripple effect for user feedback, aligning with modern UI standards.
- **Flexible Configuration**: Can be fully configured within XML layouts or created and managed programmatically in Kotlin code.

## XML Implementation
Add the `DropdownFilterHorizontal` to any layout file and configure its initial text using the custom `app` attributes.

```XML
<com.edts.components.dropdown.filter.DropdownFilterHorizontal
    android:id="@+id/date_filter"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dropdownTitle="Pekan 5"
    app:dropdownDescription="26 Jan - 1 Feb" />

<com.edts.components.dropdown.filter.DropdownFilterHorizontal
    android:id="@+id/category_filter"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="16dp"
    app:dropdownTitle="Semua Kategori" />
```

#### XML Attributes
These attributes are defined in `attrs_dropdown_filter_horizontal.xml`.

| Attribute             | Format   | Description                                                                                                       |
| :-------------------- | :------- | :---------------------------------------------------------------------------------------------------------------- |
| `dropdownTitle`       | `string` | Sets the primary text displayed on the left side of the component.                                                |
| `dropdownDescription` | `string` | Sets the optional secondary text. If this is null or empty, the description and the dot separator will be hidden. |

## Kotlin Implementation
The component can be easily controlled in your Kotlin code, allowing for dynamic updates to its content and handling of user interactions.

#### Configuration
You can set the title and description at runtime. The component's UI will update automatically.

```Kotlin
// Find the view in the layout
val dateFilter = findViewById<DropdownFilterHorizontal>(R.id.date_filter)

// Update the title and description dynamically
dateFilter.title = "Bulanan"
dateFilter.description = "September 2025"

// To hide the description and the dot separator, set the description to null or empty
dateFilter.description = null
```

#### Handling Events
To respond to user taps, implement the `DropdownFilterHorizontalDelegate` interface and assign it to the component. The primary use case is to show a selection dialog or bottom sheet.

```Kotlin
// In your Activity or Fragment
val dateFilter = findViewById<DropdownFilterHorizontal>(R.id.date_filter)

dateFilter.dropdownFilterHorizontalDelegate = object : DropdownFilterHorizontalDelegate {
    override fun onClick(dropdown: DropdownFilterHorizontal) {
        // Best practice: Show a BottomSheet or DialogFragment for filter options
        showDatePickerBottomSheet()
    }
}

private fun showDatePickerBottomSheet() {
    // ... logic to display your filter selection UI ...
}
```

