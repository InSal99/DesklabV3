# CheckBox

## Overview
A custom `CheckBox` component extending **AppCompatCheckBox**, providing:
- Custom text appearances for different states
- Animated checkmark scaling when toggled
- Error state handling with visual feedback
- Delegate support for state change callbacks

This component is styled consistently with app theme and offers enhanced UX over the default checkbox.

| Variation | Preview                                                                                                                           |
|-----------|-----------------------------------------------------------------------------------------------------------------------------------|
| Unchecked | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759228781/Screenshot_2025-09-30_at_17.39.14_uvxkxg.png) |
| Checked   | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759228780/Screenshot_2025-09-30_at_17.39.04_i1fmmk.png) |
| Disabled  | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759228780/Screenshot_2025-09-30_at_17.39.20_kfqbim.png) |
| Error     | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759304456/Screenshot_2025-10-01_at_14.40.42_u0tr2q.png) |


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
| `setErrorState()` | `error: Boolean` | Enables/disables error state (updates drawable state and text appearance) |
| `isErrorState()` | – | Returns current error state |
| `setCustomCheckBoxDelegate()` | `delegate: CheckboxDelegate?` | Sets delegate for state changes |
| `setTextAppearances()` | Multiple `@StyleRes` | Configures custom styles for states |
| `setChecked()` | `checked: Boolean` | Toggles checked state; if changed and the view is shown, animates the checkmark |
| `performClick()` | – | Calls `super.performClick()`, clears error state (`setErrorState(false)`), then notifies delegate via `checkBoxDelegate?.onCheckChanged(this, isChecked)` |

Notes:
- `setChecked()` will animate the checkmark only if the previous value differs from the new value, the view is shown, and width > 0 (i.e., the view has been measured). If not measured/visible the animation is skipped but the checked state still updates.
- The `performClick()` implementation clears the error state when the user interacts and then notifies any delegate.

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

![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759229185/checkboxgif_uhw6nq.gif)

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

⚠️ **Note**: Ensure `R.drawable.kit_ic_checkbox_states` (or your custom drawable) is a layered drawable with the checkmark on layer index 1. The animation manipulates that layer's bounds and tint — if your drawable doesn't follow this structure the animation will be skipped.