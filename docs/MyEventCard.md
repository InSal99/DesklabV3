# My Event Card


## Overview
`MyEventCard` is a custom Android view component that extends `MaterialCardView`. It is designed to provide a standardized and reusable layout for displaying event information in a clear and concise card format. The component encapsulates the structure for showing an event's date, type, title, time, and a status badge, making it easy to populate event lists within an application.


## Visual Breakdown
The `MyEventCard` component is composed of several key UI elements organized within a `androidx.constraintlayout.widget.ConstraintLayout`:

1. **EventCalendarCard (Left)**: A dedicated custom view on the left side that displays the date of the event, broken down into month, date, and day.
2. **Event Details (Right)**:
    * **Event Type (`tvEventType`)**: A `MaterialTextView` at the top-left of the details section to specify the type of event (e.g., "Online Event").
    * **Event Badge (`eventCardBadge`)**: A custom `EventCardBadge` view at the top-right, used to display the event's status, such as "Terdaftar".
    * **Event Title (`tvEventTitle`)**: A prominent `MaterialTextView` in the center for the main title of the event. It supports up to two lines and will truncate with an ellipsis if the text is too long.
    * **Event Time (`tvEventTime`)**: A `MaterialTextView` at the bottom, displaying the event's time range.


## Key Features
* **Encapsulated Design**: Combines multiple views into a single, cohesive component for easy reuse.
* **XML Configuration**: Allows for static configuration of most text properties directly in your layout XML files.
* **Programmatic Control**: Offers a simple API to dynamically set event data and badge properties from your Kotlin or Java code.
* **Click Event Handling**: Utilizes a delegate pattern (`MyEventCardDelegate`) to easily manage click actions on the card. The `performClick` method is overridden to call the delegate.
* **Material Design Compliance**: Built upon `MaterialCardView`, it's styled with a corner radius, stroke, elevation, and shadow colors for a modern look and feel.


## XML Implementation

#### Attributes
You can customize the `MyEventCard` directly from your XML layout file using the following attributes defined in `attrs_my_event_card.xml`:

| Attribute          | Format   | Description                                                   |
| :----------------- | :------- | :------------------------------------------------------------ |
| `app:myEventType`  | `string` | Sets the text for the event type.                             |
| `app:myEventTitle` | `string` | Sets the main title for the event.                            |
| `app:myEventTime`  | `string` | Sets the time text for the event.                             |
| `app:month`        | `string` | Sets the month text (e.g., "JUL") in the `EventCalendarCard`. |
| `app:date`         | `string` | Sets the date text (e.g., "24") in the `EventCalendarCard`.   |
| `app:day`          | `string` | Sets the day text (e.g., "Rab") in the `EventCalendarCard`.   |

#### Example
```XML
<com.edts.components.myevent.card.MyEventCard
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/my_event_card_example"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:myEventType="Online Event"
    app:myEventTitle="Simplifying UX Complexity"
    app:myEventTime="18:00 - 20:00"
    app:month="JUL"
    app:date="24"
    app:day="Rab" />
```

> [!note]
> Badge properties (`badgeText`, `badgeType`, `badgeSize`, `myEventBadgeVisible`) are declared in the styleable attributes but are not read in the `MyEventCard`'s `applyStyledAttributes` method. They must be set programmatically using the `setBadgeData()` method.


## Programmatically Usage
The `MyEventCard` can also be fully configured and controlled from from your code.

#### Properties and Methods
```Kotlin
val myEventCard = findViewById<MyEventCard>(R.id.my_event_card_example)

// Set Text Properties
myEventCard.eventType = "Hybrid"
myEventCard.eventTitle = "Advanced Android Development"
myEventCard.eventTime = "09:00 - 17:00"

// Set Calendar Data
myEventCard.setCalendarData(month = "SEP", date = "26", day = "Jum")

// Set Badge Data
myEventCard.setBadgeData(
    text = "Terdaftar",
    type = EventCardBadge.BadgeType.REGISTERED,
    size = EventCardBadge.BadgeType.SMALL,
    isVisible = true
)
```

#### Click Handling
Assign an implementation of `MyEventCardDelegate` to handle clicks.

```Kotlin
myEventCard.myEventCardDelegate = object : MyEventCardDelegate {
    override fun onClick(eventCard: MyEventCard) {
        // Handle click
    }
}
```

