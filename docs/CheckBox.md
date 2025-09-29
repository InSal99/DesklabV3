# CheckBox

## Overview
A custom `CheckBox` component extending **AppCompatCheckBox**, providing:  
- Custom text appearances for different states  
- Animated checkmark scaling when toggled  
- Error state handling with visual feedback  
- Delegate support for state change callbacks  

This component is styled consistently with app theme and offers enhanced UX over the default checkbox.

| Feature / Variation | Preview |
| ------------------- | ------- |
| Checked / Unchecked | ![checkbox-state](...) |
| Enabled / Disabled | ![checkbox-enabled](...) |
| Error State | ![checkbox-error](...) |
| Animated CheckMark | ![checkbox-anim](...) |


---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.checkbox.CheckBox
    android:id="@+id/checkboxTerms"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Accept Terms"
    app:isCheckboxError="false"
    app:checkboxNormalTextAppearance="@style/CheckBoxTextAppearance_Normal"
    app:checkboxSelectedTextAppearance="@style/CheckBoxTextAppearance_Selected"
    app:checkboxDisabledTextAppearance="@style/CheckBoxTextAppearance_Disabled"
    app:checkboxDisabledSelectedTextAppearance="@style/CheckBoxTextAppearance_DisabledSelected"
/>
```

### 2. Initialize in Code

```kotlin
val checkbox = findViewById<CheckBox>(R.id.checkboxTerms)

checkbox.apply {
    setCustomCheckBoxDelegate(object : CheckboxDelegate {
        override fun onCheckChanged(view: CheckBox, isChecked: Boolean) {
            // Handle state change
        }
    })
}
```

---

## Display Modes

| Mode | Description | Use Case |
| ---- | ----------- | -------- |
| Normal | Standard unchecked state | Default |
| Selected | Checked state with selected text appearance | User confirms option |
| Disabled | Grayed out, not interactive | Inactive forms |
| Disabled Selected | Checked but disabled | Pre-selected but locked |
| Error | Shows error styling | Validation errors |

---

## Properties Reference

### Text Appearance Properties

| Property | Type | Default | Description |
| -------- | ---- | ------- | ----------- |
| `normalTextAppearance` | `@StyleRes` | `CheckBoxTextAppearance_Normal` | Style for normal text |
| `selectedTextAppearance` | `@StyleRes` | `CheckBoxTextAppearance_Selected` | Style for checked text |
| `disabledTextAppearance` | `@StyleRes` | `CheckBoxTextAppearance_Disabled` | Style for disabled text |
| `disabledSelectedTextAppearance` | `@StyleRes` | `CheckBoxTextAppearance_DisabledSelected` | Style for disabled+checked text |
| `errorTextAppearance` | `@StyleRes` | `CheckBoxTextAppearance_Normal` | Style when in error state |

### Behavior Properties

| Property | Type | Default | Description |
| -------- | ---- | ------- | ----------- |
| `isErrorState` | `Boolean` | `false` | Shows error styling when true |
| `checkBoxDelegate` | `CheckboxDelegate?` | `null` | Optional delegate for change callbacks |

---

## Methods Reference

| Method | Parameters | Description |
| ------ | --------- | ----------- |
| `setErrorState()` | `error: Boolean` | Enables/disables error state |
| `isErrorState()` | – | Returns current error state |
| `setCustomCheckBoxDelegate()` | `delegate: CheckboxDelegate?` | Sets delegate for state changes |
| `setTextAppearances()` | Multiple `@StyleRes` | Configures custom styles for states |
| `setChecked()` | `checked: Boolean` | Toggles checked state with animation |

---

## Usage Examples

```kotlin
// Normal usage
checkbox.setChecked(true)

// Show error state
checkbox.setErrorState(true)

```

---

## Animation Details

| Animation | Duration | Interpolator | Description |
| --------- | -------- | ------------ | ----------- |
| `checkMarkScale` | 200ms | Decelerate | Smooth scaling checkmark in/out |

---

## Performance Considerations
- Cancels ongoing animations before starting new ones.  
- Lightweight scale/tint updates only, no heavy redraws.  
- Avoids leaks by cancelling animations in `onDetachedFromWindow()`.  

---

## Best Practices

| ✅ Do | ❌ Don’t |
| ----- | ------- |
| Use error state for validation feedback | Leave user confused on invalid input |
| Customize text appearances for branding | Hardcode text sizes/colors |
| Use delegate for state changes | Add multiple conflicting listeners |

---

⚠️ **Note**: Ensure `ic_checkbox` drawable supports layered checkmark rendering for proper animation.  
