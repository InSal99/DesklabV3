# Header 

| Feature / Variation    | Preview                                                                                                                                     |
|------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| Header                 | ![←  Title / Subtitle  ⋮](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1760336011/h_full_header_left_title_subtitle_right_lv7cmp.gif) |
| Header With Tab        | ![Title / Tab  ⋮](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1766549959/ScreenRecording2025-12-24at11.04.22-ezgif.com-video-to-gif-converter_hbrvmv.gif)                           |
| No Left Button         | ![Title / Subtitle  ⋮](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1760336011/h_no_left_button_ee3yx8.gif)                           |
| No Left & Right Button | ![←  Title / Subtitle](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287624/h_no_left_right_button_cbzwwb.png)                     |
| Title Only             | ![Large Title  ⋮](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287625/h_title_only_a24xxo.png)                                                                                                                         |
| Title With Left Button | ![←  Title](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1760336010/h_title_with_left_button_jq90ji.gif)                              |

## Overview

*A flexible header component featuring optional left/right action buttons, a title with dynamic text styling, and an optional subtitle. Automatically adjusts title typography based on visible elements to optimize visual hierarchy.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.header.Header
    android:id="@+id/header"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Screen Title"
    app:sectionSubtitleText="Optional subtitle text"
    app:showLeftButton="true"
    app:showSectionTitle="true"
    app:showSectionSubtitle="true"
    app:showRightButton="true"
    app:rightButtonSrc="@drawable/ic_more" />
```

### 2. Initialize in Code

```kotlin
val header = findViewById<Header>(R.id.header)

// Set up delegate for button clicks
header.delegate = object : HeaderDelegate {
    override fun onLeftButtonClicked() {
        // Handle back/left navigation
        finish()
    }
    
    override fun onRightButtonClicked() {
        // Handle right action (e.g., show menu)
        showOptionsMenu()
    }
}
```

## Display Modes (If Applicable)

| Mode Name | Configuration | Description | Use Case |
| --------- | ------------- | ----------- | -------- |
| `Full Header` | All elements visible | Standard header with navigation and actions | Detail screens with navigation |
| `Large Title` | No left button, no subtitle | Prominent title display | Top-level screens, home pages |
| `Title Only` | Only title visible | Minimal header | Simple informational screens |

```kotlin
// Configure for large title mode
header.apply {
    showLeftButton = false
    showSectionSubtitle = false
}
```

## Properties Reference

### Text Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `sectionTitleText` | `String` | `""` | Main header title text |
| `sectionSubtitleText` | `String` | `""` | Secondary subtitle text below title |

### Visibility Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `showLeftButton` | `Boolean` | `true` | Controls left button visibility (typically back) |
| `showSectionTitle` | `Boolean` | `true` | Controls title text visibility |
| `showSectionSubtitle` | `Boolean` | `true` | Controls subtitle text visibility |
| `showRightButton` | `Boolean` | `true` | Controls right button visibility (typically actions) |
| `app:showShadow` | `Boolean` | `false` | Controls elevated shadow appearance |
| `app:showBackgroundColor` | `Boolean` | `false` | Controls background color visibility |
| `app:showTab` | `Boolean` | `false` | Controls integrated tab component visibility |

### Icon Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `rightButtonSrc` | `@DrawableRes Int` | `-1` | Drawable resource for right action button |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `delegate` | `HeaderDelegate?` | `null` | Click event handler interface |

## Data Models (If Applicable)

### HeaderDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onLeftButtonClicked()` | None | ✅ | Called when left button is clicked |
| `onRightButtonClicked()` | None | ✅ | Called when right button is clicked |

```kotlin
val delegate = object : HeaderDelegate {
    override fun onLeftButtonClicked() {
        // Navigate back
        navController.navigateUp()
    }
    
    override fun onRightButtonClicked() {
        // Show menu or perform action
        showPopupMenu()
    }
}
```

## Usage Examples

```kotlin
// Full header with all elements
binding.header.apply {
    sectionTitleText = "Profile Settings"
    sectionSubtitleText = "Manage your account preferences"
    showLeftButton = true
    showSectionTitle = true
    showSectionSubtitle = true
    showRightButton = true
    rightButtonSrc = R.drawable.ic_more
    
    delegate = object : HeaderDelegate {
        override fun onLeftButtonClicked() {
            finish()
        }
        
        override fun onRightButtonClicked() {
            showSettingsMenu()
        }
    }
}

// Large title mode (no left button, no subtitle)
binding.headerLarge.apply {
    sectionTitleText = "Home"
    showLeftButton = false
    showSectionSubtitle = false
    showRightButton = true
    rightButtonSrc = R.drawable.ic_search
}

// Title only (minimal)
binding.headerMinimal.apply {
    sectionTitleText = "Information"
    showLeftButton = false
    showSectionSubtitle = false
    showRightButton = false
}

// Dynamic updates
fun updateHeader(title: String, hasBackButton: Boolean) {
    binding.header.apply {
        sectionTitleText = title
        showLeftButton = hasBackButton
    }
}

// Header with integrated tabs
binding.headerWithTabs.apply {
    sectionTitleText = "Categories"
    showSectionSubtitle = false
    showLeftButton = true
    showRightButton = false
    showTab = true
    showShadow = true
    showBackgroundColor = true

    // Configure the integrated tab component
    tabView.setTabs(
        listOf(
            TabData("Electronics", null, false),
            TabData("Clothing", "NEW", true),
            TabData("Home", null, false)
        )
    )

    tabView.setOnTabClickListener { position, text ->
        loadCategoryContent(text)
    }

    delegate = object : HeaderDelegate {
        override fun onLeftButtonClicked() {
            finish()
        }
        override fun onRightButtonClicked() {}
    }
}

// Header with shadow and background
binding.headerElevated.apply {
    sectionTitleText = "Featured Products"
    showShadow = true
    showBackgroundColor = true
    showLeftButton = false
    showRightButton = false
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Full header with left, title, subtitle, and right button -->
<com.edts.components.header.Header
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Profile Settings"
    app:sectionSubtitleText="Manage your account preferences"
    app:rightButtonSrc="@drawable/ic_star"
    app:showLeftButton="true"
    app:showRightButton="true"
    app:showSectionSubtitle="true"
    app:showSectionTitle="true" />

<!-- No left button (cleaner look for top-level screens) -->
<com.edts.components.header.Header
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Profile Settings"
    app:sectionSubtitleText="Manage your account preferences"
    app:rightButtonSrc="@drawable/ic_star"
    app:showLeftButton="false"
    app:showRightButton="true"
    app:showSectionSubtitle="true"
    app:showSectionTitle="true" />

<!-- No action buttons (informational header) -->
<com.edts.components.header.Header
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Profile Settings"
    app:sectionSubtitleText="Manage your account preferences"
    app:showLeftButton="false"
    app:showRightButton="false"
    app:showSectionSubtitle="true"
    app:showSectionTitle="true" />

<!-- Large title only (no left, no subtitle) -->
<com.edts.components.header.Header
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Profile Settings"
    app:showLeftButton="false"
    app:showRightButton="false"
    app:showSectionSubtitle="false"
    app:showSectionTitle="true" />

<!-- Standard with left button, no subtitle -->
<com.edts.components.header.Header
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Profile Settings"
    app:rightButtonSrc="@drawable/ic_star"
    app:showLeftButton="true"
    app:showRightButton="false"
    app:showSectionSubtitle="false"
    app:showSectionTitle="true" />
    
<!-- Header with shadow and background -->
<com.edts.components.header.Header
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Featured"
    app:showShadow="true"
    app:showBackgroundColor="true"
    app:showLeftButton="false"
    app:showRightButton="false" />

<!-- Header with integrated tabs -->
<com.edts.components.header.Header
    android:id="@+id/headerWithTabs"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:sectionTitleText="Categories"
    app:showTab="true"
    app:showShadow="true"
    app:showBackgroundColor="true" />
```

### Programmatic Customization

```kotlin
// Runtime property updates
header.apply {
    sectionTitleText = "Updated Title"
    sectionSubtitleText = "New subtitle"
    rightButtonSrc = R.drawable.ic_new_icon
}

// Toggle visibility dynamically
header.showLeftButton = canNavigateBack()
header.showRightButton = hasActions()
header.showSectionSubtitle = showDescription

// Conditional styling based on context
if (isTopLevelScreen) {
    header.apply {
        showLeftButton = false
        showSectionSubtitle = false
    }
}
```

## Performance Considerations

- **ViewBinding** — Uses ViewBinding for efficient view access and type safety
- **Conditional Rendering** — Shadow and background calculations only performed when visibility states change
- **Smart Property Setters** — Custom setters update only affected views, preventing unnecessary full layout passes

## Animation Details (If Applicable)

| Animation Type | Duration | Interpolator | Description |
| -------------- | -------- | ------------ | ----------- |
| `Visibility Changes` | System default | N/A | Standard View visibility transitions |
| `Text Style Transitions` | Instant | N/A | Text appearance updates immediately on configuration change |

## Best Practices

| ✅ Do                                              | ❌ Don't |
|---------------------------------------------------| ------- |
| Keep titles concise (1-4 words)                   | Use long, multi-line titles |
| Use subtitles for context, not repetition         | Repeat title information in subtitle |
| Implement HeaderDelegate for interactive headers  | Leave buttons visible without click handlers |
| Hide left button on top-level screens             | Show back button on root navigation level |
| Use appropriate right button icons for actions    | Use ambiguous or unclear action icons |
| Test all visibility combinations                  | Assume all configurations look good without testing |
| Use `showShadow` for on-scroll state              | Apply shadow to all headers unnecessarily |
| Access `tabView` property for tab configuration   | Try to add separate Tab components manually |
