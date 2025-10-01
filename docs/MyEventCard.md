# My Event Card

`MyEventCard` is a custom Android view component that extends `MaterialCardView`. It is designed to provide a standardized and reusable layout for displaying event information in a clear and concise card format. The component encapsulates the structure for showing an event's date, type, title, time, and a status badge, making it easy to populate event lists within an application.

## Visual Anatomy

![My Event Card](https://res.cloudinary.com/fauzanspratama/image/upload/v1759220044/My_Event_Card_jpk3ms.png)

| Element | Description |
| :------ | :---------- |
| **Calendar Card** | Compact date display showing month, date, and day |
| **Event Type** | Event category label (Online Event, Workshop, etc.) |
| **Event Title** | Primary event name with 2-line truncation |
| **Event Time** | Time range for the event |
| **Event Badge** | Status indicator (Registered, Completed, etc.) |

## Key Features
- **Integrated Calendar**: Built-in `EventCalendarCard` for consistent date display
- **Status Badging**: Configurable badges for event registration status
- **Compact Layout**: Optimized space usage with clear information hierarchy
- **Interactive Design**: Clickable card with delegate pattern for event handling
- **Flexible Content**: Dynamic text configuration for all elements
- **Material Design**: Card-based layout with proper elevation and shadows

## XML Implementation

### Main Card Usage
```xml
<com.edts.components.myevent.card.MyEventCard
    android:id="@+id/eventCard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:myEventType="Online Event"
    app:myEventTitle="Simplifying UX Complexity: Bridging the Gap Between Design and Development"
    app:myEventTime="18:00 - 20:00"
    app:month="JUL"
    app:date="24"
    app:day="Rab" />
```

#### XML Attributes
| Attribute      | Format   | Description                               |
| :------------- | :------- | :---------------------------------------- |
| `myEventType`  | `string` | Event category or type label              |
| `myEventTitle` | `string` | Primary event title (supports 2 lines)    |
| `myEventTime`  | `string` | Event time range display                  |
| `month`        | `string` | 3-letter month abbreviation (e.g., "JUL") |
| `date`         | `string` | Date number (e.g., "24")                  |
| `day`          | `string` | 3-letter day abbreviation (e.g., "Rab")   |

> **Note**: Badge properties (`badgeText`, `badgeType`, `badgeSize`, `myEventBadgeVisible`) are declared in the styleable attributes but must be set programmatically using the `setBadgeData()` method.

## Kotlin Implementation

### Basic Configuration
```kotlin
val myEventCard = findViewById<MyEventCard>(R.id.eventCard)

// Set basic event information
myEventCard.apply {
    eventType = "Hybrid Event"
    eventTitle = "Advanced Kotlin Coroutines Masterclass"
    eventTime = "14:00 - 16:30"
    setCalendarData("AUG", "15", "Sel")
}
```

### Badge Configuration
```kotlin
myEventCard.setBadgeData(
    text = "Terdaftar",
    type = EventCardBadge.BadgeType.REGISTERED,
    size = EventCardBadge.BadgeSize.SMALL,
    isVisible = true
)
```

### Event Handling
```kotlin
myEventCard.myEventCardDelegate = object : MyEventCardDelegate {
    override fun onClick(eventCard: MyEventCard) {
        // Handle card click - navigate to event details
        navigateToEventDetails(eventCard.eventTitle)
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
    
    setupEventCards()
}

private fun setupEventCards() {
    binding.eventCard.apply {
        eventType = "Webinar"
        eventTitle = "Building Scalable Android Architecture"
        eventTime = "19:00 - 21:00"
        setCalendarData("SEP", "10", "Kam")
        setBadgeData(
            text = "Completed",
            type = EventCardBadge.BadgeType.COMPLETED,
            size = EventCardBadge.BadgeSize.SMALL
        )
        
        myEventCardDelegate = object : MyEventCardDelegate {
            override fun onClick(eventCard: MyEventCard) {
                showEventDetails(eventCard.eventTitle)
            }
        }
    }
}
```

## Do's and Don'ts

### ✅ Do's
- Use for displaying user's registered/attended events
- Provide concise but descriptive event titles
- Use appropriate badge types for event status
- Ensure calendar data uses consistent 3-letter abbreviations
- Handle card clicks for event detail navigation
- Test with various title lengths for proper truncation

### ❌ Don'ts
- Use for event discovery or browsing (use simpler cards)
- Override the compact layout with custom styling
- Use long text in badges that breaks the layout
- Ignore the badge visibility for better information hierarchy
- Forget to handle RecyclerView recycling properly
- Use inconsistent date formatting

## Performance Considerations

### RecyclerView Usage
```kotlin
class EventAdapter : RecyclerView.Adapter<EventViewHolder>() {
    
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        
        holder.binding.eventCard.apply {
            eventType = event.type
            eventTitle = event.title
            eventTime = event.timeRange
            setCalendarData(event.month, event.date, event.day)
            setBadgeData(event.badgeText, event.badgeType, EventCardBadge.BadgeSize.SMALL)
            
            myEventCardDelegate = object : MyEventCardDelegate {
                override fun onClick(eventCard: MyEventCard) {
                    onEventClicked(holder.bindingAdapterPosition)
                }
            }
        }
    }
    
    override fun onViewRecycled(holder: EventViewHolder) {
        holder.binding.eventCard.myEventCardDelegate = null
        super.onViewRecycled(holder)
    }
}
```

## Material Design Styling

The component implements Material Design with:

- **Card Layout**:
  - 8dp corner radius for modern appearance
  - 1dp elevation with dynamic shadows
  - Subtle stroke border using `colorStrokeSubtle`
  - 12dp padding for content spacing

- **Typography Hierarchy**:
  - Event Type: `l3Regular` with tertiary color
  - Event Title: `p1SemiBold` with primary color (2-line max)
  - Event Time: `l3Regular` with tertiary color

- **Calendar Card**:
  - Fixed 56dp width for consistency
  - 6dp corner radius
  - Vertical layout with centered text
  - Day label with tertiary background

- **Color Scheme**:
  - Primary text: `colorForegroundPrimary`
  - Secondary text: `colorForegroundTertiary`
  - Day background: `colorBackgroundTertiary`

### Custom Styling
```xml
<style name="Widget.Desklab.MyEventCard" parent="Widget.Material3.CardView">
    <item name="cardBackgroundColor">?attr/colorBackgroundPrimary</item>
    <item name="strokeColor">?attr/colorStrokeSubtle</item>
    <item name="cardElevation">1dp</item>
    <item name="cardCornerRadius">8dp</item>
</style>
```

---

>[!Note]
>This component is specifically designed for "My Events" sections where users need to see events they've registered for or attended. The compact layout efficiently displays essential information while the badge system provides quick status recognition. Perfect for event history, registered events lists, and upcoming events displays.
