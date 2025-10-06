# Event Notification Card

The `EventNotificationCard` is a dedicated UI component designed to notify an employee about an invitation to an event. It presents information in a structured and easily scannable format, ensuring the user can quickly understand the notification's context. The card consists of several key elements: an icon, an event type label, a title, a detailed description, and an optional call-to-action button.

## Visual Anatomy
The component is a self-contained card with a clean layout, suitable for display in lists or feeds.

![Event Notification Card](https://res.cloudinary.com/fauzanspratama/image/upload/v1759290714/Event_Notification_Card_pscvuc.png)

| Element | Description |
| :------ | :---------- |
| **Icon** | Circular icon indicating event type with subtle background |
| **Event Type** | Badge showing event category (GENERAL EVENT, PEOPLE DEVELOPMENT, etc.) |
| **Title** | Primary event name using `l2SemiBold` typography |
| **Description** | Detailed event information using `p2Regular` typography |
| **Action Button** | Interactive button for accepting invitations or other actions |

## Key Features
- **Multiple Event Types**: Supports GENERAL_EVENT, PEOPLE_DEVELOPMENT, and EMPLOYEE_BENEFIT with automatic styling
- **Dual Interaction**: Separate delegates for card clicks and button clicks
- **Flexible Visibility**: Configurable button visibility and text
- **Material Design**: Card-based layout with proper elevation and shadows
- **Custom Icon Support**: Embedded EventNotificationIcon component with circular background
- **Theme Integration**: Uses app theme colors and typography

## Event Types

| Event Type | Use Case Description |
| :--- | :--- |
| **GENERAL_EVENT** | Company-wide events, town halls, social gatherings, announcements, and general corporate communications |
| **PEOPLE_DEVELOPMENT** | Training sessions, workshops, skill development programs, leadership training, and professional growth activities |
| **EMPLOYEE_BENEFIT** | Benefit announcements, healthcare programs, wellness initiatives, insurance updates, and employee welfare programs |

## XML Implementation

### Main Card Usage
```xml
<com.edts.components.notification.EventNotificationCard
    android:id="@+id/eventNotification"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:notificationTitle="EDTS Town-Hall 2025: Power of Change"
    app:notificationDescription="Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda."
    app:notificationButtonText="Terima Undangan"
    app:notificationButtonVisible="true"
    app:notificationEventType="GENERAL_EVENT" />
```

#### XML Attributes
| Attribute | Format | Description |
| :-------- | :----- | :---------- |
| `notificationTitle` | `string` | Primary event title text |
| `notificationDescription` | `string` | Detailed event description |
| `notificationButtonText` | `string` | Action button label text |
| `notificationButtonVisible` | `boolean` | Show/hide action button |
| `notificationEventType` | `string` | Event type: GENERAL_EVENT, PEOPLE_DEVELOPMENT, EMPLOYEE_BENEFIT |

### Icon Component Usage
```xml
<com.edts.components.notification.EventNotificationIcon
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:notificationIcon="@drawable/ic_custom_event" />
```

#### Icon XML Attributes
| Attribute | Format | Description |
| :-------- | :----- | :---------- |
| `notificationIcon` | `reference` | Drawable resource for the icon |

## Kotlin Implementation

### Basic Configuration
```kotlin
val notificationCard = findViewById<EventNotificationCard>(R.id.eventNotification)

// Set content dynamically
notificationCard.apply {
    title = "Team Building Workshop"
    description = "Join us for team activities and bonding exercises next Friday"
    buttonText = "Confirm Attendance"
    isButtonVisible = true
    eventType = EventNotificationCard.EventType.PEOPLE_DEVELOPMENT
}
```

### Event Type Management
```kotlin
// Set event type with automatic UI updates
notificationCard.eventType = EventNotificationCard.EventType.EMPLOYEE_BENEFIT

// Or from string
notificationCard.eventType = EventNotificationCard.EventType.fromString("GENERAL_EVENT")
```

### Event Handling with Delegate
```kotlin
notificationCard.delegate = object : EventNotificationCardDelegate {
    override fun onButtonClick(notificationCard: EventNotificationCard) {
        // Handle button tap (accept invitation, etc.)
        showConfirmationDialog()
    }

    override fun onCardClick(notificationCard: EventNotificationCard) {
        // Handle entire card tap (view details, etc.)
        navigateToEventDetails()
    }
}
```

### View Binding Implementation
```kotlin
private lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    setupNotificationCard()
}

private fun setupNotificationCard() {
    binding.eventNotification.apply {
        title = "Annual Company Party"
        description = "Celebrate our achievements together at the grand ballroom"
        buttonText = "RSVP Now"
        eventType = EventNotificationCard.EventType.GENERAL_EVENT
        
        delegate = object : EventNotificationCardDelegate {
            override fun onButtonClick(notificationCard: EventNotificationCard) {
                handleRsvp(notificationCard.title)
            }

            override fun onCardClick(notificationCard: EventNotificationCard) {
                showEventDetails(notificationCard.title)
            }
        }
    }
}
```

## Do's and Don'ts

### ✅ Do's
- Use for event invitations and announcements
- Provide clear, concise titles and descriptions
- Set appropriate event types for consistent styling
- Handle both card and button interactions meaningfully
- Use actionable button text ("RSVP", "Confirm", "Learn More")
- Test with different content lengths for proper layout

### ❌ Don'ts
- Use for non-event notifications
- Override the event type styling unnecessarily
- Use vague button labels like "Click Here"
- Ignore accessibility for screen readers
- Forget to handle configuration changes
- Use excessively long titles that truncate

## Performance Considerations

### RecyclerView Usage
```kotlin
class NotificationAdapter : RecyclerView.Adapter<NotificationViewHolder>() {
    
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        
        holder.binding.eventNotification.apply {
            title = notification.title
            description = notification.description
            buttonText = notification.buttonText
            eventType = notification.eventType
            
            delegate = object : EventNotificationCardDelegate {
                override fun onButtonClick(notificationCard: EventNotificationCard) {
                    onButtonClicked(holder.bindingAdapterPosition)
                }

                override fun onCardClick(notificationCard: EventNotificationCard) {
                    onCardClicked(holder.bindingAdapterPosition)
                }
            }
        }
    }
    
    override fun onViewRecycled(holder: NotificationViewHolder) {
        holder.binding.eventNotification.delegate = null
        super.onViewRecycled(holder)
    }
}
```

## Material Design Styling

The component implements Material Design with:

- **Card Layout**: 
  - 12dp corner radius
  - 1dp elevation with dynamic shadows
  - Subtle stroke border using `colorStrokeSubtle`
  - 12dp padding for content spacing

- **Typography**:
  - Title: `l2SemiBold` for prominence
  - Description: `p2Regular` for readability
  - Event Type: `l4Medium` with all caps for badge-like appearance

- **Icon System**:
  - Circular `MaterialCardView` container
  - 12dp icon size with 4dp margin
  - Tertiary background color
  - Subtle stroke border

- **Button Integration**:
  - Uses app's button component with `PRIMARY` type
  - `MD` size for balanced proportions
  - Automatic ripple and interaction states

- **Color Scheme**:
  - Title: `colorForegroundPrimary`
  - Description: `colorForegroundSecondary` 
  - Event Type: `colorForegroundTertiary`
  - Icons: Theme-based tinting

### Custom Styling
```xml
<style name="Widget.Desklab.EventNotificationCard" parent="Widget.Material3.CardView">
    <item name="cardBackgroundColor">?attr/colorBackgroundPrimary</item>
    <item name="strokeColor">?attr/colorStrokeSubtle</item>
    <item name="cardElevation">1dp</item>
    <item name="cardCornerRadius">12dp</item>
</style>
```

---

>[!Note]
>This component is designed for event notification systems within Desklab apps. It works well in lists, detail screens, and as standalone notification items. The dual delegate pattern allows for flexible interaction handling based on your app's navigation patterns.
