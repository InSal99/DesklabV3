# Radio Button

| Feature / Variation | Preview |
| ------------------- | ------- |
| **RadioButton - Normal** | Unchecked radio button with normal text |
| **RadioButton - Selected** | Checked with animated inner circle |
| **RadioButton - Error** | Red indicator with error styling |
| **RadioGroup** | Container managing multiple RadioButton selections |

## Overview

**Radio Button** is a custom radio button component that extends AppCompatRadioButton with enhanced animations, state management, and text appearance customization. It features a smooth scale animation for the inner circle when toggling. For the enhanced functionality for dynamic lists and type-safe data handling, Radio Group is provided as a container component that manages RadioButton instances, providing data binding, error state management, and selection callbacks.

## Basic Usage

### RadioButton - Standalone

#### 1. Add to Layout

```xml
<com.edts.components.radiobutton.RadioButton
    android:id="@+id/radioButton"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Option 1"
    app:isRadioError="false" />
```

#### 2. Initialize in Code

```kotlin
val radioButton = findViewById<RadioButton>(R.id.radioButton)

radioButton.text = "Male"
radioButton.isChecked = true

// Using delegate pattern
radioButton.radioChangedDelegate = object : RadioButtonDelegate {
    override fun onCheckChanged(radioButton: RadioButton, isChecked: Boolean) {
        Log.d("Radio", "Button ${radioButton.text} is now: $isChecked")
    }
}
```

### RadioGroup - Recommended Usage

#### 1. Add to Layout

```xml
<com.edts.components.radiobutton.RadioGroup
    android:id="@+id/radioGroup"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:radioButtonSpacing="8dp"
    app:radioNormalTextAppearance="@style/RadioTextAppearance_Normal"
    app:radioSelectedTextAppearance="@style/RadioTextAppearance_Selected" />
```

#### 2. Initialize with Data

```kotlin
val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

// Simple string list
val options = listOf("Male", "Female", "Other")
radioGroup.setData(options) { it }

// Complex data objects
data class Country(val code: String, val name: String)
val countries = listOf(
    Country("ID", "Indonesia"),
    Country("MY", "Malaysia"),
    Country("SG", "Singapore")
)
radioGroup.setData(countries) { it.name }

// Handle selection
radioGroup.setOnItemSelectedListener(object : RadioGroupDelegate {
    override fun onItemSelected(position: Int, data: Any?) {
        val selected = data as? Country
        Log.d("RadioGroup", "Selected: ${selected?.name}")
    }
})
```

## Properties Reference

### RadioButton Properties

#### XML Attributes

| Attribute Name | Type | Default | Description |
| -------------- | ---- | ------- | ----------- |
| `isRadioError` | `Boolean` | `false` | Whether radio button is in error state |

#### Runtime Properties

| Property Name | Type | Description |
| ------------- | ---- | ----------- |
| `radioChangedDelegate` | `RadioButtonDelegate?` | Delegate for check change events |
| `isErrorState` | `Boolean` | Error state flag (read via `isErrorState()`) |

### RadioGroup Properties

#### XML Attributes

| Attribute Name | Type | Default | Description |
| -------------- | ---- | ------- | ----------- |
| `radioButtonSpacing` | `Dimension` | `8dp` | Vertical spacing between radio buttons |
| `radioCompoundDrawablePadding` | `Dimension` | `8dp` | Padding between button and text |
| `radioNormalTextAppearance` | `@StyleRes` | `RadioTextAppearance_Normal` | Text style for unchecked state |
| `radioSelectedTextAppearance` | `@StyleRes` | `RadioTextAppearance_Selected` | Text style for checked state |
| `radioDisabledTextAppearance` | `@StyleRes` | `RadioTextAppearance_Disabled` | Text style for disabled unchecked |
| `radioDisabledSelectedTextAppearance` | `@StyleRes` | `RadioTextAppearance_DisabledSelected` | Text style for disabled checked |
| `radioErrorTextAppearance` | `@StyleRes` | `RadioTextAppearance_Normal` | Text style for error state |

#### Runtime Properties

| Property Name | Type | Description |
| ------------- | ---- | ----------- |
| `buttonSpacing` | `Int` | Vertical spacing in pixels (updates margins dynamically) |
| `radioGroupDelegate` | `RadioGroupDelegate?` | Selection event listener |

## Delegate Interfaces

### RadioButtonDelegate

```kotlin
interface RadioButtonDelegate {
    fun onCheckChanged(radioButton: RadioButton, isChecked: Boolean)
}
```

### RadioGroupDelegate

```kotlin
interface RadioGroupDelegate {
    fun onItemSelected(position: Int, data: Any?)
}
```

## Methods Reference

### RadioButton Methods

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setErrorState()` | `error: Boolean` | Set error state on/off |
| `isErrorState()` | - | Check if in error state |
| `setTextAppearances()` | `normal, selected, disabled, disabledSelected, error: Int` | Customize all text appearances |
| `setChecked()` | `checked: Boolean` | Set checked state with animation |
| `setEnabled()` | `enabled: Boolean` | Enable/disable the radio button |
| `performClick()` | - | Programmatically trigger click |

### RadioGroup Methods

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `setData()` | `dataList: List<T>, itemDisplayProvider: (T) -> String` | Populate group with data |
| `setOnItemSelectedListener()` | `listener: RadioGroupDelegate` | Set selection callback |
| `getSelectedData()` | - | Get selected item data (returns `Any?`) |
| `getSelectedPosition()` | - | Get selected item position (returns `Int`) |
| `selectItem()` | `position: Int` | Select item by position |
| `selectItemByData()` | `data: Any` | Select item by matching data |
| `setErrorStateOnAll()` | `error: Boolean` | Set error state on all buttons |
| `setErrorStateOnPosition()` | `position: Int, error: Boolean` | Set error state on specific position |
| `setErrorStateOnData()` | `data: Any, error: Boolean` | Set error state on specific data item |
| `clearAllErrorStates()` | - | Clear all error states |

## Animation Details

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| Inner Circle Scale | `200ms` | `DecelerateInterpolator` | Animates inner circle from 0 to 1 scale when checking |

### Animation Behavior

- **Check Animation**: Inner circle scales from 0% to 100% with deceleration
- **Uncheck Animation**: Inner circle scales from 100% to 0% with deceleration
- **Cancellation**: Previous animation is cancelled when state changes rapidly
- **Cleanup**: Animation is properly cancelled on view detachment

## Usage Examples

### Basic RadioGroup with Strings

```kotlin
val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)

genderGroup.setData(listOf("Male", "Female", "Other")) { it }

genderGroup.setOnItemSelectedListener(object : RadioGroupDelegate {
    override fun onItemSelected(position: Int, data: Any?) {
        val gender = data as? String
        Log.d("Gender", "Selected: $gender")
    }
})
```

### RadioGroup with Data Objects

```kotlin
data class PaymentMethod(val id: String, val name: String, val icon: Int)

val paymentMethods = listOf(
    PaymentMethod("cc", "Credit Card", R.drawable.ic_credit_card),
    PaymentMethod("bank", "Bank Transfer", R.drawable.ic_bank),
    PaymentMethod("ewallet", "E-Wallet", R.drawable.ic_wallet)
)

radioGroup.setData(paymentMethods) { it.name }

radioGroup.setOnItemSelectedListener(object : RadioGroupDelegate {
    override fun onItemSelected(position: Int, data: Any?) {
        val method = data as? PaymentMethod
        processPayment(method)
    }
})
```

### Form Validation with Error States

```kotlin
fun validateForm(): Boolean {
    val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
    
    if (genderGroup.getSelectedPosition() == -1) {
        genderGroup.setErrorStateOnAll(true)
        Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
        return false
    }
    
    genderGroup.clearAllErrorStates()
    return true
}

// Clear error on selection
radioGroup.setOnItemSelectedListener(object : RadioGroupDelegate {
    override fun onItemSelected(position: Int, data: Any?) {
        radioGroup.clearAllErrorStates()
    }
})
```

### Programmatic Selection

```kotlin
// Select by position
radioGroup.selectItem(0) // Select first item

// Select by data
val targetCountry = Country("ID", "Indonesia")
radioGroup.selectItemByData(targetCountry)

// Get current selection
val selectedData = radioGroup.getSelectedData()
val selectedPosition = radioGroup.getSelectedPosition()
```

### Custom Text Appearances

```kotlin
// Via XML
<RadioGroup
    app:radioNormalTextAppearance="@style/CustomRadioNormal"
    app:radioSelectedTextAppearance="@style/CustomRadioSelected"
    app:radioDisabledTextAppearance="@style/CustomRadioDisabled" />
```

### Integration with InputField

```kotlin
// RadioGroup is used within InputField component
val inputField = findViewById<InputField>(R.id.genderField)

inputField.configure(
    InputFieldConfig(
        type = InputFieldType.RadioGroup,
        title = "Gender",
        isRequired = true,
        options = listOf("Male", "Female", "Other")
    )
)

// InputField internally creates RadioGroup and manages error states
if (!inputField.isValid()) {
    // RadioGroup will show error state automatically
}
```

### Conditional Error States

```kotlin
// Set error on specific item
radioGroup.setErrorStateOnPosition(2, true) // Error on third option

// Set error based on data
val invalidOption = "Other"
radioGroup.setErrorStateOnData(invalidOption, true)

// Clear specific errors
radioGroup.setErrorStateOnPosition(2, false)
```

## Visual States

| State | Text Appearance | Visual Feedback |
| ----- | --------------- | --------------- |
| Normal (unchecked) | `RadioTextAppearance_Normal` | Empty circle |
| Selected (checked) | `RadioTextAppearance_Selected` | Filled inner circle with animation |
| Disabled (unchecked) | `RadioTextAppearance_Disabled` | Muted colors, non-interactive |
| Disabled (checked) | `RadioTextAppearance_DisabledSelected` | Muted with filled circle |
| Error | `RadioTextAppearance_Normal` | Custom error drawable state |

## State Management

### RadioButton State Priority

The component determines text appearance in this order:
1. **Error state** → `errorTextAppearance`
2. **Disabled + Checked** → `disabledSelectedTextAppearance`
3. **Disabled** → `disabledTextAppearance`
4. **Checked** → `selectedTextAppearance`
5. **Default** → `normalTextAppearance`

### RadioGroup Data Binding

```kotlin
// Internal data mapping
private val radioButtonDataMap = mutableMapOf<Int, Any?>()

// When setData() is called:
// 1. All views are removed
// 2. Data map is cleared
// 3. New RadioButtons are created with generated IDs
// 4. Each RadioButton ID is mapped to its data
// 5. Selection listener updates trigger delegate callbacks
```

## Component Integration

### RadioGroup internally manages RadioButton instances

```kotlin
// When you use RadioGroup, it creates RadioButton instances automatically
radioGroup.setData(options) { it }

// RadioGroup handles:
// - RadioButton creation and configuration
// - Text appearance application
// - Spacing management
// - Error state propagation
// - Selection callbacks
```

### Error State Propagation

```kotlin
// RadioGroup can set error on all RadioButtons
radioGroup.setErrorStateOnAll(true)

// Each RadioButton receives the error state
// This is used by InputField for validation feedback
```


## Performance Considerations

- **View Recycling**: RadioGroup removes and recreates views on data change (not optimal for frequent updates)
- **Animation Management**: Proper cleanup on view detachment prevents memory leaks
- **Data Mapping**: Uses HashMap for O(1) lookup of data by RadioButton ID
- **Conditional Animation**: Only animates when view is visible and has dimensions

## Common Patterns

### Form with Multiple RadioGroups

```kotlin
class FormActivity : AppCompatActivity() {
    private val formFields = mutableMapOf<String, RadioGroup>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val educationGroup = findViewById<RadioGroup>(R.id.educationGroup)
        
        formFields["gender"] = genderGroup
        formFields["education"] = educationGroup
        
        genderGroup.setData(listOf("Male", "Female", "Other")) { it }
        educationGroup.setData(listOf("High School", "Bachelor", "Master", "PhD")) { it }
    }
    
    fun validateForm(): Boolean {
        var isValid = true
        formFields.forEach { (name, group) ->
            if (group.getSelectedPosition() == -1) {
                group.setErrorStateOnAll(true)
                isValid = false
            }
        }
        return isValid
    }
    
    fun getFormData(): Map<String, Any?> {
        return formFields.mapValues { it.value.getSelectedData() }
    }
}
```

### Dynamic Options Loading

```kotlin
lifecycleScope.launch {
    val countries = fetchCountriesFromAPI()
    radioGroup.setData(countries) { it.name }
    
    // Restore previous selection if exists
    savedInstanceState?.getString("selected_country")?.let { countryCode ->
        val country = countries.find { it.code == countryCode }
        country?.let { radioGroup.selectItemByData(it) }
    }
}
```

### Preset Selection

```kotlin
// Load saved preference
val savedGender = preferences.getString("gender", null)
val genderOptions = listOf("Male", "Female", "Other")

radioGroup.setData(genderOptions) { it }

// Select after data is set
savedGender?.let { 
    radioGroup.selectItemByData(it) 
}
```

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use RadioGroup for managing multiple options | Use standalone RadioButton for groups |
| Clear error states on user selection | Leave stale error states |
| Use type-safe data objects with setData() | Rely on string comparisons only |
| Call setData() once with complete list | Add individual RadioButtons manually |
| Use RadioGroupDelegate for selection handling | Mix multiple callback patterns |
| Provide meaningful text via itemDisplayProvider | Use toString() without formatting |
| Test with various data types | Assume all data is String |

## Accessibility Considerations

```kotlin
// RadioButton accessibility
radioButton.contentDescription = "Select ${radioButton.text}"

// Ensure proper touch target (RadioButton handles this internally)
// Minimum height set to 24dp

// RadioGroup provides logical grouping for screen readers
radioGroup.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
```

## Troubleshooting

### Animation Not Working

- Verify drawable is a LayerDrawable with at least 2 layers
- Check that view is visible and has non-zero dimensions
- Ensure `ic_radio_button` drawable is properly structured

### Selection Not Triggering Callback

- Ensure `setOnItemSelectedListener()` is called after `setData()`
- Check that RadioGroupDelegate is properly implemented
- Verify checkedRadioButtonId is updating

### Error State Not Visible

- Confirm error drawable state is defined in theme
- Check that `setErrorState()` is called on RadioButton instances
- Verify error text appearance resources exist

### Data Not Matching After Selection

- Use `getSelectedData()` to retrieve original data object
- Don't rely on text comparison for complex objects
- Cast returned data to expected type safely

---

> **⚠️ Note**: RadioGroup extends the Android RadioGroup and automatically manages RadioButton instances. When using `setData()`, all previous views are removed and recreated. For better performance with large lists or frequent updates, consider using RecyclerView with custom ViewHolders. The animation requires a properly structured LayerDrawable (see Custom Drawable Structure section).