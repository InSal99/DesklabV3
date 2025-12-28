# Leave Card

## Overview

Leave Card is a custom component that extends `MaterialCardView` to display employee leave information in a card format. It combines employee details (name, role, profile image) with a leave counter display, providing an interactive, Material Design-compliant card with ripple effects and customizable styling. The component uses composition to integrate `EmployeeInfo` and `LeaveCounter` sub-components.

| Variation            | Preview                                                                                                                                |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| **Normal Counter**   | ![leavecard](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759231266/leavecardgif_wcbpeg.gif) |
| **Critical Counter** | ![leavecard](https://res.cloudinary.com/dpdbzlnhr/image/upload/c_scale,w_400/v1759316036/leavecardcriticalgif_tn9l3g.gif) |

---

## Basic Usage

### 1. Add to Layout

```xml
<com.edts.components.leave.card.LeaveCard
        android:id="@+id/leaveCard"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:employeeName="John Doe"
        app:employeeRole="Software Engineer"
        app:employeeImage="@drawable/avatar_john"
        app:counterText="15 days remaining"
        app:counterType="NORMAL" />
```

### 2. Initialize in Code

```kotlin
val leaveCard = findViewById<LeaveCard>(R.id.leaveCard)

// Set employee information (properties)
leaveCard.employeeName = "John Doe"
leaveCard.employeeRole = "Software Engineer"
leaveCard.employeeImage = R.drawable.avatar_john
leaveCard.employeeImageUrl = "https://example.com/avatar.jpg" // optional

// Set counter information
leaveCard.counterText = "15 days remaining"
leaveCard.counterType = LeaveCounter.CounterType.NORMAL

// Set click delegate
leaveCard.leaveCardDelegate = object : LeaveCardDelegate {
  override fun onClick(card: LeaveCard) {
    // Handle card click
    showLeaveDetails(card.employeeName)
  }
}
```

## Display Modes (CounterType)

| Mode Name | Value | Description | Use Case |
| --------- | ----- | ----------- | -------- |
| `NORMAL` | `0` | Default counter styling | Standard leave balance display |
| `CRITICAL` | `1` | Warning/red counter styling | Low leave balance alert |

```kotlin
leaveCard.counterType = LeaveCounter.CounterType.NORMAL
// or
leaveCard.counterType = LeaveCounter.CounterType.CRITICAL
```

## Properties Reference

### Employee Information Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `employeeName` | `CharSequence?` | `null` | Employee's display name (backed by `lEmployeeInfo.employeeName`) |
| `employeeRole` | `CharSequence?` | `null` | Employee's job title/role (backed by `lEmployeeInfo.employeeRole`) |
| `employeeImage` | `Int?` | `R.drawable.kit_ic_placeholder` | Employee profile image resource ID (when set, assigned to `lEmployeeInfo.employeeImage`) |
| `employeeImageUrl` | `String?` | `null` | Optional remote image URL (when set, assigned to `lEmployeeInfo.employeeImageUrl`) |

Notes:
- updateImageSrc() applies `employeeImageUrl` first (if present) then `employeeImage` resource.

### Counter Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `counterText` | `CharSequence?` | `null` | Counter text (e.g., "15 days remaining") |
| `counterType` | `LeaveCounter.CounterType` | `NORMAL` | Counter styling type |

### Interaction Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `leaveCardDelegate` | `LeaveCardDelegate?` | `null` | Click event delegate; notified from `performClick()` |

### Card Appearance & Sizing

| Property Name | Source | Description |
| ------------- | ------ | ----------- |
| `strokeColor` | resolved via `R.attr.colorStrokeSubtle` (fallback `R.color.kitColorNeutralGrayLight30`) | Border stroke color |
| `strokeWidth` | `R.dimen.stroke_weight_1dp` | Border stroke width (resource) |
| `radius` | `R.dimen.radius_12dp` | Corner radius (resource) |
| `cardBackgroundColor` | resolved via `R.attr.colorBackgroundElevated` (fallback `R.color.kitColorNeutralWhite`) | Card background color |
| `cardElevation` | `2dp` (set using `2f.dpToPx`) | Card shadow elevation |
| `rippleColor` | resolved via `R.attr.colorBackgroundModifierOnPress` (fallback `R.color.kitColorNeutralGrayDarkA5`) | Ripple effect color |

## LeaveCardDelegate Interface

Implement this interface to receive click events:

```kotlin
interface LeaveCardDelegate {
    fun onClick(card: LeaveCard)
}
```

## Methods Reference

### Property Accessors

| Method Name | Parameters | Return Type | Description |
| ----------- | ---------- | ----------- | ----------- |
| `getEmployeeName()` | - | `CharSequence?` | Returns employee name |
| `setEmployeeName()` | `value: CharSequence?` | `Unit` | Sets employee name |
| `getEmployeeRole()` | - | `CharSequence?` | Returns employee role |
| `setEmployeeRole()` | `value: CharSequence?` | `Unit` | Sets employee role |
| `getEmployeeImage()` | - | `Int?` | Returns employee image resource ID |
| `setEmployeeImage()` | `value: Int?` | `Unit` | Sets employee image |
| `getCounterText()` | - | `CharSequence?` | Returns counter text |
| `setCounterText()` | `value: CharSequence?` | `Unit` | Sets counter text |
| `getCounterType()` | - | `CounterType` | Returns counter type |
| `setCounterType()` | `value: CounterType` | `Unit` | Sets counter type |

### Inherited Methods

| Method Name | Parameters | Description |
| ----------- | ---------- | ----------- |
| `performClick()` | - | Triggers click event and notifies delegate |

## Usage Examples

### Example 1: Basic Leave Card

```kotlin
val leaveCard = findViewById<LeaveCard>(R.id.leaveCard)

leaveCard.apply {
    employeeName = "Jane Smith"
    employeeRole = "Product Manager"
    employeeImage = R.drawable.avatar_jane
    counterText = "20 days available"
    counterType = LeaveCounter.CounterType.NORMAL
}
```

### Example 2: Critical Leave Balance

```kotlin
val leaveCard = findViewById<LeaveCard>(R.id.leaveCard)

leaveCard.apply {
    employeeName = "Mike Johnson"
    employeeRole = "Developer"
    employeeImage = R.drawable.avatar_mike
    counterText = "2 days remaining"
    counterType = LeaveCounter.CounterType.CRITICAL
}

leaveCard.leaveCardDelegate = object : LeaveCardDelegate {
    override fun onClick(card: LeaveCard) {
        Toast.makeText(this@MainActivity, "Low leave balance!", Toast.LENGTH_SHORT).show()
    }
}
```

### Example 3: Dynamic Data Binding

```kotlin
// With ViewModel
viewModel.employeeLeave.observe(this) { leaveData ->
    leaveCard.apply {
        employeeName = leaveData.employeeName
        employeeRole = leaveData.role
        employeeImage = leaveData.imageResId
        counterText = "${leaveData.remainingDays} days left"
        counterType = when {
            leaveData.remainingDays <= 3 -> LeaveCounter.CounterType.CRITICAL
            else -> LeaveCounter.CounterType.NORMAL
        }
    }
}
```

## Performance Considerations

- **View Binding**: Uses ViewBinding (`LeaveCardBinding`) for efficient view access.
- **Composition Pattern**: Delegates to `EmployeeInfo` and `LeaveCounter` sub-components.
- **Attribute Resolution**: Theme attributes are resolved once during initialization.
- **Material Ripple**: Hardware-accelerated ripple effect for smooth interactions.
- **RecyclerView Friendly**: Designed for efficient use in lists with view recycling.
- **Cache & Cleanup**: No internal caches in this component; it relies on subcomponents for their own cleanup.

## Implementation Details

### Layout Structure

The LeaveCard uses `LeaveCardBinding`, which contains:
- `lEmployeeInfo`: EmployeeInfo component (displays name, role, image)
- `lDescription`: LeaveCounter component (displays counter with type-based styling)

### Sub-Components

#### EmployeeInfo Component
Properties accessible through LeaveCard:
- `employeeName`: CharSequence? (delegated to `lEmployeeInfo.employeeName`)
- `employeeRole`: CharSequence? (delegated)
- `employeeImage`: Int? (delegated)
- `employeeImageUrl`: String? (delegated)

#### LeaveCounter Component
Properties accessible through LeaveCard:
- `counterText`: CharSequence?
- `counterType`: CounterType enum (NORMAL, CRITICAL)

### Click & Ripple Behaviour

- setupClickAnimation():
  - Enables `isClickable = true` and `isFocusable = true` so the card responds to clicks.
  - Resolves the active ripple color from `R.attr.colorBackgroundModifierOnPress` (fallback `R.color.kitColorNeutralGrayDarkA5`) and sets `rippleColor`.
- `performClick()` calls `super.performClick()` then notifies `leaveCardDelegate?.onClick(this)`.

### Shadow Colors (API 28+)
- On API >= P the card sets:
  - `outlineAmbientShadowColor` from `R.attr.colorShadowTintedAmbient` (fallback `R.color.kitColorBrandPrimaryA10`)
  - `outlineSpotShadowColor` from `R.attr.colorShadowTintedKey` (fallback `R.color.kitColorBrandPrimaryA20`)

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use LeaveCardDelegate for click handling | Override `setOnClickListener()` directly without considering accessibility |
| Keep employee names concise | Display very long text without ellipsizing |
| Set CounterType based on balance | Always use NORMAL regardless of data |
| Test ripple effects on different themes | Assume ripple color works everywhere |
| Provide accessible content descriptions | Ignore accessibility for screen readers |
| Use in RecyclerView with proper ViewHolder | Create new instances for each scroll |
| Handle null values gracefully | Assume properties are always set |
| Test on different screen sizes | Design for a single device only |

## Required Resources

### Layout
- `leave_card.xml` (LeaveCardBinding): Contains:
  - `lEmployeeInfo`: EmployeeInfo custom view
  - `lDescription`: LeaveCounter custom view

### Sub-Components
- **EmployeeInfo**: Custom view with `employeeName`, `employeeRole`, `employeeImage`, `employeeImageUrl` properties
- **LeaveCounter**: Custom view with `counterText`, `counterType` properties

### Drawables
- `R.drawable.kit_ic_placeholder` (default employee image placeholder)

### Dimensions
- `stroke_weight_1dp`: Border stroke width
- `radius_12dp`: Corner radius

### Theme Attributes
- `colorBackgroundPrimary`: Card background color
- `colorStrokeSubtle`: Border stroke color
- `colorBackgroundModifierOnPress`: Ripple effect color
- `colorShadowNeutralAmbient`: Ambient shadow color (API 28+)
- `colorShadowNeutralKey`: Spot shadow color (API 28+)

---

> **⚠️ Note**: This component requires `LeaveCardBinding` to be generated from `leave_card.xml`. The layout must contain `lEmployeeInfo` (EmployeeInfo component) and `lDescription` (LeaveCounter component) with matching IDs. Ensure both sub-components are implemented with the expected properties (including support for `employeeImageUrl` if you plan to use remote images).