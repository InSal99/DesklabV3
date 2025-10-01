# Event Card: Status

| Feature / Variation | Preview                                                                                                                   |
| ------------------- |---------------------------------------------------------------------------------------------------------------------------|
| RSVP Status | ![Warning color text](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228577/ecs_rsvp_status_nlxiwx.png)                                                                                                   |
| Registered Status | ![Success color text](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228577/ecs_registered_status_k7lwec.png)     |
| Unregistered Status | ![Attention color text](https://res.cloudinary.com/dacnnk5j4/image/upload/w_300,c_scale,q_auto,f_auto/v1759228578/ecs_unregistered_status_lvogdv.png) |

| **StatusType** | **Color Theme** |
| -------------- | --------------- |
| **RSVP** | Warning (Intense) |
| **REGISTERED** | Success (Intense) |
| **UNREGISTERED** | Attention (Intense) |

## Overview

*A lightweight status indicator component for event cards that displays text with contextual color coding based on registration status. Supports three distinct status types with theme-aware color attributes.*

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.event.card.EventCardStatus
    android:id="@+id/eventCardStatus"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:statusType="rsvp"
    app:statusText="Reservasi sekarang!" />
```

### 2. Initialize in Code

```kotlin
val eventCardStatus = binding.eventCardStatus

// Configure status
eventCardStatus.statusType = EventCardStatus.StatusType.REGISTERED
eventCardStatus.statusText = "Catat Kehadiran"
```

## Display Modes

| Mode Name | Value | Description | Use Case                                                      |
| --------- | ----- | ----------- |---------------------------------------------------------------|
| `RSVP` | `0` | Warning state for pending reservations | User needs to reserve their spot                              |
| `REGISTERED` | `1` | Success state for confirmed registration | User is registered and can check-in                           |
| `UNREGISTERED` | `2` | Attention state for closed/unavailable | Registration is closed or unavailable or Invitation is denied |

```kotlin
// Set status type programmatically
eventCardStatus.statusType = EventCardStatus.StatusType.RSVP
```

## Properties Reference

### Status Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `statusType` | `StatusType` | `RSVP` | Determines the status color scheme |
| `statusText` | `String?` | `null` | Text displayed in the status indicator |

### StatusType Enum

| Enum Value | Int Value | Color Attribute | Description |
| ---------- | --------- | --------------- | ----------- |
| `RSVP` | `0` | `colorForegroundWarningIntense` | Pending reservation status |
| `REGISTERED` | `1` | `colorForegroundSuccessIntense` | Successfully registered status |
| `UNREGISTERED` | `2` | `colorForegroundAttentionIntense` | Closed or unavailable status |

## Data Models 

### StatusType Enum

| Property | Type | Required | Description |
| -------- | ---- | -------- | ----------- |
| `value` | `Int` | ✅ | Integer representation of status type |

```kotlin
// Create status type from integer value
val status = EventCardStatus.StatusType.fromValue(1)
// Returns StatusType.REGISTERED

// Access enum value
val statusValue = EventCardStatus.StatusType.RSVP.value
// Returns 0
```

## Methods Reference

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `StatusType.fromValue()` | `value: Int` | Static method to convert integer to StatusType enum |

## Usage Examples

```kotlin
// RSVP Status - Warning theme
binding.statusRsvp.apply {
    statusType = EventCardStatus.StatusType.RSVP
    statusText = "Reservasi sekarang!"
}

// Registered Status - Success theme
binding.statusRegistered.apply {
    statusType = EventCardStatus.StatusType.REGISTERED
    statusText = "Catat Kehadiran"
}

// Unregistered Status - Attention theme
binding.statusUnregistered.apply {
    statusType = EventCardStatus.StatusType.UNREGISTERED
    statusText = "Pendaftaran ditutup"
}

// Dynamic status updates based on event state
fun updateEventStatus(isRegistered: Boolean, isOpen: Boolean) {
    binding.eventCardStatus.apply {
        when {
            isRegistered -> {
                statusType = EventCardStatus.StatusType.REGISTERED
                statusText = "Catat Kehadiran"
            }
            isOpen -> {
                statusType = EventCardStatus.StatusType.RSVP
                statusText = "Reservasi sekarang!"
            }
            else -> {
                statusType = EventCardStatus.StatusType.UNREGISTERED
                statusText = "Pendaftaran ditutup"
            }
        }
    }
}
```

## Customization Examples

### XML Attribute Customization

```xml
<!-- RSVP Status -->
<com.edts.components.event.card.EventCardStatus
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:statusType="rsvp"
    app:statusText="Reservasi sekarang!" />

<!-- Registered Status -->
<com.edts.components.event.card.EventCardStatus
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:statusType="registered"
    app:statusText="Catat Kehadiran" />

<!-- Unregistered Status -->
<com.edts.components.event.card.EventCardStatus
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:statusType="unregistered"
    app:statusText="Pendaftaran ditutup" />
```

### Programmatic Customization

```kotlin
// Change status based on API response
eventCardStatus.apply {
    when (eventResponse.status) {
        "pending" -> {
            statusType = EventCardStatus.StatusType.RSVP
            statusText = "Menunggu konfirmasi"
        }
        "confirmed" -> {
            statusType = EventCardStatus.StatusType.REGISTERED
            statusText = "Terdaftar"
        }
        "closed" -> {
            statusType = EventCardStatus.StatusType.UNREGISTERED
            statusText = "Event sudah penuh"
        }
    }
}

// Using enum value conversion
val statusFromServer = 1 // From API
eventCardStatus.statusType = EventCardStatus.StatusType.fromValue(statusFromServer)
```

## Performance Considerations

- **Theme Attribute Resolution** — Uses TypedValue for efficient theme color resolution
- **ViewBinding** — Uses ViewBinding for type-safe view access
- **Enum Pattern** — Leverages Kotlin enums for type-safe status management

## Animation Details

| Animation Type | Duration | Description |
| -------------- | -------- | ----------- |
| `Color Update` | Instant | Text color changes immediately on status type update |
| `Text Update` | Instant | Text content updates immediately when status text is set |

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | ------- |
| Match statusType with appropriate text content | Mix status types with mismatched text |
| Keep status text concise (1-3 words) | Use long sentences in status indicators |
