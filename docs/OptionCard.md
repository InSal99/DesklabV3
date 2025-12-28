# Option Card

## Overview

OptionCard is a selectable card component that displays an option with text and an icon. It features Material Design styling with elevation, stroke, shadow, and ripple effects. Commonly used in dropdown menus, option lists, and selection interfaces.

### Preview
![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759290856/optioncardgif_fe8kox.gif)
---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.option.card.OptionCard
    android:id="@+id/optionCard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardText="Select Option"
    app:cardIcon="@drawable/ic_chevron_right" />
```

### 2. Initialize in Code

```kotlin
val optionCard = findViewById<OptionCard>(R.id.optionCard)

// Set properties
optionCard.titleText = "Indonesia"
optionCard.iconResource = R.drawable.ic_check

// Set delegate for click handling
optionCard.delegate = object : OptionCardDelegate {
    override fun onClick(card: OptionCard) {
        // Handle card click
        Toast.makeText(context, "Selected: ${card.titleText}", Toast.LENGTH_SHORT).show()
    }
}
```

---

## Properties Reference

### XML Attributes

| Attribute Name | Type | Default | Description |
| -------------- | ---- | ------- | ----------- |
| `cardText` | `String` | `null` | Text displayed on the card |
| `cardIcon` | `Drawable` | `null` | Icon drawable displayed on the right |

### Runtime Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `titleText` | `CharSequence?` | `null` | Text content of the card (set via `titleText`) |
| `iconDrawable` | `Drawable?` | `null` | Icon as drawable object (set via `iconDrawable`) |
| `iconResource` | `Int?` | `null` | Icon as drawable resource ID (set via `iconResource`) |
| `delegate` | `OptionCardDelegate?` | `null` | Click event handler (notified from `performClick()`) |

Notes:
- Properties are exposed as Kotlin `var` properties. Use them directly to read/write values.
- `initAttrs` reads `cardText` and `cardIcon` from XML and applies them at inflation.

### Visual Properties

| Property | Value (code) | Description |
| -------- | ------------ | ----------- |
| Stroke Width | `1.dpToPx` | Border thickness |
| Corner Radius | `R.dimen.radius_12dp` | Rounded corner radius |
| Card Elevation | `1.dpToPx` | Shadow elevation (1dp) |
| Stroke Color | resolved via `R.attr.colorStrokeSubtle` (fallback `R.color.kitColorNeutralGrayLight30`) | Border color from theme |
| Ripple Color | resolved via `R.attr.colorBackgroundModifierOnPress` (fallback `R.color.kitColorNeutralGrayDarkA5`) | Press feedback color |

---

## Delegate Interface

```kotlin
interface OptionCardDelegate {
    fun onClick(card: OptionCard)
}
```

The component calls `delegate?.onClick(this)` from `performClick()` before calling `super.performClick()`.

---

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `performClick()` | - | Triggers click action and notifies delegate (delegate invoked, then `super.performClick()` is called). |

Note: Properties are set directly rather than through getters/setters.

---

## Usage Examples

### Basic Option Card

```kotlin
val optionCard = OptionCard(context).apply {
    titleText = "Payment Method"
    iconResource = R.drawable.ic_chevron_down

    delegate = object : OptionCardDelegate {
        override fun onClick(card: OptionCard) {
            showPaymentOptions()
        }
    }
}
```

### Dynamic List of Options

```kotlin
val options = listOf("Credit Card", "Bank Transfer", "E-Wallet", "Cash on Delivery")

val container = findViewById<LinearLayout>(R.id.optionsContainer)
container.removeAllViews()

options.forEach { option ->
    val card = OptionCard(context).apply {
        titleText = option
        iconResource = R.drawable.ic_arrow_forward

        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_8dp)
        }

        delegate = object : OptionCardDelegate {
            override fun onClick(card: OptionCard) {
                handleOptionSelected(option)
            }
        }
    }
    container.addView(card)
}
```

### Selected State Indicator

```kotlin
val cards = mutableListOf<OptionCard>()
var selectedCard: OptionCard? = null

options.forEachIndexed { index, option ->
    val card = OptionCard(context).apply {
        titleText = option

        delegate = object : OptionCardDelegate {
            override fun onClick(card: OptionCard) {
                // Update previous selection
                selectedCard?.iconResource = R.drawable.ic_chevron_right

                // Set new selection
                iconResource = R.drawable.ic_check
                selectedCard = this@apply

                onOptionSelected(option)
            }
        }
    }

    // Set initial state
    if (index == 0) {
        card.iconResource = R.drawable.ic_check
        selectedCard = card
    } else {
        card.iconResource = R.drawable.ic_chevron_right
    }

    cards.add(card)
    container.addView(card)
}
```

### Custom Icon from Drawable

```kotlin
val customIcon = ContextCompat.getDrawable(context, R.drawable.custom_icon)
optionCard.apply {
    titleText = "Special Option"
    iconDrawable = customIcon
}
```

### Programmatic Click

```kotlin
// Trigger click programmatically
optionCard.performClick()
```

---

## Integration with InputField

OptionCard is commonly used within InputField's dropdown functionality:

```kotlin
// Used internally by InputField for dropdown options
private fun createOptionDrawerLayout(
    options: List<String>,
    onOptionSelected: (String) -> Unit
): BottomTray {
    val contentContainer = LinearLayout(context)

    options.forEach { option ->
        val optionCard = OptionCard(context).apply {
            titleText = option
            delegate = object : OptionCardDelegate {
                override fun onClick(card: OptionCard) {
                    onOptionSelected(option)
                }
            }
        }
        contentContainer.addView(optionCard)
    }

    // Add to bottom tray...
}
```

---

## Visual States

| State | Visual Feedback |
| ----- | --------------- |
| Default | White (or elevated background) with subtle border |
| Pressed | Ripple effect with color resolved from `colorBackgroundModifierOnPress` |
| Focused | Material card elevation visible |
| Shadow | Ambient and spot shadows applied on Android P+ (API 28+) |

---

## Material Design Features

### Elevation & Shadow

Configured in code during `setupCardAppearance()`:

- Card elevation is set to 1dp (via `1f.dpToPx`)
- On Android P+ (API 28) the card sets shadow tint colors:
    - `outlineAmbientShadowColor` resolved from `R.attr.colorShadowNeutralAmbient` (fallback `R.color.kitColorNeutralGrayDarkA5`)
    - `outlineSpotShadowColor` resolved from `R.attr.colorShadowNeutralKey` (fallback `R.color.kitColorNeutralGrayDarkA10`)

### Ripple Effect

Configured in `setupClickAnimation()`:

- `isClickable = true` and `isFocusable = true`
- `rippleColor` resolved from `R.attr.colorBackgroundModifierOnPress` with fallback `R.color.kitColorNeutralGrayDarkA5`

---

## Theme Attributes & Fallbacks

| Theme Attribute | Fallback Resource (code) | Usage |
| --------------- | ------------------------- | ----- |
| `colorBackgroundElevated` | `R.color.kitColorModifierElevated` | Card background color |
| `colorStrokeSubtle` | `R.color.kitColorNeutralGrayLight30` | Stroke / border color |
| `colorBackgroundModifierOnPress` | `R.color.kitColorNeutralGrayDarkA5` | Ripple press color |
| `colorShadowNeutralAmbient` | `R.color.kitColorNeutralGrayDarkA5` | Ambient shadow color (API 28+) |
| `colorShadowNeutralKey` | `R.color.kitColorNeutralGrayDarkA10` | Spot shadow color (API 28+) |

---

## Performance Considerations

- **Lightweight**: Extends MaterialCardView with minimal overhead
- **View Binding**: Uses ViewBinding for efficient view references (OptionCardBinding)
- **Attribute Resolution**: Theme attributes are resolved once during initialization
- **No Complex Layouts**: Simple internal structure for fast inflation

---

## Common Patterns

### Option Selection List

```kotlin
fun createOptionsList(options: List<String>, onSelected: (String) -> Unit) {
    val container = findViewById<LinearLayout>(R.id.container)

    options.forEach { option ->
        val card = OptionCard(context).apply {
            titleText = option
            iconResource = R.drawable.ic_radio_button_unchecked

            delegate = object : OptionCardDelegate {
                override fun onClick(card: OptionCard) {
                    // Update all cards
                    updateSelectionState(container, card)
                    onSelected(option)
                }
            }

            layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT, WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 8.dp)
            }
        }
        container.addView(card)
    }
}
```

---

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use concise, clear option text | Use overly long text that wraps |
| Set appropriate icons for context | Mix inconsistent icon styles |
| Provide visual feedback for selection | Leave users uncertain of selection |
| Use consistent spacing between cards | Create uneven visual rhythm |
| Implement delegate for clickable cards | Rely on performClick() without delegate |
| Test ripple effect visibility | Assume default theme works everywhere |

---

## Accessibility Considerations

```kotlin
// Add content description for screen readers
optionCard.contentDescription = "Select ${optionCard.titleText}"

// Ensure clickable feedback
optionCard.isClickable = true
optionCard.isFocusable = true
```

---

> **⚠️ Note**: This component extends MaterialCardView and requires Material Components library. The delegate pattern ensures loose coupling and easy testing. Shadow colors are only applied on Android P (API 28) and above.