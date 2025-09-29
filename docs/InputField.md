# Input Field

## Overview

InputField is a flexible, customizable form input component that supports multiple input types with built-in validation, error handling, and visual feedback. It provides a unified interface for various input scenarios including text entry, numeric input, dropdown selection, and option groups.

| Feature / Variation | Description | Preview |
| ------------------- | ------- | ------- |
| **Text Input** | Single-line text entry with validation | ![text-input](...) |
| **Number Input** | Numeric keyboard input with validation | ![number-input](...) |
| **Text Area** | Multi-line text input | ![text-area](...) |
| **Dropdown** | Selection from predefined options | ![dropdown](...) |
| **Radio Group** | Single selection from multiple options | ![radio-group](...) |
| **Checkbox Group** | Multiple selection from options | ![checkbox-group](...) |

---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.input.field.InputField
    android:id="@+id/inputField"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:inputType="textArea"
    app:fieldTitle="Full Name"
    app:fieldDescription="Enter your legal name"
    app:fieldHint="John Doe"
    app:fieldRequired="true"
    app:inputType="textInput"
    app:maxLength="50"
    app:minLength="3" />
```

### 2. Initialize in Code

```kotlin
val inputField = findViewById<InputField>(R.id.inputField)

// Configure programmatically
inputField.configure(
    config = InputFieldConfig(
        type = InputFieldType.TextInput,
        title = "Email Address",
        hint = "example@email.com",
        isRequired = true
    ),
    id = "email_field"
)

// Set up delegate
inputField.delegate = object : InputFieldDelegate {
    override fun onValueChange(fieldId: String, value: Any?) {
        // Handle value changes
    }
    
    override fun onValidationChange(fieldId: String, isValid: Boolean) {
        // Handle validation state changes
    }
}
```

## Input Types

| Type | Value | Description | Use Case |
| ---- | ----- | ----------- | -------- |
| `TextInput` | `0` | Single-line text input | Names, emails, short text |
| `NumberInput` | `1` | Numeric keyboard input | Phone numbers, quantities |
| `TextArea` | `2` | Multi-line text input | Comments, descriptions |
| `Dropdown` | `3` | Selection from list | Country, category selection |
| `RadioGroup` | `4` | Single option selection | Gender, agreement choice |
| `CheckboxGroup` | `5` | Multiple option selection | Interests, preferences |

```kotlin
// Set input type via XML
app:inputType="textInput"  // or numberInput, textArea, dropdown, radioGroup, checkboxGroup

// Set input type programmatically
inputField.configure(
    InputFieldConfig(type = InputFieldType.Dropdown, ...)
)
```

## Properties Reference

### XML Attributes

| Attribute Name | Type | Default | Description |
| -------------- | ---- | ------- | ----------- |
| `fieldTitle` | `String` | `""` | Label text displayed above input |
| `fieldDescription` | `String` | `null` | Optional description text |
| `fieldHint` | `String` | `null` | Placeholder text for input |
| `fieldRequired` | `Boolean` | `false` | Whether field is mandatory |
| `fieldEnabled` | `Boolean` | `true` | Whether field is enabled |
| `inputType` | `Enum` | `textInput` | Type of input field |
| `maxLength` | `Int` | `0` | Maximum character/digit limit |
| `minLength` | `Int` | `0` | Minimum character/digit requirement |
| `maxLines` | `Int` | `4` | Maximum lines for TextArea |
| `minLines` | `Int` | `3` | Minimum lines for TextArea |
| `options` | `String[]` | `null` | Options array for dropdown/radio/checkbox |
| `supportingText` | `String` | `null` | Helper text below input |
| `errorText` | `String` | `null` | Error message to display |
| `showError` | `Boolean` | `false` | Whether to show error on init |
| `requiredColor` | `@ColorRes` | `colorRed30` | Color for required indicator |
| `errorColor` | `@ColorRes` | `colorRed50` | Color for error messages |
| `titleTextAppearance` | `@StyleRes` | - | Custom text appearance for title |
| `descriptionTextAppearance` | `@StyleRes` | - | Custom text appearance for description |

### Color Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `requiredColor` | `@ColorInt` | `colorRed30` | Asterisk color for required fields |
| `errorColor` | `@ColorInt` | `colorRed50` | Error text and border color |

Theme colors used internally:
- `colorForegroundPrimary` - Main text color
- `colorForegroundSecondary` - Label text color
- `colorForegroundPlaceholder` - Hint text color
- `colorForegroundDisabled` - Disabled state color
- `colorStrokeSubtle` - Default border color
- `colorStrokeAccent` - Focused border color
- `colorStrokeAttentionIntense` - Error border color

## Data Models

### InputFieldConfig

| Property | Type | Required | Description |
| -------- | ---- | -------- | ----------- |
| `type` | `InputFieldType` | ✅ | Type of input field |
| `title` | `String` | ✅ | Field label text |
| `description` | `String?` | ❌ | Optional description |
| `hint` | `String?` | ❌ | Placeholder text |
| `isRequired` | `Boolean` | ❌ | Required field flag (default: false) |
| `options` | `List<String>?` | ❌ | Options for dropdown/radio/checkbox |
| `maxLines` | `Int` | ❌ | Max lines for TextArea (default: 4) |
| `minLines` | `Int` | ❌ | Min lines for TextArea (default: 3) |
| `maxLength` | `Int` | ❌ | Max character limit (default: 0) |
| `minLength` | `Int` | ❌ | Min character requirement (default: 0) |

```kotlin
val config = InputFieldConfig(
    type = InputFieldType.TextInput,
    title = "Username",
    hint = "Enter username",
    isRequired = true,
    minLength = 3,
    maxLength = 20
)
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `configure()` | `config: InputFieldConfig, id: String = ""` | Configure field with config object |
| `getValue()` | - | Get current field value (returns `Any?`) |
| `setValue()` | `value: Any?` | Set field value programmatically |
| `setError()` | `errorText: String?` | Display error message |
| `clearError()` | - | Clear error state |
| `getErrorText()` | - | Get current error text |
| `isValid()` | - | Check if field passes validation |
| `isFieldEnabled()` | - | Check if field is enabled |
| `setEnabled()` | `enabled: Boolean` | Enable/disable field |
| `setSupportingText()` | `text: String?` | Set helper text |
| `setMinLength()` | `min: Int` | Set minimum length requirement |
| `setMaxLength()` | `max: Int` | Set maximum length limit |
| `getNumericValue()` | - | Get value as Double (NumberInput only) |
| `getIntValue()` | - | Get value as Int (NumberInput only) |

## Usage Examples

### Text Input with Validation

```kotlin
val emailField = findViewById<InputField>(R.id.emailField)
emailField.configure(
    InputFieldConfig(
        type = InputFieldType.TextInput,
        title = "Email",
        hint = "you@example.com",
        isRequired = true
    ),
    id = "email"
)

// Validate on submit
if (!emailField.isValid()) {
    // Field is invalid (shows error automatically)
    return
}

val email = emailField.getValue() as? String
```

### Dropdown Selection

```kotlin
// Via XML
<InputField
    app:inputType="dropdown"
    app:fieldTitle="Country"
    app:options="@array/countries" />

// Via code
inputField.configure(
    InputFieldConfig(
        type = InputFieldType.Dropdown,
        title = "Select Country",
        hint = "Choose your country",
        options = listOf("Indonesia", "Malaysia", "Singapore")
    )
)

// Get selected value
val selectedCountry = inputField.getValue() as? String
```

### Text Area with Character Counter

```kotlin
inputField.configure(
    InputFieldConfig(
        type = InputFieldType.TextArea,
        title = "Description",
        hint = "Tell us about yourself",
        maxLength = 500,
        minLength = 50,
        maxLines = 6,
        minLines = 3
    )
)

inputField.setSupportingText("Write a brief description")
```

### Radio Group

```kotlin
inputField.configure(
    InputFieldConfig(
        type = InputFieldType.RadioGroup,
        title = "Gender",
        isRequired = true,
        options = listOf("Male", "Female", "Other")
    )
)

// Get selected option
val gender = inputField.getValue() as? String
```

### Checkbox Group

```kotlin
inputField.configure(
    InputFieldConfig(
        type = InputFieldType.CheckboxGroup,
        title = "Interests",
        options = listOf("Sports", "Music", "Reading", "Travel")
    )
)

// Get selected options
val interests = inputField.getValue() as? List<String>
```

### Number Input

```kotlin
inputField.configure(
    InputFieldConfig(
        type = InputFieldType.NumberInput,
        title = "Phone Number",
        hint = "08123456789",
        maxLength = 13,
        minLength = 10
    )
)

// Get as integer
val phoneNumber = inputField.getIntValue()
```

## Delegate Interface

```kotlin
inputField.delegate = object : InputFieldDelegate {
    override fun onValueChange(fieldId: String, value: Any?) {
        Log.d("InputField", "$fieldId changed to: $value")
    }
    
    override fun onValidationChange(fieldId: String, isValid: Boolean) {
        submitButton.isEnabled = isValid
    }
}
```

## Validation

The component automatically validates based on:
- Required field constraints
- Minimum/maximum length requirements
- Input type constraints

```kotlin
// Manual validation
if (inputField.isValid()) {
    // Process form
} else {
    // Error is automatically displayed
    val error = inputField.getErrorText()
}

// Programmatic error setting
inputField.setError("Email format is invalid")

// Clear error
inputField.clearError()
```

## Customization Examples

### Dynamic Configuration

```kotlin
// Change field properties at runtime
inputField.apply {
    setMinLength(5)
    setMaxLength(100)
    setSupportingText("Please enter at least 5 characters")
}
```

### Custom Error Messages

```kotlin
val value = inputField.getValue() as? String
if (value?.contains("@") == false) {
    inputField.setError("Email must contain @ symbol")
}
```

### Enable/Disable Field

```kotlin
// Disable field
inputField.setEnabled(false)

// Re-enable
inputField.setEnabled(true)
```

## Performance Considerations

- **View Caching**: Reuses color and drawable resources via internal caching to minimize memory allocations
- **Efficient Updates**: Uses `doAfterTextChanged` for reactive updates without excessive listener overhead
- **Lazy Initialization**: Creates input components only when needed based on type
- **Cache Clearing**: Automatically clears caches on `onDetachedFromWindow` to prevent memory leaks

## Visual States

| State | Visual Feedback |
| ----- | --------------- |
| Normal | Default border with subtle stroke |
| Focused | Accent color border (2dp stroke) |
| Error | Red border with error message below |
| Disabled | Grayed out with overlay, non-interactive |
| Required | Asterisk (*) in title with attention color |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use descriptive field titles | Use vague titles like "Input" |
| Provide helpful supporting text | Rely only on placeholders |
| Set appropriate maxLength limits | Allow unlimited input for bounded fields |
| Use validation callbacks for complex logic | Validate only on form submit |
| Test with different input scenarios | Assume all users follow expected patterns |
| Clear errors when user corrects input | Keep stale error messages |
| Use appropriate input types | Use TextInput for everything |
| Provide immediate feedback | Delay validation until submission |

## Common Patterns

### Form with Multiple Fields

```kotlin
val fields = listOf(
    findViewById<InputField>(R.id.nameField),
    findViewById<InputField>(R.id.emailField),
    findViewById<InputField>(R.id.phoneField)
)

submitButton.setOnClickListener {
    val allValid = fields.all { it.isValid() }
    if (allValid) {
        // Submit form
        val formData = fields.associate { 
            it.fieldId to it.getValue() 
        }
        submitForm(formData)
    }
}
```

---

> **⚠️ Note**: This component requires FragmentActivity context for dropdown functionality. Ensure your Activity extends FragmentActivity or AppCompatActivity. The component automatically manages focus and keyboard behavior for optimal user experience.
