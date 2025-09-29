# Option Card

## Overview

OptionCard is a selectable card component that displays an option with text and an icon. It features Material Design styling with elevation, stroke, shadow, and ripple effects. Commonly used in dropdown menus, option lists, and selection interfaces.

| Feature / Variation | Preview |
| ------------------- | ------- |
| **Default Card** | Text with chevron icon |
| **Custom Icon** | Text with custom drawable/resource |
| **Interactive** | Ripple effect on press |

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

## Properties Reference

### XML Attributes

| Attribute Name | Type | Default | Description |
| -------------- | ---- | ------- | ----------- |
| `cardText` | `String` | `null` | Text displayed on the card |
| `cardIcon` | `Drawable` | `null` | Icon drawable displayed on the right |

### Runtime Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `titleText` | `CharSequence?` | `null` | Text content of the card |
| `iconDrawable` | `Drawable?` | `null` | Icon as drawable object |
| `iconResource` | `Int?` | `null` | Icon as drawable resource ID |
| `delegate` | `OptionCardDelegate?` | `null` | Click event handler |

### Visual Properties

| Property | Value | Description |
| -------- | ----- | ----------- |
| Stroke Width | `1dp` | Border thickness |
| Corner Radius | `12dp` | Rounded corner radius |
| Card Elevation | `2dp` | Shadow elevation |
| Stroke Color | `colorStrokeSubtle` | Border color from theme |
| Ripple Color | `colorBackgroundModifierOnPress` | Press feedback color |

## Delegate Interface

```kotlin
interface OptionCardDelegate {
    fun onClick(card: OptionCard)
}
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `performClick()` | - | Triggers click action and notifies delegate |

*Note: Properties are set directly rather than through methods*

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

## Customization Examples

### Change Text at Runtime

```kotlin
// Update text dynamically
optionCard.titleText = "Updated Option Text"
```

### Swap Icons

```kotlin
// Change icon based on state
if (isTrue) {
    optionCard.iconResource = R.drawable.ic_chevron_up
} else {
    optionCard.iconResource = R.drawable.ic_chevron_down
}
```

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

## Visual States

| State | Visual Feedback |
| ----- | --------------- |
| Default | White background with subtle border |
| Pressed | Ripple effect with `colorBackgroundModifierOnPress` |
| Focused | Material card elevation visible |
| Shadow | Ambient and spot shadows (API 28+) |

## Material Design Features

### Elevation & Shadow

```kotlin
// Configured automatically in setupCardAppearance()
cardElevation = 2dp

// On Android P+ (API 28)
outlineAmbientShadowColor = colorShadowNeutralAmbient
outlineSpotShadowColor = colorShadowNeutralKey
```

### Ripple Effect

```kotlin
// Configured automatically
rippleColor = ColorStateList.valueOf(colorBackgroundModifierOnPress)
isClickable = true
isFocusable = true
```

## Theme Colors Used

| Theme Attribute | Fallback Color | Usage |
| --------------- | -------------- | ----- |
| `colorStrokeSubtle` | - | Card border color |
| `colorBackgroundModifierOnPress` | `color000Opacity5` | Ripple effect color |
| `colorShadowNeutralAmbient` | `colorGreen50` | Ambient shadow (API 28+) |
| `colorShadowNeutralKey` | `colorGreen50` | Spot shadow (API 28+) |

## Performance Considerations

- **Lightweight**: Extends MaterialCardView with minimal overhead
- **View Binding**: Uses ViewBinding for efficient view references
- **Attribute Caching**: Resolves theme attributes once during initialization
- **No Complex Layouts**: Simple internal structure for fast inflation

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

private fun updateSelectionState(container: ViewGroup, selectedCard: OptionCard) {
    for (i in 0 until container.childCount) {
        val card = container.getChildAt(i) as? OptionCard
        card?.iconResource = if (card == selectedCard) {
            R.drawable.ic_radio_button_checked
        } else {
            R.drawable.ic_radio_button_unchecked
        }
    }
}
```

### Navigation Menu

```kotlin
val menuItems = listOf(
    "Profile" to R.drawable.ic_person,
    "Settings" to R.drawable.ic_settings,
    "Help" to R.drawable.ic_help,
    "Logout" to R.drawable.ic_logout
)

menuItems.forEach { (title, icon) ->
    val card = OptionCard(context).apply {
        titleText = title
        iconResource = icon
        
        delegate = object : OptionCardDelegate {
            override fun onClick(card: OptionCard) {
                navigateTo(title)
            }
        }
    }
    menuContainer.addView(card)
}
```

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use concise, clear option text | Use overly long text that wraps |
| Set appropriate icons for context | Mix inconsistent icon styles |
| Provide visual feedback for selection | Leave users uncertain of selection |
| Use consistent spacing between cards | Create uneven visual rhythm |
| Implement delegate for all clickable cards | Rely on performClick() without delegate |
| Test ripple effect visibility | Assume default theme works everywhere |

## Accessibility Considerations

```kotlin
// Add content description for screen readers
optionCard.contentDescription = "Select ${optionCard.titleText}"

// Ensure clickable feedback
optionCard.isClickable = true
optionCard.isFocusable = true
```

## Layout Guidelines

### Recommended Spacing

```kotlin
val layoutParams = LinearLayout.LayoutParams(
    LinearLayout.LayoutParams.MATCH_PARENT,
    LinearLayout.LayoutParams.WRAP_CONTENT
).apply {
    bottomMargin = resources.getDimensionPixelSize(R.dimen.margin_8dp)
    // or
    setMargins(16.dp, 0, 16.dp, 8.dp)
}
```

### Container Recommendations

- Use within `LinearLayout` for vertical lists
- Use within `ScrollView` for long option lists
- Use within `BottomTray` for modal selections
- Add padding to parent container (16dp recommended)

---

> **⚠️ Note**: This component extends MaterialCardView and requires Material Components library. The delegate pattern ensures loose coupling and easy testing. Shadow colors are only applied on Android P (API 28) and above.
