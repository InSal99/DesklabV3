# Leave Quota Card

`LeaveQuotaCard` is a custom Android view component that extends `MaterialCardView` to display employee leave quota information in a clean, organized card format within the EDTS Desklab App. It provides a comprehensive overview of leave balances, usage, and expiration dates with professional English text formatting, making it ideal for HR management and employee self-service features in corporate environments.

![Leave Quota Card](https://res.cloudinary.com/fauzanspratama/image/upload/v1760418138/Leave_Quota_Card_musmcd.png)

## Visual Anatomy

The component presents leave information in a structured layout with clear typography hierarchy:

| Element | Description |
| :------ | :---------- |
| **Title** | Leave type title using "Annual Leave Balance" |
| **Leave Quota** | Total available leave days in prominent display with "days" suffix |
| **Leave Used** | Number of days already utilized with "Leave Used: X Days" format |
| **Expiry Date** | Expiration date with "Expires: [date]" format |

## Key Features
- **Professional Corporate Design**: Clean, business-appropriate styling for enterprise applications
- **Clear Information Hierarchy**: Prominent display of key leave metrics for quick scanning
- **Material Design Compliance**: Card-based layout with proper elevation and shadows following Google's guidelines
- **Theme Integration**: Seamlessly integrates with EDTS Desklab App's design system
- **Simple Configuration**: Easy setup via XML attributes or programmatic binding
- **Type Safety**: Strongly typed properties with proper validation for robust implementation

## XML Implementation

### Main Card Usage
```xml
<com.edts.components.leave.card.LeaveQuotaCard
    android:id="@+id/leaveQuotaCard"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:leaveQuotaTitle="Annual Leave Balance"
    app:leaveQuota="15"
    app:leaveUsed="7"
    app:expiredDate="December 31, 2024" />
```

### XML Attributes
| Attribute | Format | Description | Example |
| :-------- | :----- | :---------- | :------ |
| `leaveQuotaTitle` | `string` | Type of leave (default: "Annual Leave Balance") | "Annual Leave Balance" |
| `leaveQuota` | `integer` | Total available leave days | 15 |
| `leaveUsed` | `integer` | Number of leave days already used | 7 |
| `expiredDate` | `string` | Expiration date of the leave quota | "December 31, 2024" |

## String Resource Formatting

The component automatically applies professional English formatting using these string resources:

```xml
<string name="leave_description_title">Annual Leave Balance</string>
<string name="leave_balance_format">%d days</string>
<string name="leave_expiry_format">Expires: %s</string>
<string name="leave_used_format">Leave Used: %d Days</string>
```

**Output Examples:**
- Leave Balance: `15 days`
- Expiry Date: `Expires: December 31, 2024`
- Leave Used: `Leave Used: 7 Days`

## Kotlin Implementation

### Basic Configuration
```kotlin
val leaveQuotaCard = findViewById<LeaveQuotaCard>(R.id.leaveQuotaCard)

// Set leave quota data programmatically
leaveQuotaCard.apply {
    title = "Annual Leave Balance"
    leaveQuota = 15
    leaveUsed = 7
    expiredDate = "December 31, 2024"
}
```

### Dynamic Data Binding
```kotlin
fun updateLeaveQuota(leaveData: LeaveData) {
    binding.leaveQuotaCard.apply {
        title = leaveData.type.displayName
        leaveQuota = leaveData.totalQuota
        leaveUsed = leaveData.used
        expiredDate = leaveData.expiryDate.formatToDisplayDate()
    }
}
```

### RecyclerView Adapter Implementation
```kotlin
class LeaveQuotaAdapter(private val leaveList: List<LeaveQuota>) : 
    RecyclerView.Adapter<LeaveQuotaAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leaveQuota = leaveList[position]
        
        holder.binding.leaveQuotaCard.apply {
            title = leaveQuota.title
            leaveQuota = leaveQuota.quota
            leaveUsed = leaveQuota.used
            expiredDate = leaveQuota.expiredDate
        }
    }

    class ViewHolder(val binding: ItemLeaveQuotaBinding) : 
        RecyclerView.ViewHolder(binding.root)
}
```

### Data Class Example
```kotlin
data class LeaveQuota(
    val title: String,
    val quota: Int,
    val used: Int,
    val expiredDate: String
)

// Usage with professional English formatting
val annualLeave = LeaveQuota(
    title = "Annual Leave Balance",
    quota = 15,
    used = 7,
    expiredDate = "December 31, 2024"
)

val sickLeave = LeaveQuota(
    title = "Sick Leave Balance", 
    quota = 10,
    used = 2,
    expiredDate = "December 31, 2024"
)
```

### Fragment Implementation
```kotlin
class LeaveDashboardFragment : Fragment() {
    
    private fun setupLeaveQuotas() {
        val leaveQuotas = listOf(
            LeaveQuota("Annual Leave Balance", 15, 7, "December 31, 2024"),
            LeaveQuota("Sick Leave Balance", 10, 2, "December 31, 2024"),
            LeaveQuota("Maternity Leave Balance", 90, 0, "June 30, 2024"),
            LeaveQuota("Personal Leave Balance", 5, 0, "December 31, 2024")
        )
        
        val adapter = LeaveQuotaAdapter(leaveQuotas)
        binding.recyclerViewLeaveQuotas.adapter = adapter
    }
}
```

### Date Formatting Helper
```kotlin
// Extension function for professional date formatting
fun Date.formatToDisplayDate(): String {
    val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
    return formatter.format(this)
}

// Usage
val expiryDate = Date().formatToDisplayDate() // "December 31, 2024"
```

## Do's and Don'ts

### ✅ Do's
- Use for displaying employee leave balances in corporate HR applications
- Provide clear, professional titles using standard business terminology
- Ensure leave quota and used values are always accurate and current
- Use consistent English date formatting (e.g., "December 31, 2024")
- Display multiple leave types in a RecyclerView for comprehensive employee overview
- Update card data in real-time when leave requests are approved or utilized
- Test with various leave scenarios including zero balance and maximum usage

### ❌ Don'ts
- Use for non-HR related information or non-leave data
- Display negative values for leave quotas
- Mix different date formats within the same application screen
- Override the card's professional material design styling
- Use informal or colloquial language in a corporate application
- Forget to implement proper data refresh mechanisms
- Display expired leave quotas without clear visual indicators

## Performance Considerations

### RecyclerView Optimization
```kotlin
binding.recyclerViewLeaveQuotas.apply {
    layoutManager = LinearLayoutManager(requireContext())
    adapter = leaveQuotaAdapter
    setHasFixedSize(true) // Optimize when all cards have consistent height
    addItemDecoration(
        DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
    )
}
```

### Efficient Data Updates
```kotlin
// Update specific leave quota efficiently
fun updateLeaveUsage(leaveType: String, newUsed: Int) {
    val position = leaveQuotas.indexOfFirst { it.title == leaveType }
    if (position != -1) {
        leaveQuotas[position] = leaveQuotas[position].copy(used = newUsed)
        leaveQuotaAdapter.notifyItemChanged(position)
    }
}
```

## Material Design Styling

The component implements enterprise-grade Material Design with:

- **Professional Card Layout**:
  - 8dp corner radius for modern, corporate appearance
  - 1dp elevation with dynamic shadows for depth perception
  - Subtle stroke border using `colorStrokeSubtle` for definition
  - Appropriate padding for clean content spacing

- **Corporate Typography Hierarchy**:
  - Title: Professional styling for leave type identification
  - Leave Quota: Prominent, emphasized text for quick balance reading
  - Leave Used: Secondary information with clear usage context
  - Expiry Date: Tertiary text with standardized expiry formatting

- **Enterprise Color Scheme**:
  - Uses EDTS Desklab App theme attributes
  - Maintains corporate brand consistency
  - Ensures WCAG accessibility compliance for all users

### Custom Styling
```xml
<style name="Widget.EDTS.LeaveQuotaCard" parent="Widget.Material3.CardView">
    <item name="cardBackgroundColor">?attr/colorBackgroundPrimary</item>
    <item name="strokeColor">?attr/colorStrokeSubtle</item>
    <item name="cardElevation">1dp</item>
    <item name="cardCornerRadius">8dp</item>
</style>
```

---

>[!Note]
> This component is specifically designed for the EDTS Desklab App's HR management system, providing employees and managers with clear, professional visibility into leave balances and usage. The corporate-appropriate English formatting and clean design ensure important leave information is accessible at a glance while maintaining the professional standards expected in enterprise applications.
