# Event Notification Card

`EventNotificationCard` is a dedicated UI component designed to notify employees about event invitations. It presents event information in a structured and easily scannable format, allowing users to quickly understand the notification context and take action. The card features an icon, event category label, title, detailed description, and an action button.

## Visual Anatomy

![Event Notification Card](https://res.cloudinary.com/fauzanspratama/image/upload/v1759290714/Event_Notification_Card_pscvuc.png)

| Element | Description |
| :------ | :---------- |
| **Notification Icon** | Circular icon with event symbol and subtle background |
| **Event Category** | Category label (GENERAL EVENT, PEOPLE DEVELOPMENT, EMPLOYEE BENEFIT) |
| **Title** | Primary event name using appropriate typography |
| **Description** | Detailed event invitation description |
| **Action Button** | Interactive button for accepting invitations |

## Key Features
- **Multiple Event Categories**: Supports GENERAL_EVENT, PEOPLE_DEVELOPMENT, and EMPLOYEE_BENEFIT with automatic styling
- **Dual Interaction**: Separate delegates for card clicks and button clicks
- **Flexible Content**: Configurable title, description, button text, and visibility
- **Material Design**: Card-based layout with proper elevation and shadows
- **Theme Integration**: Uses app theme colors and typography
- **RecyclerView Ready**: Optimized for use in lists and feeds

## Event Categories

| Category | Display Text | Use Case |
| :--- | :--- | :--- |
| **GENERAL_EVENT** | "GENERAL EVENT" | Company-wide events, town halls, social gatherings, announcements |
| **PEOPLE_DEVELOPMENT** | "PEOPLE DEVELOPMENT" | Training sessions, workshops, skill development programs |
| **EMPLOYEE_BENEFIT** | "EMPLOYEE BENEFIT" | Benefit announcements, wellness programs, employee welfare initiatives |

## XML Implementation

### Main Card Usage
```xml
<com.edts.components.notification.EventNotificationCard
    android:id="@+id/eventNotification"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:notificationTitle="Simplifying UX Complexity: Bridging the Gap Between Design and Development"
    app:notificationDescription="Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda."
    app:notificationButtonText="Terima Undangan"
    app:notificationButtonVisible="true"
    app:notificationEventCategory="general_event" />
```

### XML Attributes
| Attribute | Format | Description |
| :-------- | :----- | :---------- |
| `notificationTitle` | `string` | Primary event title text |
| `notificationDescription` | `string` | Detailed event description and invitation details |
| `notificationButtonText` | `string` | Action button label text (default: "Terima Undangan") |
| `notificationButtonVisible` | `boolean` | Show/hide action button (default: true) |
| `notificationEventCategory` | `enum` | Event category: general_event, people_development, employee_benefit |

## Kotlin Implementation

### Basic Configuration
```kotlin
val notificationCard = findViewById<EventNotificationCard>(R.id.eventNotification)

// Set content dynamically
notificationCard.apply {
    title = "EDTS Town-Hall 2025: Power of Change"
    description = "Anda diundang pada Jumat, 25 Juli 2025, pukul 10:00 – 12:00 WIB. Segera konfirmasi kehadiran Anda."
    buttonText = "Terima Undangan"
    isButtonVisible = true
    eventCategory = EventNotificationCard.EventCategory.PEOPLE_DEVELOPMENT
}
```

### Event Handling with Delegate
```kotlin
notificationCard.eventNotificationCardDelegate = object : EventNotificationCardDelegate {
    override fun onButtonClick(notificationCard: EventNotificationCard) {
        // Handle button tap (accept invitation)
        Toast.makeText(context, "Button clicked for: ${notificationCard.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onCardClick(notificationCard: EventNotificationCard) {
        // Handle entire card tap (view event details)
        Toast.makeText(context, "Card clicked: ${notificationCard.title}", Toast.LENGTH_SHORT).show()
    }
}
```

### RecyclerView Adapter Implementation
```kotlin
class EventInvitationAdapter(
    private val notifications: List<EventInvitation>,
    private val onCardClick: (EventInvitation) -> Unit,
    private val onButtonClick: (EventInvitation) -> Unit
) : RecyclerView.Adapter<EventInvitationAdapter.NotificationViewHolder>() {

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        
        holder.card.apply {
            title = notification.title
            description = notification.description
            buttonText = notification.buttonText
            isButtonVisible = notification.isButtonVisible
            eventCategory = notification.eventCategory

            eventNotificationCardDelegate = object : EventNotificationCardDelegate {
                override fun onCardClick(notificationCard: EventNotificationCard) {
                    onCardClick(notification)
                }

                override fun onButtonClick(notificationCard: EventNotificationCard) {
                    onButtonClick(notification)
                }
            }
        }
    }

    override fun onViewRecycled(holder: NotificationViewHolder) {
        holder.card.eventNotificationCardDelegate = null
        super.onViewRecycled(holder)
    }
}
```

### Data Model Integration
```kotlin
// Creating EventInvitation data
val eventInvitation = EventInvitation(
    title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
    description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
    buttonText = "Terima Undangan",
    isButtonVisible = true,
    eventCategory = EventNotificationCard.EventCategory.GENERAL_EVENT
)
```

### Fragment Implementation Example
```kotlin
class EventInvitationComponentFragment : Fragment() {

    private fun setupRecyclerView() {
        val notificationList = createSampleData()

        val notificationAdapter = EventInvitationAdapter(
            notifications = notificationList,
            onCardClick = { notification ->
                Toast.makeText(requireContext(), "Card clicked: ${notification.title}", Toast.LENGTH_SHORT).show()
            },
            onButtonClick = { notification ->
                Toast.makeText(requireContext(), "Button clicked for: ${notification.title}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvEventInvitation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(requireContext(), R.dimen.margin_8dp, SpaceItemDecoration.VERTICAL))
            adapter = notificationAdapter
        }
    }

    private fun createSampleData(): List<EventInvitation> {
        return listOf(
            EventInvitation(
                title = "Simplifying UX Complexity: Bridging the Gap Between Design and Development",
                description = "Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventCategory = EventNotificationCard.EventCategory.GENERAL_EVENT
            ),
            EventInvitation(
                title = "EDTS Town-Hall 2025: Power of Change",
                description = "Anda diundang pada Jumat, 25 Juli 2025, pukul 10:00 – 12:00 WIB. Segera konfirmasi kehadiran Anda.",
                eventCategory = EventNotificationCard.EventCategory.PEOPLE_DEVELOPMENT
            )
        )
    }
}
```

## Do's and Don'ts

### ✅ Do's
- Use for event invitations and announcement notifications
- Provide clear, descriptive titles and detailed invitation descriptions
- Set appropriate event categories for consistent styling
- Handle both card and button interactions meaningfully
- Use "Terima Undangan" as default button text for consistency
- Test with different content lengths for proper layout
- Clear delegate references in RecyclerView onViewRecycled

### ❌ Don'ts
- Use for non-event notifications
- Override the event category styling unnecessarily
- Use vague button labels - stick to "Terima Undangan" for invitations
- Ignore accessibility for screen readers
- Forget to handle configuration changes
- Use excessively long titles that truncate improperly
- Leave delegate references set when views are recycled

### ViewHolder Pattern
```kotlin
class NotificationViewHolder(val card: EventNotificationCard) : RecyclerView.ViewHolder(card) {

    fun bind(
        item: EventInvitation,
        onCardClick: (EventInvitation) -> Unit,
        onButtonClick: (EventInvitation) -> Unit
    ) {
        card.apply {
            title = item.title
            description = item.description
            buttonText = item.buttonText
            isButtonVisible = item.isButtonVisible
            eventCategory = item.eventCategory

            eventNotificationCardDelegate = object : EventNotificationCardDelegate {
                override fun onCardClick(notificationCard: EventNotificationCard) {
                    onCardClick(item)
                }

                override fun onButtonClick(notificationCard: EventNotificationCard) {
                    onButtonClick(item)
                }
            }
        }
    }
}
```

## Material Design Styling

The component implements Material Design with:

- **Card Layout**: 
  - 12dp corner radius for modern appearance
  - 1dp elevation with dynamic shadows
  - Subtle stroke border using `colorStrokeSubtle`
  - Proper padding for content spacing

- **Typography Hierarchy**:
  - Event Category: Appropriate styling for category label
  - Title: Primary event name with emphasis
  - Description: Detailed invitation information

- **Icon System**:
  - Uses `ic_notification_event` drawable for all event types
  - Circular background with proper styling

- **Button Integration**:
  - Primary action button for accepting invitations
  - Configurable text and visibility

- **Color Scheme**:
  - Uses theme attributes: `colorBackgroundPrimary`, `colorForegroundPrimary`, etc.
  - Consistent with app design system

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
