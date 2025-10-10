# Event Card

| Feature / Variation           | Preview                                                                                                                           |
|-------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| Full Card with Badge & Status | ![Image + Badge + Banner + Description + Status](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287572/ec_full_card_with_badge_status_rvbfiu.gif)                                                                                |
| Without Badge & Status              | ![Image + Banner + Description](https://res.cloudinary.com/dacnnk5j4/image/upload/w_500,c_scale,q_auto,f_auto/v1759287574/ec_without_badge_status_wg9isd.gif) |

| **CardState** | **Visual Effect** |
| ------------- | ----------------- |
| **REST** | Default elevated background |
| **ON_PRESS** | Pressed overlay effect |

## Overview

*A comprehensive event card component that displays event information including an image, badge indicator, event type banner, title, date, and registration status. Features press state animations and click handling with debounce functionality.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.event.card.EventCard
    android:id="@+id/eventCard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:eventImageSrc="@drawable/poster_event"
    app:showCardBadge="true"
    app:cardBadgeType="live"
    app:cardBadgeText="Berlangsung"
    app:cardEventType="Hybrid Event"
    app:cardEventCategory="People Development"
    app:cardEventTitle="EDTS Town-Hall 2025: Power of Change"
    app:cardEventDate="23 Juli 2025"
    app:showCardStatus="true"
    app:cardStatusType="registered"
    app:cardStatusText="Terdaftar" />
```

### 2. Initialize in Code

```kotlin
val eventCard = binding.eventCard

// Set up delegate for click handling
eventCard.eventCardDelegate = object : EventCardDelegate {
    override fun onEventCardClick(card: EventCard) {
        // Handle event card click
        Log.d("MainActivity", "Event clicked: ${card.eventTitle}")
        navigateToEventDetail(card)
    }
}
```

## Display Modes

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `REST` | `0` | Default state with elevated background | Normal display state |
| `ON_PRESS` | `1` | Pressed state with overlay effect | User interaction feedback |

```kotlin
// Card state is managed internally during touch events
// No manual state setting required
```

## Properties Reference

### Image Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `eventImageSrc` | `Int?` | `null` | Drawable resource ID for the event image |

### Badge Properties

| Property Name | Type | Default | Description                         |
| ------------- | ---- | ------- |-------------------------------------|
| `showBadge` | `Boolean` | `false` | Controls badge visibility           |
| `badgeType` | `EventCardBadge.BadgeType` | `LIVE` | Type of badge (LIVE, INVITED, etc.) |
| `badgeText` | `String?` | `null` | Text displayed in the badge         |

### Banner Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `eventType` | `String?` | `null` | Event type (e.g., "Hybrid Event", "Online Event") |
| `eventCategory` | `String?` | `null` | Event category (e.g., "People Development") |

### Description Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `eventTitle` | `String?` | `null` | Main event title |
| `eventDate` | `String?` | `null` | Event date string |

### Status Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `showStatus` | `Boolean` | `true` | Controls status visibility |
| `statusType` | `EventCardStatus.StatusType` | `UNREGISTERED` | Registration status type |
| `statusText` | `String?` | `null` | Text displayed in the status indicator |

### Behavior Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `eventCardDelegate` | `EventCardDelegate?` | `null` | Click event handler interface |
| `cardState` | `CardState` | `REST` | Current visual state (managed internally) |

## Data Models

### EventCardDelegate Interface

| Method | Parameters | Required | Description |
| ------ | ---------- | -------- | ----------- |
| `onEventCardClick()` | `card: EventCard` | ✅ | Called when the event card is clicked |

```kotlin
val delegate = object : EventCardDelegate {
    override fun onEventCardClick(card: EventCard) {
        // Handle click event
        navigateToEventDetail(card.eventTitle)
    }
}
```

### EventCardBadge.BadgeType Enum

Common badge types used to indicate event status:
- `LIVE` - Event is currently happening
- `INVITED` - User is invited to the event
- `REGISTERED` - User is registered for the event
- `ATTENDED` - User has record their attendance the event
- `NOTATTENDED` - User did not record their attendance the event

### EventCardStatus.StatusType Enum

Registration status indicators:
- `RSVP` - User need to filled RSVP Form
- `REGISTERED` - User is registered for the event
- `UNREGISTERED` - User has not registered

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `resetClickCount()` | None | Resets internal click counter to zero |
| `getClickCount()` | None | Returns current click count for debugging |
| `performClick()` | None | Programmatically triggers a click event |

## Usage Examples

```kotlin
// Full featured event card
binding.eventCard.apply {
    eventImageSrc = R.drawable.poster_event
    showBadge = true
    badgeType = EventCardBadge.BadgeType.LIVE
    badgeText = "Berlangsung"
    eventType = "Hybrid Event"
    eventCategory = "People Development"
    eventTitle = "EDTS Town-Hall 2025: Power of Change"
    eventDate = "23 Juli 2025"
    showStatus = true
    statusType = EventCardStatus.StatusType.REGISTERED
    statusText = "Terdaftar"
    
    eventCardDelegate = object : EventCardDelegate {
        override fun onEventCardClick(card: EventCard) {
            navigateToEventDetail(card)
        }
    }
}

// Event card without badge
binding.eventCard2.apply {
    eventImageSrc = R.drawable.poster_event_2
    showBadge = false
    eventType = "Online Event"
    eventCategory = "Technical Workshop"
    eventTitle = "Kotlin Best Practices 2025"
    eventDate = "15 Agustus 2025"
    statusType = EventCardStatus.StatusType.UNREGISTERED
    statusText = "Belum Terdaftar"
}

// Event card without status
binding.eventCard3.apply {
    eventImageSrc = R.drawable.poster_event_3
    badgeType = EventCardBadge.BadgeType.INVITED
    badgeText = "Diundang"
    eventType = "Conference"
    eventCategory = "Innovation"
    eventTitle = "Digital Transformation Summit"
    eventDate = "30 September 2025"
    showStatus = false
}

// Dynamic updates based on event state
fun updateEventStatus(eventId: String, isLive: Boolean) {
    binding.eventCard.apply {
        if (isLive) {
            badgeType = EventCardBadge.BadgeType.LIVE
            badgeText = "Berlangsung"
            showBadge = true
        } else {
            showBadge = false
        }
    }
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- Full featured event card -->
<com.edts.components.event.card.EventCard
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:eventImageSrc="@drawable/poster2"
    app:showCardBadge="true"
    app:cardBadgeType="live"
    app:cardBadgeText="Berlangsung"
    app:cardEventType="Hybrid Event"
    app:cardEventCategory="People Development"
    app:cardEventTitle="EDTS Town-Hall 2025: Power of Change"
    app:cardEventDate="23 Juli 2025"
    app:showCardStatus="true"
    app:cardStatusType="registered"
    app:cardStatusText="Terdaftar" />

<!-- Event card without badge -->
<com.edts.components.event.card.EventCard
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:eventImageSrc="@drawable/poster3"
    app:showCardBadge="false"
    app:cardEventType="Online Event"
    app:cardEventCategory="Training"
    app:cardEventTitle="Leadership Workshop 2025"
    app:cardEventDate="10 Agustus 2025"
    app:cardStatusType="unregistered"
    app:cardStatusText="Belum Terdaftar" />

<!-- Event card without status -->
<com.edts.components.event.card.EventCard
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:eventImageSrc="@drawable/poster4"
    app:cardBadgeType="invited"
    app:cardBadgeText="Diundang"
    app:cardEventType="Seminar"
    app:cardEventCategory="Technology"
    app:cardEventTitle="AI Innovation Forum"
    app:cardEventDate="5 Oktober 2025"
    app:showCardStatus="false" />
```

### Programmatic Customization

```kotlin
// Runtime property updates
binding.eventCard.apply {
    eventImageSrc = R.drawable.new_poster
    eventTitle = "Updated Event Title"
    eventDate = "New Date"
    badgeText = "New Badge"
    statusText = "New Status"
}

// Conditional visibility based on event state
fun configureCard(event: Event) {
    binding.eventCard.apply {
        showBadge = event.isLive || event.isInvited
        showStatus = event.userRegistered != null
        
        when {
            event.isLive -> {
                badgeType = EventCardBadge.BadgeType.LIVE
                badgeText = "Live Now"
            }
            event.isUpcoming -> {
                badgeType = EventCardBadge.BadgeType.INVITED
                badgeText = "Diundang"
            }
        }
        
        statusType = when {
            event.userRegistered == true -> EventCardStatus.StatusType.REGISTERED
            event.userOnWaitlist == true -> EventCardStatus.StatusType.WAITLIST
            else -> EventCardStatus.StatusType.UNREGISTERED
        }
    }
}
```

## Performance Considerations

- **Click Debouncing** — Built-in 300ms debounce prevents accidental double-clicks and improves user experience
- **ViewBinding** — Uses ViewBinding for efficient view access and type safety
- **Nested Component Architecture** — Delegates rendering to specialized sub-components (EventCardBadge, EventCardBanner, EventCardDescription, EventCardStatus)

## Animation Details

| Animation Type | Duration | Description |
| -------------- | -------- | ----------- |
| `Press State` | Instant | Background color change on touch down/up |
| `Foreground Overlay` | Instant | Overlay drawable for pressed state feedback |
| `Badge Visibility` | N/A | Immediate show/hide transition |
| `Status Visibility` | N/A | Immediate show/hide transition |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Use high-quality event images with consistent aspect ratios | Use low-resolution or distorted images |
| Keep event titles concise and descriptive (max 2 lines) | Use overly long titles that may wrap excessively |
| Update badge status in real-time for live events | Leave stale "Live" badges after event ends |
| Implement EventCardDelegate for all event cards | Ignore click handling |
| Use appropriate badge types for event states | Mix badge types inconsistently |
| Test card layouts with various content lengths | Assume all content will fit perfectly |

## Component Architecture

The EventCard is composed of several nested components:
- **EventCardBadge** 
- **EventCardBanner**
- **EventCardDescription**
- **EventCardStatus**

Each sub-component can be customized through the parent EventCard properties, which automatically propagates changes to the appropriate child component.

---

> **⚠️ Note**: This component uses MaterialCardView as its base and relies on nested custom views for its sub-components. Ensure all dependent components (EventCardBadge, EventCardBanner, EventCardDescription, EventCardStatus) are properly implemented in your project. There's also click debounce mechanism to prevents rapid successive clicks.