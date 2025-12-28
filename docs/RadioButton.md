# Radio Button

| Type                  | Preview |
|-----------------------| ------- |
| **Normal**            | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759290446/Screenshot_2025-10-01_at_10.45.34_kv2vos.png) |
| **Selected**          | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759290446/Screenshot_2025-10-01_at_10.45.42_g8ipjs.png) |
| **Disabled**          | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759290446/Screenshot_2025-10-01_at_10.45.47_j6peqo.png) |
| **Disabled Selected** | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759290446/Screenshot_2025-10-01_at_10.45.51_djsien.png) |
| **Error**             | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_200/v1759306969/Screenshot_2025-10-01_at_15.22.34_rydlyv.png) |

## Overview

**Radio Button** is a custom radio button component that extends AppCompatRadioButton with enhanced animations, state management, and text appearance customization. It features a smooth scale animation for the inner circle when toggling. For the enhanced functionality for dynamic lists and type-safe data handling, Radio Group is provided as a container component that manages RadioButton instances, providing data binding, error state management, and selection callbacks.

## Basic Usage

### Standalone RadioButton (presentation)

```xml
<com.edts.components.radiobutton.RadioButton
    android:id="@+id/radioButton"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Option 1"
    app:isRadioError="false" />
```

Note: While RadioButton has an internal delegate, the class is intended to be managed by `RadioGroup` when used in forms/lists. Do not rely on setting a delegate directly on a RadioButton instance (the delegate field is internal in this implementation).

---

## RadioGroup (recommended)

Use `RadioGroup` to manage multiple `RadioButton` instances — it provides data-binding, selection callbacks and error propagation.

```kotlin
val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

radioGroup.setData(listOf("Male", "Female", "Other")) { it }

radioGroup.setOnItemSelectedListener(object : RadioGroupDelegate {
    override fun onItemSelected(position: Int, data: Any?) {
        // Handle selection
    }
})
```

---

## Properties Reference

### RadioButton (public surface)

| Property/Method | Type | Notes |
| --------------- | ---- | ----- |
| `setErrorState(error: Boolean)` | method | Toggle error state (updates drawable/text appearance) |
| `isErrorState(): Boolean` | method | Read current error state |
| `setTextAppearances(...)` | method | Customize text appearances for all states |
| `setChecked(checked: Boolean)` | method | Programmatic check (triggers animation when appropriate) |
| `setEnabled(enabled: Boolean)` | method | Enable/disable the control |
| `performClick()` | method | Programmatic click (clears error and triggers internal callback) |

### RadioGroup (public surface — highlights)

| Method | Description |
| ------ | ----------- |
| `setData()` | Populate group with items; group creates RadioButton instances |
| `setOnItemSelectedListener()` | Receive selection callbacks |
| `getSelectedData()` | Get currently selected data |
| `selectItem()` / `selectItemByData()` | Programmatic selection |
| `setErrorStateOnAll()` | Propagate error state to all children |

---

## Examples & Patterns

- Use `RadioGroup`/`RadioGroupDelegate` for form handling and validation, not the `RadioButton`'s internal delegate.
- For lists or dynamic data consider using `RadioGroup.setData()` or a RecyclerView-based approach if the list is large/frequently updated.
- For validation flows use `RadioGroup.setErrorStateOnAll(true)` to mark required choices.

---

## Performance Considerations

- Animations are lightweight (ValueAnimator) and cancelled on detach to avoid leaks.
- Drawable operations are minimal; avoid frequent recreation of custom drawables at runtime.

---

> **Note**: This techdoc reflects the implementation in the code you provided. If you intended `radioChangedDelegate` to be part of the public API, change the visibility in the implementation (make it a public var or expose a setter) and I can update the docs and examples to show direct delegate usage.