# InfoBoxFooter

## Overview
A composite `InfoBoxFooter` component that combines an **InfoBox** (information message) with a **Footer** (CTA or dual-button layout).  

It supports:
- All **InfoBox types** (`Information`, `Success`, `Error`, `General`)  
- All **Footer types** (`CallToAction`, `CallToActionDetail`, `DualButton`, `NoAction`)  
- Configurable visibility of the InfoBox  
- Delegation via `FooterDelegate`  

### Preview
![infobox-footer](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229784/Screenshot_2025-09-30_at_17.56.11_qf34ye.png)

---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.infoboxfooter.InfoBoxFooter
    android:id="@+id/infoBoxFooter"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:text="Your session will expire soon"
    app:infoboxType="error"
    app:showInfoBox="true"
    app:footerType="dual_button"
    app:primaryButtonText="Continue"
    app:secondaryButtonText="Cancel"
    app:footerTitle="Action Required"
    app:footerDescription="Choose an option below"
    app:primaryButtonEnabled="true"
    app:secondaryButtonEnabled="true"
    app:footerHasStroke="true"
/>
```

### 2. Initialize in Code

```kotlin
val infoBoxFooter = findViewById<InfoBoxFooter>(R.id.infoBoxFooter)

infoBoxFooter.apply {
    setInfoBoxText("Your application was submitted successfully")
    setInfoBoxVariant(InfoBox.InfoBoxVariant.SUCCESS)
    setFooterType(Footer.FooterType.CALL_TO_ACTION)
    setPrimaryButtonText("Continue")
    setSecondaryButtonText("Cancel")
    footerDelegate = object : FooterDelegate {
        override fun onPrimaryButtonClicked(type: Footer.FooterType) { /* handle */ }
        override fun onSecondaryButtonClicked(type: Footer.FooterType) { /* handle */ }
    }
}

```

---

## Display Modes

- InfoBox types: Information, Success, Error, General
- Footer Types: CallToAction, CallToActionDetail, DualButton, NoAction
- Visibility Control: InfoBox can be shown/hidden while keeping footer active


### XML Attributes

| Method                        | Parameters                                                  | Description                         |
| ----------------------------- | ----------------------------------------------------------- | ----------------------------------- |
| `setInfoBoxText()`            | `text: CharSequence?`                                       | Updates InfoBox message             |
| `setInfoBoxVariant()`         | `variant: InfoBox.InfoBoxVariant`                           | Sets InfoBox style                  |
| `showInfoBox()`               | `show: Boolean`                                             | Shows/hides InfoBox                 |
| `setFooterType()`             | `type: Footer.FooterType`                                   | Updates footer layout type          |
| `getFooterType()`             | –                                                           | Returns current footer type         |
| `setPrimaryButtonText()`      | `text: String`                                              | Updates primary button label        |
| `setSecondaryButtonText()`    | `text: String`                                              | Updates secondary button label      |
| `setTitleAndDescription()`    | `title: String, description: String`                        | Updates footer texts                |
| `setStatusBadge()`            | `text: String, type: StatusBadge.ChipType`                  | Sets footer status badge            |
| `setPrimaryButtonEnabled()`   | `enabled: Boolean`                                          | Enables/disables primary button     |
| `setSecondaryButtonEnabled()` | `enabled: Boolean`                                          | Enables/disables secondary button   |
| `setDescriptionVisibility()`  | `showDescription: Boolean`                                  | Shows/hides support texts in footer |
| `setDualButtonDescription()`  | `title: String, supportText1: String, supportText2: String` | Updates dual button texts           |
| `setFooterStroke()`           | `hasStroke: Boolean`                                        | Enables/disables stroke manually    |
| `getInfoBox()`                | –                                                           | Returns inner InfoBox instance      |
| `getFooter()`                 | –                                                           | Returns inner Footer instance       |


### Properties Reference

| Property           | Type                     | Default       | Description                  |
| ------------------ | ------------------------ | ------------- | ---------------------------- |
| `infoText`         | `CharSequence?`          | `null`        | InfoBox text message         |
| `infoVariant`      | `InfoBox.InfoBoxVariant` | `INFORMATION` | InfoBox style variant        |
| `footerDelegate`   | `FooterDelegate?`        | `null`        | Handles footer button clicks |
| `isInfoBoxVisible` | `Boolean`                | `true`        | Whether InfoBox is visible   |

### Methods Reference

| Method                        | Parameters                                                  | Description                         |
| ----------------------------- | ----------------------------------------------------------- | ----------------------------------- |
| `setInfoBoxText()`            | `text: CharSequence?`                                       | Updates InfoBox message             |
| `setInfoBoxVariant()`         | `variant: InfoBox.InfoBoxVariant`                           | Sets InfoBox style                  |
| `showInfoBox()`               | `show: Boolean`                                             | Shows/hides InfoBox                 |
| `setFooterType()`             | `type: Footer.FooterType`                                   | Updates footer layout type          |
| `getFooterType()`             | –                                                           | Returns current footer type         |
| `setPrimaryButtonText()`      | `text: String`                                              | Updates primary button label        |
| `setSecondaryButtonText()`    | `text: String`                                              | Updates secondary button label      |
| `setTitleAndDescription()`    | `title: String, description: String`                        | Updates footer texts                |
| `setStatusBadge()`            | `text: String, type: StatusBadge.ChipType`                  | Sets footer status badge            |
| `setPrimaryButtonEnabled()`   | `enabled: Boolean`                                          | Enables/disables primary button     |
| `setSecondaryButtonEnabled()` | `enabled: Boolean`                                          | Enables/disables secondary button   |
| `setDescriptionVisibility()`  | `showDescription: Boolean`                                  | Shows/hides support texts in footer |
| `setDualButtonDescription()`  | `title: String, supportText1: String, supportText2: String` | Updates dual button texts           |
| `setFooterStroke()`           | `hasStroke: Boolean`                                        | Enables/disables stroke manually    |
| `getInfoBox()`                | –                                                           | Returns inner InfoBox instance      |
| `getFooter()`                 | –                                                           | Returns inner Footer instance       |

---

## Display Modes
```kotlin
// Info + Dual Button footer
infoBoxFooter.apply {
    setInfoBoxText("Are you sure you want to proceed?")
    setInfoBoxVariant(InfoBox.InfoBoxVariant.ERROR)
    setFooterType(Footer.FooterType.DUAL_BUTTON)
    setPrimaryButtonText("Yes")
    setSecondaryButtonText("No")
}

// Footer only (InfoBox hidden)
infoBoxFooter.apply {
    showInfoBox(false)
    setFooterType(Footer.FooterType.CALL_TO_ACTION)
    setPrimaryButtonText("Proceed")
}
```

---

## Performance Considerations
- Delegates to inner InfoBox and Footer for rendering → no duplicate logic.
- Automatically toggles stroke visibility based on InfoBox state.
- Avoids layout bloat by reusing child components.

---

## Best Practices
| ✅ Do                                      | ❌ Don’t                                            |
| ----------------------------------------- | -------------------------------------------------- |
| Use InfoBox for contextual info           | Mix unrelated info in footer                       |
| Hide InfoBox when not needed              | Leave empty InfoBox visible                        |
| Reuse Footer delegate for button handling | Handle button clicks separately in multiple places |

---

⚠️ Note: This component is a wrapper around InfoBox + Footer, so both must be properly styled and available in your theme.