# Monthly Picker
The `MonthlyPicker` is a custom Android view component that functions as a selectable, chip-style button. It is designed specifically for interfaces requiring month selection, such as calendar filters or report configuration screens. Built upon `MaterialCardView`, it offers robust state management and visual feedback for user interactions.

![My Event Card](https://res.cloudinary.com/fauzanspratama/image/upload/v1759223877/Monthly_Picker_vzp76f.png)

### Anatomy
The component consists of two primary elements:
- **Container**: The root `MaterialCardView` which defines the component's shape, background, and stroke.
- **Month Label**: A `MaterialTextView` centered within the container that displays the month's name.

## Key Features
- **Stateful Control**: Manages three distinct states: `SELECTED`, `UNSELECTED`, and `DISABLED`.
- **Interactive Feedback**: Provides visual cues for `ON_PRESS` and `ON_FOCUS` states to enhance user experience and accessibility.
- **Self-Contained Toggle**: Automatically toggles its own state between `SELECTED` and `UNSELECTED` when tapped by the user.
- **Delegate Pattern**: Uses the `MonthlyPickerDelegate` interface to notify a listener of click events after its internal state has been updated.
- **Flexible Configuration**: Can be fully configured in layout XML files or created and controlled programmatically in Kotlin.
- **Theme-Aware Styling**: Leverages theme attributes for its colors, ensuring visual consistency with the application's theme.

---

## Usage and Implementation

### XML Usage
The component can be added directly to layout files.

```XML
<com.edts.components.dropdown.filter.MonthlyPicker
    android:id="@+id/januaryPicker"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:monthLabel="Jan"
    app:pickerType="selected"/>
```

#### XML Attributes
These attributes are defined in `attrs_monthly_picker.xml`.

| Attribute | Format | Description |
| :--- | :--- | :--- |
| `monthLabel` | `string` | Sets the text displayed inside the picker. |
| `pickerType` | `enum` | Sets the initial state of the picker. Values: `unselected` (default), `selected`, `disabled`. |

### Programmatic (Kotlin)
It can also be instantiated directly in Kotlin code.

```Kotlin
val monthlyPicker = MonthlyPicker(requireContext())
```

##### Configuration
**Setting the Label**: Use the `setMonthLabel` method to define the display text.
```Kotlin
monthlyPicker.setMonthLabel("Jan")
```

**Managing State**: The `type` property gets or sets the component's state. Modifying this property immediately updates its appearance.
```Kotlin
// Set the picker to its selected state
monthlyPicker.type = MonthlyPicker.PickerType.SELECTED

// Set the picker to its unselected state
monthlyPicker.type = MonthlyPicker.PickerType.UNSELECTED
```

#### Handling Events
To respond to user taps, assign an object that implements the `MonthlyPickerDelegate` interface to the picker's `delegate` property. The `onMonthClicked` method is called after the picker has already toggled its own state.

```Kotlin
monthlyPicker.delegate = object : MonthlyPickerDelegate {
    override fun onMonthClicked(picker: MonthlyPicker) {
        // The picker's state has already been updated.
        // You can now react to the new state.
        Log.d("MonthlyPicker", "A month was clicked! Its new state is: ${picker.type}")
    }
}
```

---
## Advanced Example: Dynamic Grid in a Bottom Sheet
A best practice for using the `MonthlyPicker` is to create a dynamic and reusable month selection interface within a bottom sheet. This allows users to easily select a month and year without leaving the current screen. This guide outlines how to build this feature programmatically.

![Monthly Picker | 256](https://res.cloudinary.com/fauzanspratama/image/upload/v1759226356/Monthly_Picker_emkoi0.gif)
#### Step 1: Manage State in Your Fragment or Activity
Before building the UI, define the necessary state variables in your hosting Fragment or Activity. This includes the list of month names and variables to track the user's selection.

```Kotlin
// State variables to be managed by the host (e.g., a Fragment)
private var selectedMonthIndex: Int = 8 // September (0-indexed)
private var selectedYear: Int = 2025

// A constant list of month names for creating the pickers
private val monthNames = listOf(
    "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
    "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
)
```

#### Step 2: Build the Bottom Sheet Layout
For maximum flexibility, create the bottom sheet's layout programmatically. The layout should consist of a year navigator and a grid for the `MonthlyPicker` components.

```Kotlin
// In a function that shows the bottom sheet
fun showMonthPickerBottomSheet() {
    val bottomSheet = BottomTray.newInstance(title = "Pilih Bulan")
    var currentYearInPicker = selectedYear // Local state for the picker UI

    // 1. Create the main container
    val container = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.VERTICAL
        // ... add layout params and padding ...
    }

    // 2. Create the year navigator header
    val yearHeader = createYearNavigator { newYear ->
        currentYearInPicker = newYear
        // This function will be defined in the next step
        populateMonthGrid(monthChipsContainer, currentYearInPicker)
    }

    // 3. Create the grid for the month pickers
    val monthChipsContainer = GridLayout(requireContext()).apply {
        columnCount = 3
        // ... add layout params ...
    }

    // 4. Populate the grid with the initial state
    populateMonthGrid(monthChipsContainer, currentYearInPicker)

    // 5. Assemble the layout and show the bottom sheet
    container.addView(yearHeader)
    container.addView(monthChipsContainer)
    bottomSheet.setTrayContentView(container)
    bottomSheet.show(parentFragmentManager, "MonthPicker")
}
```

#### Step 3: Populate the Grid with `MonthlyPicker` Components
Create a dedicated function to populate the `GridLayout` with `MonthlyPicker` instances. This function should clear any existing pickers and generate new ones based on the selected year, ensuring the UI is always in sync.

```Kotlin
private fun populateMonthGrid(grid: GridLayout, year: Int) {
    grid.removeAllViews() // Clear previous month pickers

    monthNames.forEachIndexed { index, monthName ->
        val monthlyPicker = MonthlyPicker(requireContext()).apply {
            setMonthLabel(monthName) //
            
            // Set the state based on the current selection for that year
            type = if (index == selectedMonthIndex && year == selectedYear) {
                MonthlyPicker.PickerType.SELECTED //
            } else {
                MonthlyPicker.PickerType.UNSELECTED //
            }
            
            layoutParams = GridLayout.LayoutParams().apply {
                // ... set margins and weight for proper grid spacing ...
            }
            
            // Set the delegate to handle user interaction
            delegate = createMonthPickerDelegate(index, year) //
        }
        grid.addView(monthlyPicker)
    }
}
```

#### Step 4: Handle Selection and Update State
Implement the `MonthlyPickerDelegate` to capture user taps. When a month is clicked, update your fragment's state variables, refresh the main UI, and dismiss the bottom sheet.

```Kotlin
private fun createMonthPickerDelegate(monthIndex: Int, year: Int): MonthlyPickerDelegate {
    return object : MonthlyPickerDelegate {
        override fun onMonthClicked(picker: MonthlyPicker) {
            // 1. Update the stored state
            selectedMonthIndex = monthIndex
            selectedYear = year

            // 2. Refresh the app's main display to show the new selection
            updateFilterDisplay() 

            // 3. Dismiss the bottom sheet
            (parentFragmentManager.findFragmentByTag("MonthPicker") as? BottomTray)?.dismiss()
        }
    }
}
```

