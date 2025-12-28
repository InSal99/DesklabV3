# Footer


## Overview
A flexible `Footer` component extending **LinearLayout**, designed to display:
- Call-to-action footers with a single button
- Detailed CTAs with description text
- Dual-button layouts with support text
- Informational footers with status badges

It supports delegates for handling button events, stroke customization, and dynamic text updates.

| Feature / Variation | Preview |
| ------------------- | ------- |
| Call To Action | ![footer-cta](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229524/Screenshot_2025-09-30_at_17.51.17_f6lppm.png) |
| Call To Action Detail | ![footer-cta-detail](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229524/Screenshot_2025-09-30_at_17.51.33_iofiup.png) |
| Dual Button | ![footer-dual](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229525/Screenshot_2025-09-30_at_17.51.43_fltxgg.png) |
| No Action | ![footer-noaction](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229525/Screenshot_2025-09-30_at_17.51.51_lfdrh0.png) |

---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.footer.Footer
    android:id="@+id/footer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:footerType="CALL_TO_ACTION_DETAIL"
    app:footerHasStroke="true"
    app:primaryButtonText="Register"
    app:footerTitle="Footer Title"
    app:footerDescription="This is a description"
    app:statusBadgeText="Approved"
    app:statusBadgeType="approved"
/>
```

### 2. Initialize in Code

```kotlin
val footer = findViewById<Footer>(R.id.footer)

footer.delegate = object : FooterDelegate {
    override fun onPrimaryButtonClicked(type: Footer.FooterType) {
        // Handle primary button
    }

    override fun onSecondaryButtonClicked(type: Footer.FooterType) {
        // Handle secondary button
    }
}
```

---

## Display Modes

| Type | Value | Description | Use Case |
| ---- | ----- | ----------- | -------- |
| `CALL_TO_ACTION` | `0` | Footer with a single primary button | Sign-up, main action |
| `CALL_TO_ACTION_DETAIL` | `1` | CTA with title and description | Informative CTA |
| `DUAL_BUTTON` | `2` | Footer with primary + secondary buttons and support text | Continue/Cancel flows |
| `NO_ACTION` | `3` | Informational footer with status badge | Read-only information |

```kotlin
footer.setFooterType(Footer.FooterType.DUAL_BUTTON)
```

---

## Properties Reference

### Text Properties

| Property | Type | Default | Description |
| -------- | ---- | ------- | ----------- |
| `primaryButtonText` | `String` | "Daftar Sekarang" | Text for primary button |
| `secondaryButtonText` | `String` | "Button Label" | Text for secondary button |
| `footerTitle` | `String` | `""` | Title text |
| `footerDescription` | `String` | `""` | Description text |
| `statusText` | `String` | `""` | Status badge label |
| `dualButtonTitle` | `String` | `""` | Title in dual button mode |
| `dualButtonSupportText1` | `String` | `""` | First support text |
| `dualButtonSupportText2` | `String` | `""` | Second support text |

### Behavior Properties

| Property | Type | Default | Description |
| -------- | ---- | ------- | ----------- |
| `footerType` | `FooterType` | `CALL_TO_ACTION` | Determines footer layout |
| `footerHasStroke` | `Boolean` | `false` | Adds top border stroke (see stroke details) |
| `primaryButtonEnabled` | `Boolean` | `true` | Enable/disable primary button |
| `secondaryButtonEnabled` | `Boolean` | `true` | Enable/disable secondary button |
| `delegate` | `FooterDelegate?` | `null` | Handles click events |

---

## Methods Reference

| Method | Parameters | Description |
| ------ | --------- | ----------- |
| `setFooterType()` | `type: FooterType` | Switches footer layout |
| `setPrimaryButtonText()` | `text: String` | Updates primary button text |
| `setSecondaryButtonText()` | `text: String` | Updates secondary button text |
| `setTitleAndDescription()` | `title: String, description: String` | Updates title and description |
| `setStatusBadge()` | `text: String, type: StatusBadge.ChipType` | Sets status badge label and style |
| `setPrimaryButtonEnabled()` | `enabled: Boolean` | Enables/disables primary button |
| `setSecondaryButtonEnabled()` | `enabled: Boolean` | Enables/disables secondary button |
| `setDescriptionVisibility()` | `showDescription: Boolean` | Shows/hides support texts |
| `setDualButtonDescription()` | `title: String, supportText1: String, supportText2: String` | Updates dual button texts |
| `setStroke()` | `hasStroke: Boolean` | Toggles top stroke |
| `isDescriptionVisible()` | – | Returns whether description is visible |
| `getDualButtonTitle()` | – | Returns current dual button title |
| `getDualButtonSupportText1()` | – | Returns first support text |
| `getDualButtonSupportText2()` | – | Returns second support text |

---

## Usage Examples

```kotlin
// Call to action footer
footer.apply {
    setFooterType(Footer.FooterType.CALL_TO_ACTION)
    setPrimaryButtonText("Sign Up")
}

// Dual button footer
footer.apply {
    setFooterType(Footer.FooterType.DUAL_BUTTON)
    setDualButtonDescription("Proceed?", "Continue your action", "Or cancel instead")
    setPrimaryButtonText("Continue")
    setSecondaryButtonText("Cancel")
}
```

---

## Performance Considerations
- Uses **ViewBinding** for layout inflation per mode.
- Clears bindings to avoid memory leaks when switching footer types.
- Minimizes layout updates by only rebinding changed properties.

---

## Best Practices

| ✅ Do | ❌ Don’t |
| ----- | ------- |
| Use CTA for primary actions | Put too many buttons in one footer |
| Use DualButton for binary flows (Continue/Cancel) | Mix unrelated actions in one footer |
| Hide description when not needed | Leave empty placeholders visible |

---

⚠️ **Note**: Ensure `StatusBadge` component is implemented and styled consistently for use in `NO_ACTION` mode. Also remember to implement all delegate callbacks you need — the Footer will call both generic (onPrimaryButtonClicked / onSecondaryButtonClicked) and mode-specific callbacks (onRegisterClicked, onContinueClicked, onCancelClicked).