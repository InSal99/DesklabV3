# Event Notification Card

`NotificationCard` is a comprehensive UI component designed to notify employees about various types of events and notifications. It presents information in a structured and easily scannable format, allowing users to quickly understand the notification context and take appropriate actions. The card features an icon, event category label, title, detailed description, and configurable action buttons.

## Visual Anatomy

![Notification Card](https://res.cloudinary.com/fauzanspratama/image/upload/v1759290714/Event_Notification_Card_pscvuc.png)

| Element | Description |
| :------ | :---------- |
| **Notification Icon** | Circular icon with event-specific symbol and subtle background |
| **Event Category** | Category label (GENERAL EVENT, PEOPLE DEVELOPMENT, EMPLOYEE BENEFIT, etc.) |
| **Title** | Primary notification title using appropriate typography |
| **Description** | Detailed notification description |
| **Primary Button** | Main interactive button (e.g., "Terima Undangan") |
| **Secondary Button** | Optional secondary button (e.g., "Tolak") |
| **Notification Badge** | Visibility indicator for new notifications |

## Key Features
- **Multiple Event Categories**: Supports 7 event types with automatic styling and icons
- **Dual Button Support**: Configurable primary and secondary buttons
- **Flexible Interaction**: Separate delegates for card clicks and button clicks
- **Material Design**: Card-based layout with proper elevation and shadows
- **Theme Integration**: Uses app theme colors and typography
- **RecyclerView Ready**: Optimized for use in lists and feeds
- **Customizable Visibility**: Control button and badge visibility independently

## Event Categories

| Category | Display Text | Icon | Use Case |
| :--- | :--- | :--- | :--- |
| **GENERAL_EVENT** | "GENERAL EVENT" | `ic_notification_event` | Company-wide events, town halls, social gatherings |
| **PEOPLE_DEVELOPMENT** | "PEOPLE DEVELOPMENT" | `ic_notification_event` | Training sessions, workshops, skill development |
| **EMPLOYEE_BENEFIT** | "EMPLOYEE BENEFIT" | `ic_notification_event` | Benefit announcements, wellness programs |
| **ACTIVITY** | "AKTIVITAS" | `ic_notification_activity` | Activity approvals and updates |
| **LEAVE** | "CUTI" | `ic_notification_leave` | Leave request approvals |
| **SPECIAL_WORK** | "KERJA KHUSUS" | `ic_notification_special_work` | Special work approvals |
| **DELEGATION** | "DELEGASI" | `ic_notification_delegation` | Delegation notifications |

## XML Implementation

### Main Card Usage
```xml
<com.edts.components.notification.NotificationCard
    android:id="@+id/notificationCard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:notificationTitle="Simplifying UX Complexity: Bridging the Gap Between Design and Development"
    app:notificationDescription="Anda diundang pada Rabu, 23 Juli 2025, pukul 15:00 – 17:00 WIB. Segera konfirmasi kehadiran Anda."
    app:notificationPrimaryButtonText="Terima Undangan"
    app:notificationSecondaryButtonText="Tolak"
    app:showNotificationPrimaryButton="true"
    app:showNotificationSecondaryButton="true"
    app:notificationBadgeVisible="true"
    app:notificationCategory="general_event" />
```

### XML Attributes
| Attribute | Format | Description |
| :-------- | :----- | :---------- |
| `notificationTitle` | `string` | Primary notification title text |
| `notificationDescription` | `string` | Detailed notification description |
| `notificationPrimaryButtonText` | `string` | Primary button label (default: "Terima Undangan") |
| `notificationSecondaryButtonText` | `string` | Secondary button label (default: "Tolak") |
| `showNotificationPrimaryButton` | `boolean` | Show/hide primary button (default: true) |
| `showNotificationSecondaryButton` | `boolean` | Show/hide secondary button (default: false) |
| `notificationBadgeVisible` | `boolean` | Show/hide notification badge (default: true) |
| `notificationCategory` | `enum` | Event category: general_event, people_development, employee_benefit, activity, leave, special_work, delegation |

## Kotlin Implementation

### Basic Configuration
```kotlin
val notificationCard = findViewById<NotificationCard>(R.id.notificationCard)

// Set content dynamically
notificationCard.apply {
    notificationTitle = "EDTS Town-Hall 2025: Power of Change"
    notificationDescription = "Anda diundang pada Jumat, 25 Juli 2025, pukul 10:00 – 12:00 WIB. Segera konfirmasi kehadiran Anda."
    primaryButtonText = "Terima Undangan"
    secondaryButtonText = "Tolak"
    isPrimaryButtonVisible = true
    isSecondaryButtonVisible = true
    isBadgeVisible = true
    notificationCategory = NotificationCard.EventCategory.PEOPLE_DEVELOPMENT
}
```

### Event Handling with Delegate
```kotlin
notificationCard.notificationCardDelegate = object : NotificationCardDelegate {
    override fun onCardClick(notificationCard: NotificationCard) {
        // Handle entire card tap (view notification details)
        Toast.makeText(context, "Card clicked: ${notificationCard.notificationTitle}", Toast.LENGTH_SHORT).show()
    }

    override fun onPrimaryButtonClick(notificationCard: NotificationCard) {
        // Handle primary button tap (accept/confirm)
        Toast.makeText(context, "Primary button clicked for: ${notificationCard.notificationTitle}", Toast.LENGTH_SHORT).show()
    }

    override fun onSecondaryButtonClick(notificationCard: NotificationCard) {
        // Handle secondary button tap (reject/decline)
        Toast.makeText(context, "Secondary button clicked for: ${notificationCard.notificationTitle}", Toast.LENGTH_SHORT).show()
    }
}
```

### RecyclerView Adapter Implementation
```kotlin
class EventInvitationAdapter(
    private val notifications: List<EventInvitation>,
    private val onCardClick: (EventInvitation) -> Unit,
    private val onPrimaryButtonClick: (EventInvitation) -> Unit,
    private val onSecondaryButtonClick: (EventInvitation) -> Unit
) : RecyclerView.Adapter<EventInvitationAdapter.NotificationViewHolder>() {

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        
        holder.card.apply {
            notificationTitle = notification.title
            notificationDescription = notification.description
            primaryButtonText = notification.primaryButtonText
            secondaryButtonText = notification.secondaryButtonText
            isPrimaryButtonVisible = notification.isPrimaryButtonVisible
            isSecondaryButtonVisible = notification.isSecondaryButtonVisible
            isBadgeVisible = notification.isBadgeVisible
            notificationCategory = notification.eventCategory

            notificationCardDelegate = object : NotificationCardDelegate {
                override fun onCardClick(notificationCard: NotificationCard) {
                    onCardClick(notification)
                }

                override fun onPrimaryButtonClick(notificationCard: NotificationCard) {
                    onPrimaryButtonClick(notification)
                }

                override fun onSecondaryButtonClick(notificationCard: NotificationCard) {
                    onSecondaryButtonClick(notification)
                }
            }
        }
    }

    override fun onViewRecycled(holder: NotificationViewHolder) {
        holder.card.notificationCardDelegate = null
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
    primaryButtonText = "Terima Undangan",
    secondaryButtonText = "Tolak",
    isPrimaryButtonVisible = true,
    isSecondaryButtonVisible = true,
    isBadgeVisible = true,
    eventCategory = NotificationCard.EventCategory.GENERAL_EVENT
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
            onPrimaryButtonClick = { notification ->
                Toast.makeText(requireContext(), "Primary button clicked for: ${notification.title}", Toast.LENGTH_SHORT).show()
            },
            onSecondaryButtonClick = { notification ->
                Toast.makeText(requireContext(), "Secondary button clicked for: ${notification.title}", Toast.LENGTH_SHORT).show()
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
                eventCategory = NotificationCard.EventCategory.GENERAL_EVENT
            ),
            EventInvitation(
                title = "Persetujuan Cuti",
                description = "Anga Kho Meidy telah menyetujui permintaan cuti Anda untuk tanggal 31 Agu 2024 - 2 Jan 2025",
                eventCategory = NotificationCard.EventCategory.LEAVE,
                isPrimaryButtonVisible = false
            )
        )
    }
}
```

## Do's and Don'ts

### ✅ Do's
- Use for various notification types including events, approvals, and delegations
- Provide clear, descriptive titles and detailed descriptions
- Set appropriate event categories for consistent styling and icons
- Handle card, primary button, and secondary button interactions meaningfully
- Use "Terima Undangan" as default primary button text for event invitations
- Use "Tolak" as default secondary button text for rejection actions
- Test with different content lengths for proper layout
- Clear delegate references in RecyclerView onViewRecycled

### ❌ Don'ts
- Use for non-notification purposes
- Override the event category styling unnecessarily
- Use vague button labels - stick to established defaults
- Ignore accessibility for screen readers
- Forget to handle configuration changes
- Use excessively long titles that truncate improperly
- Leave delegate references set when views are recycled

### ViewHolder Pattern
```kotlin
class NotificationViewHolder(val card: NotificationCard) : RecyclerView.ViewHolder(card) {

    fun bind(
        item: EventInvitation,
        onCardClick: (EventInvitation) -> Unit,
        onPrimaryButtonClick: (EventInvitation) -> Unit,
        onSecondaryButtonClick: (EventInvitation) -> Unit
    ) {
        card.apply {
            notificationTitle = item.title
            notificationDescription = item.description
            primaryButtonText = item.primaryButtonText
            secondaryButtonText = item.secondaryButtonText
            isPrimaryButtonVisible = item.isPrimaryButtonVisible
            isSecondaryButtonVisible = item.isSecondaryButtonVisible
            isBadgeVisible = item.isBadgeVisible
            notificationCategory = item.eventCategory

            notificationCardDelegate = object : NotificationCardDelegate {
                override fun onCardClick(notificationCard: NotificationCard) {
                    onCardClick(item)
                }

                override fun onPrimaryButtonClick(notificationCard: NotificationCard) {
                    onPrimaryButtonClick(item)
                }

                override fun onSecondaryButtonClick(notificationCard: NotificationCard) {
                    onSecondaryButtonClick(item)
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
  - Title: Primary notification title with emphasis
  - Description: Detailed notification information

- **Icon System**:
  - Different icons for each event category
  - Circular background with proper styling

- **Button Integration**:
  - Primary action button for main actions
  - Secondary button for alternative actions
  - Configurable text and visibility

- **Color Scheme**:
  - Uses theme attributes: `colorBackgroundPrimary`, `colorForegroundPrimary`, etc.
  - Consistent with app design system

### Custom Styling
```xml
<style name="Widget.Desklab.NotificationCard" parent="Widget.Material3.CardView">
    <item name="cardBackgroundColor">?attr/colorBackgroundPrimary</item>
    <item name="strokeColor">?attr/colorStrokeSubtle</item>
    <item name="cardElevation">1dp</item>
    <item name="cardCornerRadius">12dp</item>
</style>
```

## NotificationCardDelegate Interface

```kotlin
interface NotificationCardDelegate {
    fun onCardClick(notificationCard: NotificationCard)
    fun onPrimaryButtonClick(notificationCard: NotificationCard)
    fun onSecondaryButtonClick(notificationCard: NotificationCard)
}
```

---

>[!Note]
>This component is designed for comprehensive notification systems within Desklab apps. It works well in lists, detail screens, and as standalone notification items. The triple delegate pattern allows for flexible interaction handling based on your app's navigation patterns and user workflows.