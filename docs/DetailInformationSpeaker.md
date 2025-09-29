# Detail Information Speaker

## Overview
`DetailInformationSpeaker` is a simple UI component for displaying a **speaker profile** consisting of an **image** and a **name**.  
It’s designed for event/session details, team members, or any case where a person’s name and avatar need to be displayed.

## Preview
![detail-speaker](...)

---

## XML Usage
```xml
<com.edts.components.detail.information.DetailInformationSpeaker
    android:id="@+id/speakerInfo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:speakerImage="@drawable/speaker_photo"
    app:speakerName="John Doe"/>
```

---

## Public Methods

| Method               | Parameters            | Description                                                            |
| -------------------- | --------------------- | ---------------------------------------------------------------------- |
| `setImageResource()` | `resId: Int`          | Sets the speaker’s image by resource ID.                               |
| `setImageDrawable()` | `drawable: Drawable?` | Sets the speaker’s image by drawable.                                  |
| `setName()`          | `name: String`        | Sets the speaker’s display name.                                       |
| `getName()`          | –                     | Returns the current speaker name.                                      |
| `getTextView()`      | –                     | Returns the `TextView` used for the name (for advanced customization). |

---

## XML Attributes

| Attribute      | Format      | Description                                         |
| -------------- | ----------- | --------------------------------------------------- |
| `speakerImage` | `reference` | Speaker image (defaults to placeholder if not set). |
| `speakerName`  | `string`    | Name text displayed under/next to the image.        |

---

## Example (Programmatic)
```kotlin
val speaker = findViewById<DetailInformationSpeaker>(R.id.speakerInfo)

speaker.setImageResource(R.drawable.speaker_photo)
speaker.setName("Jane Smith")

val currentName = speaker.getName()
```

---

## Best Practices

| ✅ Do                                                         | ❌ Don’t                                        |
| ------------------------------------------------------------ | ---------------------------------------------- |
| Use `speakerImage` for real speaker photos                   | Leave `speakerImage` empty without a fallback  |
| Keep `speakerName` concise (e.g., “First Last”)              | Use long sentences or descriptions in the name |
| Provide a placeholder image for missing avatars              | Show a broken image resource                   |
| Use `getTextView()` for advanced styling only when necessary | Override internal layout directly              |


---

> **⚠️ Note**: Ensure speakerName is always set for accessibility and clarity. By default, if no speakerImage is provided, a placeholder avatar will be displayed. Use consistent image sizes across multiple speaker items to maintain a clean layout.