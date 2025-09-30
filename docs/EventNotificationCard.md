# Event Notification Card
The `EventNotificationCard` is a dedicated UI component designed to notify an employee about an invitation to an event. It presents information in a structured and easily scannable format, ensuring the user can quickly understand the notification's context. The card consists of several key elements: an icon, an event type label, a title, a detailed description, and an optional call-to-action button.

## Component Preview
The component is a self-contained card with a clean layout, suitable for display in lists or feeds.

## Key Features
- **Structured Layout**: Provides a consistent and clear presentation for event details, including an icon, type, title, description, and an action button.
- **Customizable Content**: Allows for easy modification of the title, description, and button text through both XML attributes and programmatic setters.
- **Event Typing**: Supports predefined event categories (`GENERAL_EVENT`, `PEOPLE_DEVELOPMENT`, `EMPLOYEE_BENEFIT`) to help users distinguish between different types of notifications.
- **Interactive Callbacks**: Utilizes a delegate pattern (`EventNotificationCardDelegate`) to handle user interactions, supporting separate callbacks for clicks on the card itself and on the action button.
- **Conditional Action Button**: The primary action button can be dynamically shown or hidden based on the notification's requirements.

---

## XML Implementation
The `EventNotificationCard` is designed to be used directly within your Kotlin code or XML layouts. Add the component to your layout file and configure its initial appearance using the available custom attributes.

#### Attributes
These attributes can be set on the `EventNotificationCard` in your XML layout.

| Attribute                   | Format   | Description                                                                                                                                |
| :-------------------------- | :------- | :----------------------------------------------------------------------------------------------------------------------------------------- |
| `notificationTitle`         | `String` | Sets the main title of the notification.                                                                                                   |
| `notificationDescription`   | `String` | Sets the descriptive body text of the notification.                                                                                        |
| `notificationButtonText`    | `String` | Sets the text for the action button.                                                                                                       |
| `notificationButtonVisible` | `String` | Determines if the action button is visible (`true`) or hidden (`false`). Defaults to `true`.                                               |
| `notificationEventType`     | `String` | Sets the event type. Accepts "General Event", "People Development", or "Employee Benefit" (case-insensitive). Defaults to `GENERAL_EVENT`. |

#### Example Usage
```XML
<com.edts.components.notification.EventNotificationCard
    android:id="@+id/event_card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:notificationTitle="EDTS Town-Hall 2025: Power of Change"
    app:notificationDescription="Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB."
    app:notificationButtonText="Terima Undangan"
    app:notificationButtonVisible="true"
    app:notificationEventType="General Event" />
```

---

## Programmatically Implementation
You can also access and modify the card's properties programmatically in your Fragment or Activity.

#### Component Properties
| Property          | Type                             | Description                                                                                                                                |
| :---------------- | :------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------- |
| `title`           | `String`                         | Sets the main title of the notification.                                                                                                   |
| `description`     | `String`                         | Sets the descriptive body text of the notification.                                                                                        |
| `buttonText`      | `String`                         | Sets the text for the action button.                                                                                                       |
| `isButtonVisible` | `Boolean`                        | Determines if the action button is visible (`true`) or hidden (`false`). Defaults to `true`.                                               |
| `eventType`       | `EventType`                      | Sets the event type. Accepts "General Event", "People Development", or "Employee Benefit" (case-insensitive). Defaults to `GENERAL_EVENT`. |
| `delegate`        | `EventNotificationCardDelegate?` |                                                                                                                                            |

#### Example Usage
```Kotlin
// In your Fragment or Activity
val eventCard = binding.eventCard

// Update the card's content dynamically
eventCard.title = "New Event Title"
eventCard.description = "This is an updated description for the event."
eventCard.buttonText = "View Details"
eventCard.isButtonVisible = true
eventCard.eventType = EventNotificationCard.EventType.PEOPLE_DEVELOPMENT
```

#### Handing Clicks
To respond to user interactions, your Fragment or Activity must implement the `EventNotificationCardDelegate` interface and set itself as the delegate for the card instance.

```Kotlin
// 1. Implement the delegate in your Fragment or Activity class
class NotificationFragment : Fragment(), EventNotificationCardDelegate {

    private lateinit var binding: FragmentNotificationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. Set the delegate on your card instance
        binding.eventCard.delegate = this //
    }

    // 3. Override the required methods to handle callbacks
    override fun onButtonClick(notificationCard: EventNotificationCard) { //
        // Handle the action button click
        // For example, accept an invitation
        val title = notificationCard.title
        Toast.makeText(context, "Button clicked for: $title", Toast.LENGTH_SHORT).show()
    }

    override fun onCardClick(notificationCard: EventNotificationCard) { //
        // Handle the card click
        // For example, navigate to a details screen
        val title = notificationCard.title
        Toast.makeText(context, "Card clicked for: $title", Toast.LENGTH_SHORT).show()
    }
}
```

