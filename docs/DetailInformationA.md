# Detail Information A

## Overview
Detail Information A is a customizable information display component that shows an icon, title, description, and optional action buttons. It's designed for displaying detailed information in a structured format with flexible interaction options, automatically managing view visibility and layout constraints based on content availability.

| Variation | Preview                                                                                                                        |
| --------------- |--------------------------------------------------------------------------------------------------------------------------------|
| **Default** | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229295/Screenshot_2025-09-30_at_17.47.46_vfop2v.png) |
| **With Actions** | ![HPTL](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759229295/Screenshot_2025-09-30_at_17.47.58_iroouo.png)                                                                                                              |

---

## Key Features
- Icon (optional) at the start
- Title text (optional)
- Description text (optional)
- Two optional action buttons (actionButton1, actionButton2)
- Delegate callbacks for handling button clicks
- Description icon (optional) with click handling
- Clickable root container with ripple effect
- Delegate callbacks for root and description icon clicks
- Optional URL support for automatic external navigation on root click

---

## Basic Usage

### 1. Add to Layout
```xml
<com.edts.components.detail.information.DetailInformationA
    android:id="@+id/detail_info_block"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:infoIcon="@drawable/ic_info_24"
    app:infoTitle="Status Updated"
    app:infoDescription="Detailed description text goes here."
    app:hasAction="true"
    app:hasDescIcon="true"
    app:descIcon="@drawable/ic_help_24"
    app:urlInfo="https://google.com"
    />
```

### 2. Initialize in Code
```kotlin
val detailInfo = findViewById<DetailInformationA>(R.id.detailInfo)
detailInfo.apply {
    icon = ContextCompat.getDrawable(context, R.drawable.ic_warning)
    title = "Important Notice"
    description = "This is a detailed description of the information being displayed."
    hasAction = true
    hasDescIcon = true
    descIcon = ContextCompat.getDrawable(context, R.drawable.ic_help)
    delegate = object : DetailInformationADelegate {
        override fun onAction1Clicked(component: DetailInformationA) {
            // Handle first action button click
        }

        override fun onAction2Clicked(component: DetailInformationA) {
            // Handle second action button click
        }

        override fun onItemClick(component: DetailInformationA) {
            // Handle root container click
        }

        override fun onDescIconClick(component: DetailInformationA) {
            // Handle description icon click
        }
        
        override fun onUrlClicked(component: DetailInformationA, url: String) {
            // Custom handling (analytics, in-app browser, etc.)
        }
    }
}
```

---

## Example Usage
```xml
<com.edts.components.detail.information.DetailInformationA
    android:id="@+id/detailInfo"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:infoIcon="@drawable/ic_info"
    app:infoTitle="Order Details"
    app:infoDescription="Your order is on the way."
    app:hasAction="true"/>
```

Assign delegate in code:

```kotlin
detailInfo.delegate = object : DetailInformationADelegate {
    override fun onAction1Clicked(component: DetailInformationA) {
        // Handle Action 1
    }

    override fun onAction2Clicked(component: DetailInformationA) {
        // Handle Action 2
    }
}
```

---

## Properties Reference

### Content Properties

| Property      | Type                          | Default | Description                        |
|---------------| ----------------------------- | ------- | ---------------------------------- |
| `icon`        | `Drawable?`                   | `null`  | Sets the start icon.               |
| `title`       | `CharSequence?`               | `null`  | Title text displayed next to icon. |
| `description` | `CharSequence?`               | `null`  | Supporting description text.       |
| `hasAction`   | `Boolean`                     | `false` | Whether action buttons are shown.  |
| `hasDescIcon` | `Boolean`                     | `false` | Whether description icon is shown. |
| `descIcon`    | `Drawable?`                   | `null`  | Sets the description icon.         |
| `urlInfo`     | `String?`                   | `null`  | Optional URL. When set, the component becomes clickable and opens the link on tap.         |

### Content Properties

| `actionButton1` | `Button`                      | –       | First action button.               |
| `actionButton2` | `Button`                      | –       | Second action button.              |
| `delegate`      | `DetailInformationADelegate?` | `null`  | Delegate for button, root, and icon clicks. |

### Methods

| Method                           | Parameters | Description                                                 |
| -------------------------------- | ---------- | ----------------------------------------------------------- |
| `updateDescriptionConstraints()` | –          | Updates constraints depending on whether actions are shown. |

### XML Attributes

| Attribute         | Format      | Description                       |
| ----------------- | ----------- | --------------------------------- |
| `infoIcon`        | `reference` | Sets the left icon drawable       |
| `infoTitle`       | `string`    | Sets the title text               |
| `infoDescription` | `string`    | Sets the description text         |
| `hasAction`       | `boolean`   | Shows or hides the action buttons |
| `hasDescIcon`     | `boolean`   | Shows or hides the description icon |
| `descIcon`        | `reference` | Sets the description icon drawable   |
| `urlInfo`        | `string` | Optional URL. When set, the component opens this link on tap   |

---

## Usage Examples

### Basic Setup

```kotlin
detailInfo.apply {
    icon = ContextCompat.getDrawable(context, R.drawable.ic_success)
    title = "Success"
    description = "Your operation completed successfully."
    hasAction = false
}
```

### With Action Buttons
```kotlin
detailInfo.apply {
    title = "Update Available"
    description = "A new version of the app is available. Would you like to update now?"
    hasAction = true
    urlInfo = "https://google.com"
    
    // Configure action buttons
    actionButton1.text = "Update Now"
    actionButton2.text = "Later"
    
    delegate = object : DetailInformationADelegate {
        override fun onAction1Clicked(component: DetailInformationA) {
            // Start update process
        }
        
        override fun onAction2Clicked(component: DetailInformationA) {
            // Dismiss or schedule reminder
        }
    }
}
```
---

## Customization Examples

### Dynamic Content Update
```kotlin
// Update content dynamically
detailInfo.title = "New Title"
detailInfo.description = "Updated description text"
detailInfo.icon = ContextCompat.getDrawable(context, R.drawable.ic_updated)

// Show/hide actions based on state
detailInfo.hasAction = shouldShowActions
```

### With Description Icon
```kotlin
detailInfo.apply {
    title = "Help Information"
    description = "Tap the icon for more details"
    hasDescIcon = true
    descIcon = ContextCompat.getDrawable(context, R.drawable.ic_info_circle)

    delegate = object : DetailInformationADelegate {
        override fun onDescIconClick(component: DetailInformationA) {
            // Show help dialog or additional info
        }

        override fun onItemClick(component: DetailInformationA) {
            // Handle container click
        }

        // Other delegate methods...
    }
}
```

---

## Best Practices
| ✅ Do | ❌ Don't |
|------|----------|
| Use clear, concise titles (3-5 words)| Use overly long titles that might truncate on small screens |
| Provide meaningful descriptions that add context | Repeat title information in the description field |
| Set hasAction = false when no user action is needed| Show action buttons without clear purpose or handlers |
| Implement delegate callbacks for all action buttons | Leave action buttons without proper click handling |

---

> **⚠️ Note**: This component automatically manages view visibility and layout constraints based on content. When ```hasAction``` changes from ```false``` to ```true```, the description constraints automatically adjust to maintain proper spacing. Always test with different content combinations and screen sizes to ensure optimal layout.



