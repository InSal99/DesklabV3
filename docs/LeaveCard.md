# Leave Card

## Overview

Leave Card is a custom component that extends `MaterialCardView` to display employee leave information in a card format. It combines employee details (name, role, profile image) with a leave counter display, providing an interactive, Material Design-compliant card with ripple effects and customizable styling. The component uses composition to integrate `EmployeeInfo` and `LeaveCounter` sub-components.

|Variation | Preview |
| ------------------- | ------- |
| **Normal Counter** | Default counter appearance |
| **Critical Counter** | Red/warning counter for low balance |

---

## Basic Usage

### 1. Add to Layout

```xml
<your.edts.components.leave.card.LeaveCard
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

// Set employee information
leaveCard.employeeName = "John Doe"
leaveCard.employeeRole = "Software Engineer"
leaveCard.employeeImage = R.drawable.avatar_john

// Set counter information
leaveCard.counterText = "15 days remaining"
leaveCard.counterType = LeaveCounter.CounterType.NORMAL

// Set click listener
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
| `employeeName` | `CharSequence?` | `null` | Employee's display name |
| `employeeRole` | `CharSequence?` | `null` | Employee's job title/role |
| `employeeImage` | `Int?` | `placeholder` | Employee profile image resource ID |

### Counter Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `counterText` | `CharSequence?` | `null` | Counter text (e.g., "15 days remaining") |
| `counterType` | `LeaveCounter.CounterType` | `NORMAL` | Counter styling type |

### Interaction Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `leaveCardDelegate` | `LeaveCardDelegate?` | `null` | Click event delegate |

### Card Appearance Properties

| Property Name | Type | Default | Description |
| ------------- | ---- | ------- | ----------- |
| `strokeColor` | `@ColorInt` | `colorStrokeSubtle` | Border stroke color |
| `strokeWidth` | `Int` | `1dp` | Border stroke width |
| `radius` | `Float` | `12dp` | Corner radius |
| `cardBackgroundColor` | `@ColorInt` | `colorBackgroundPrimary` | Card background color |
| `cardElevation` | `Float` | `2dp` | Card shadow elevation |
| `rippleColor` | `ColorStateList` | `colorBackgroundModifierOnPress` | Ripple effect color |

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

- **View Binding**: Uses ViewBinding for efficient view access
- **Composition Pattern**: Delegates to `EmployeeInfo` and `LeaveCounter` sub-components
- **Attribute Caching**: Resolves theme attributes once during initialization
- **Material Ripple**: Hardware-accelerated ripple effect for smooth interactions
- **RecyclerView Friendly**: Designed for efficient use in lists with view recycling
- **Single Click Handling**: Overrides `performClick()` for proper accessibility support

## Implementation Details

### Layout Structure

The LeaveCard uses `LeaveCardBinding` which contains:
- `lEmployeeInfo`: EmployeeInfo component (displays name, role, image)
- `lDescription`: LeaveCounter component (displays counter with type-based styling)

### Sub-Components

#### EmployeeInfo Component
Properties accessible through LeaveCard:
- `employeeName`: String
- `employeeRole`: String
- `employeeImage`: Drawable resource ID

#### LeaveCounter Component
Properties accessible through LeaveCard:
- `counterText`: String
- `counterType`: CounterType enum (NORMAL, CRITICAL)

### Material Design Elements

- **Elevation**: 2dp shadow for depth
- **Corner Radius**: 12dp for modern rounded appearance
- **Stroke**: 1dp border for definition
- **Ripple Effect**: Material ripple on press with theme-based color
- **Shadow Colors**: API 28+ support for custom shadow colors

## Best Practices

| ✅ Do | ❌ Don't |
| ----- | -------- |
| Use LeaveCardDelegate for click handling | Override `setOnClickListener()` directly |
| Keep employee names concise | Display very long text without ellipsizing |
| Set appropriate CounterType based on balance | Always use NORMAL regardless of data |
| Test ripple effects on different themes | Assume ripple color works everywhere |
| Provide accessible content descriptions | Ignore accessibility for screen readers |
| Use in RecyclerView with proper ViewHolder | Create new instances for each scroll |
| Handle null values gracefully | Assume properties are always set |
| Test on different screen sizes | Design for single device only |

## Required Resources

### Layout
- `leave_card.xml` (LeaveCardBinding): Contains:
  - `lEmployeeInfo`: EmployeeInfo custom view
  - `lDescription`: LeaveCounter custom view

### Sub-Components
- **EmployeeInfo**: Custom view with `employeeName`, `employeeRole`, `employeeImage` properties
- **LeaveCounter**: Custom view with `counterText`, `counterType` properties

### Drawables
- `placeholder`: Default employee image placeholder

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

> **⚠️ Note**: This component requires `LeaveCardBinding` to be properly generated from `leave_card.xml`. The layout must contain `lEmployeeInfo` (EmployeeInfo component) and `lDescription` (LeaveCounter component) with matching IDs. Ensure both sub-components are properly implemented with their respective properties.