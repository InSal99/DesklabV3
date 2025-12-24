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
- **Integrated Calendar**: Built-in calendar card for consistent date display
- **Status Badging**: Configurable badges for event registration and attendance status
- **Location Types**: Support for offline, online, and hybrid events
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
    app:myEventLocation="online"
    app:myEventType="registered"
    app:myEventTitle="Simplifying UX Complexity: Bridging the Gap Between Design and Development"
    app:myEventTime="18:00 - 20:00"
    app:month="JUL"
    app:date="24"
    app:day="Rab"
    app:myEventBadgeVisible="true" />
```

### XML Attributes
| Attribute             | Format    | Description                                                                                                    |
| :-------------------- | :-------- | :------------------------------------------------------------------------------------------------------------- |
| `myEventLocation`     | `enum`    | Sets the location text (`offline`, `online`, `hybrid`)                                                         |
| `myEventType`         | `enum`    | Sets the event status and automatically configures the badge (`live`, `registered`, `attended`, `notattended`) |
| `myEventTitle`        | `string`  | Primary event title (supports 2 lines)                                                                         |
| `myEventTime`         | `string`  | Event time range display                                                                                       |
| `month`               | `string`  | 3-letter month abbreviation (e.g., "JUL")                                                                      |
| `date`                | `string`  | Date number (e.g., "24")                                                                                       |
| `day`                 | `string`  | 3-letter day abbreviation (e.g., "Rab")                                                                        |
| `myEventBadgeVisible` | `boolean` | Show/hide badge                                                                                                |


> **Note**: Badge properties (`badgeText`, `badgeType`, `badgeSize`, `myEventBadgeVisible`) are declared in the style able attributes but must be set programmatically using the `setBadgeData()` method.


## Kotlin Implementation

### Basic Configuration
```kotlin
val myEventCard = findViewById<MyEventCard>(R.id.eventCard)

// Set basic event information
myEventCard.apply {
    myEventType = MyEventCard.MyEventType.REGISTERED
    eventLocation = MyEventCard.MyEventLocation.HYBRID
    eventTitle = "Simplifying UX Complexity: Bridging the Gap Between Design and Development"
    eventTime = "18:00 - 20:00 WIB"
    setCalendarData("SEP", "12", "Wed")
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


### RecyclerView Adapter Implementation
```kotlin
class MyEventAdapter(private val onEventClick: (MyEvent) -> Unit) : 
    ListAdapter<MyEvent, MyEventAdapter.ViewHolder>(MyEventDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        
        holder.binding.eventCard.apply {
            myEventType = event.myEventType
            eventLocation = event.myEventLocation
            eventTitle = event.title
            eventTime = event.time
            setCalendarData(event.month, event.date, event.day)
            setBadgeData(event.badgeText, event.badgeType, event.badgeSize, event.isBadgeVisible)
            
            myEventCardDelegate = object : MyEventCardDelegate {
                override fun onClick(eventCard: MyEventCard) {
                    onEventClick(event)
                }
            }
        }
    }
    
    override fun onViewRecycled(holder: ViewHolder) {
        holder.binding.eventCard.myEventCardDelegate = null
        super.onViewRecycled(holder)
    }
}
```


### Data Model Integration
```kotlin
// Creating MyEvent data
val myEvent = MyEvent(
    status = MyEventStatus.TERDAFTAR,
    date = "15",
    day = "Sat",
    month = "SEP",
    time = "10:00 - 12:00 WIB",
    title = "IT Security Awareness: Stay Ahead of Threats, Stay Secure",
    myEventType = MyEventCard.MyEventType.REGISTERED,
    myEventLocation = MyEventCard.MyEventLocation.ONLINE,
    badgeText = "Terdaftar",
    badgeType = EventCardBadge.BadgeType.REGISTERED,
    isBadgeVisible = true,
    badgeSize = EventCardBadge.BadgeSize.SMALL
)
```


## Event Status Types
| Status        | Badge Text    | Description                      |
| :------------ | :------------ | :------------------------------- |
| `LIVE`        | `Berlangsung` | Event is currently ongoing       |
| `REGISTERED`  | `Terdaftar`   | User is registered for the event |
| `ATTENDED`    | `Hadir`       | User attended the event          |
| `NOTATTENDED` | `Tidak Hadir` | User did not attend the event    |


### Event Location Types
| Location  | Display Text    | Description                       |
| :-------- | :-------------- | :-------------------------------- |
| `OFFLINE` | `Offline Event` | In-person physical event          |
| `ONLINE`  | `Online Event`  | Virtual/remote event              |
| `HYBRID`  | `Hybrid Event`  | Combination of offline and online 


## Do's and Don'ts
### ✅ Do's
- Use for displaying user's registered/attended events in "My Events" sections
- Provide concise but descriptive event titles
- Use appropriate badge types for event status
- Ensure calendar data uses consistent 3-letter abbreviations
- Handle card clicks for event detail navigation
- Test with various title lengths for proper truncation
- Use the correct event location type (offline, online, hybrid)

### ❌ Don'ts
- Use for event discovery or browsing (use simpler cards)
- Override the compact layout with custom styling
- Use long text in badges that breaks the layout
- Ignore the badge visibility for better information hierarchy
- Forget to handle RecyclerView recycling properly
- Use inconsistent date formatting
- Mix up event status types with location types


## Material Design Styling

The component implements Material Design 3 principles:

### Card Structure
- **Corner Radius**: 8dp for modern, friendly appearance
- **Elevation**: 1dp with neutral shadow colors (API 28+)
- **Stroke**: 1dp subtle border using `colorStrokeSubtle`
- **Background**: Elevated surface color from theme
- **Padding**: 12dp all sides for content breathing room

### Typography Hierarchy
- **Event Location**: `l3Regular` style with secondary foreground color
- **Event Title**: `p1SemiBold` style with primary foreground color, 2-line max with ellipsis
- **Event Time**: `l3Regular` style with secondary foreground color

### Calendar Card Integration
- **Fixed Width**: 56dp for visual consistency
- **Vertical Layout**: Month/Date/Day stacked
- **Constraint Layout**: Anchored to start, vertically centered

### Badge System
- **Dynamic Positioning**: End-aligned, vertically centered with location text
- **Auto-Configuration**: Badge type, text, and size set based on event status
- **Size Consistency**: Always uses SMALL size for compact display

### Color System
- **Primary Text**: `colorForegroundPrimary` (title)
- **Secondary Text**: `colorForegroundSecondary` (location, time)
- **Background**: `colorBackgroundElevated` (card surface)
- **Stroke**: `colorStrokeSubtle` (card border)
- **Shadows**: `colorShadowNeutralAmbient` (ambient), `colorShadowNeutralKey` (spot)

### Shadow Behavior (API 28+)
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    outlineAmbientShadowColor = colorShadowNeutralAmbient  // A5 opacity
    outlineSpotShadowColor = colorShadowNeutralKey         // A10 opacity
}
```


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
