# InfoBox

## Overview
A custom `InfoBox` component extending **MaterialCardView**, used to display contextual information messages.  

It supports **variants** (`Information`, `Success`, `Error`, `General`), each with distinct background, text, and icon colors. The component also renders a **top shadow with elevation** and custom rounded corners.

| Variant | Preview |
| ------- | ------- |
| Information | ![infobox-info](...) |
| Success | ![infobox-success](...) |
| Error | ![infobox-error](...) |
| General | ![infobox-general](...) |

---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.infobox.InfoBox
    android:id="@+id/infoBox"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:text="This is an information box"
    app:variant="information"
/>
```

### 2. Initialize in Code

```kotlin
val infoBox = findViewById<InfoBox>(R.id.infoBox)

infoBox.apply {
    text = "Operation completed successfully"
    variant = InfoBox.InfoBoxVariant.SUCCESS
    iconSRC = R.drawable.ic_success
}
```

---

## Display Modes

| Variant | Value | Description | Default Icon |
| ------- | ----- | ----------- | ------------- |
| `INFORMATION` | `0` | Subtle background + intense info text | `ic_information` |
| `SUCCESS` | `1` | Success background + green text | `ic_success` |
| `ERROR` | `2` | Attention background + red text | `ic_error` |
| `GENERAL` | `3` | Neutral/secondary background | `placeholder` |

```kotlin
infoBox.variant = InfoBox.InfoBoxVariant.ERROR
```

---

## XML Attributes

| Attribute | Format | Values | Description |
| --------- | ------ | ------ | ----------- |
| `text` | `string` | Any string | Text content of the InfoBox |
| `variant` | `enum` | `information (0)`, `success (1)`, `error (2)`, `general (3)` | Selects the InfoBox variant |

---

## Properties Reference

| Property | Type | Default | Description |
| -------- | ---- | ------- | ----------- |
| `text` | `CharSequence?` | `null` | Sets the message text |
| `iconSRC` | `Int?` (drawable res) | `null` | Custom icon resource (overrides default) |
| `variant` | `InfoBoxVariant` | `INFORMATION` | Controls style (colors, icon, text) |

---

## Methods Reference

| Method | Parameters | Description |
| ------ | --------- | ----------- |
| `applyVariant()` | `variant: InfoBoxVariant` | Applies styling for a given variant |

---

## Usage Examples

```kotlin
// Info variant
infoBox.apply {
    text = "Please check your email inbox"
    variant = InfoBox.InfoBoxVariant.INFORMATION
}

// Error variant
infoBox.apply {
    text = "Something went wrong"
    variant = InfoBox.InfoBoxVariant.ERROR
    iconSRC = R.drawable.ic_custom_error
}
```

---

## Performance Considerations
- Uses **LayerDrawable** with **GradientDrawable** for shadows and rounded corners.  
- Variants reuse the same base layout and apply color updates efficiently.  

---

## Best Practices

| ✅ Do | ❌ Don’t |
| ----- | ------- |
| Use variants to match message context | Overload with too many variants |
| Provide short, clear messages | Add long paragraphs inside InfoBox |
| Customize icons only if needed | Override variant colors unnecessarily |

---

⚠️ **Note**: Requires theme attributes such as `colorBackgroundInfoSubtle`, `colorForegroundSuccessIntense`, etc. to be defined for proper styling.
