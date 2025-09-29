# Desklab Components Library

A comprehensive UI component library designed specifically for Desklab Project Android applications. This library provides a collection of customizable, production-ready components that follow modern Android development best practices.

> **Note**: This library is currently under active development. Features and APIs may change as we continue to improve stability and expand functionality.

---

## 📦 Installation

Add the JitPack repository to your root `build.gradle` file:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app's `build.gradle` file:

```groovy
dependencies {
    implementation("")
}
```

Replace `latest-version` with the version number shown in the JitPack badge above.

---

## 📚 Component Catalog

### Interactive Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Button** | 🔨 In Progress | [View Docs]() |
| **Checkbox** | 🔨 In Progress | [View Docs]() |
| **Radio Button** | 🔨 In Progress | [View Docs]() |
| **Input Field** | 🔨 In Progress | [View Docs]() |
| **Input Search** | ✅ Complete | [View Docs](md/input_search_docs.md) |
| **Sort Button** | ✅ Complete | [View Docs](md/sort_button_docs.md) |

### Navigation Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Bottom Navigation** | ✅ Complete | [View Docs](md/bottom_navigation_docs.md) |
| **Bottom Navigation Item** | ✅ Complete | [View Docs](md/bottom_navigation_item_docs.md) |
| **Tab** | ✅ Complete | [View Docs](md/tab_docs.md) |
| **Tab Item** | ✅ Complete | [View Docs](md/tab_item_docs.md) |
| **Header** | ✅ Complete | [View Docs](md/header_docs.md) |

### Display Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Infobox** | 🔨 In Progress | [View Docs]() |
| **Toast** | 🔨 In Progress | [View Docs]() |
| **Status Badge** | 🔨 In Progress | [View Docs]() |
| **Badge** | ✅ Complete | [View Docs](md/badge_docs.md) |
| **Info Speaker** | 🔨 In Progress | [View Docs]() |
| **Footer** | 🔨 In Progress | [View Docs]() |

### Card Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Option Card** | 🔨 In Progress | [View Docs]() |
| **Multi Detail Card** | ✅ Complete | [View Docs](md/card_multi_detail_docs.md) |
| **Detail Information B** | ✅ Complete | [View Docs](md/card_detail_info_b_docs.md) |
| **Card: Left Slot** | ✅ Complete | [View Docs](md/card_left_slot_docs.md) |
| **Event Card** | ✅ Complete | [View Docs](md/event_card_docs.md) |
| **Event Card: Badge** | ✅ Complete | [View Docs](md/event_card_badge_docs.md) |
| **Event Card: Status** | ✅ Complete | [View Docs](md/event_card_status_docs.md) |
| **Invitation Card** | 🔨 In Progress | [View Docs]() |
| **My Event Card** | 🔨 In Progress | [View Docs]() |

### Selection Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Dropdown Filter** | ✅ Complete | [View Docs](md/dropdown_filter_docs.md) |
| **Chip** | ✅ Complete | [View Docs](md/chip_docs.md) |

### Container Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Bottom Tray** | 🔨 In Progress | [View Docs]() |
| **Flat: Detail Info A** | 🔨 In Progress | [View Docs]() |

### Modal Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Dialog Pop-Up** | 🔨 In Progress | [View Docs]() |
| **Loading Pop-Up** | 🔨 In Progress | [View Docs]() |

### Utility Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Monthly Picker** | 🔨 In Progress | [View Docs]() |

---

## 🚀 Quick Start

Here's a simple example of how to use a component from the library:

```kotlin
import com.desklab.components.button.DesklabButton
```

For detailed usage examples and customization options, please refer to the individual component documentation linked in the tables above.

---

## 📋 Requirements

- **Minimum SDK**: Android API 24 (Android 7.0 Nougat)
- **Target SDK**: Android API 34 (Android 14)
- **Kotlin Version**: 1.7.10
- **Gradle Version**: 7.3.0

---

## 🤝 Contributing

This library is developed and maintained internally for Desklab Project. For bug reports, feature requests, or other inquiries, please contact the DMP 2 team.

---

## 📄 License

Desklab Components Library is proprietary software.

**Copyright © 2025 All Rights Reserved by EDTS**

This library is released under SG-EDTS company license. Unauthorized copying, modification, distribution, or use of this software is strictly prohibited.

---

## 📞 Contact & Support

For further information, support, or inquiries:

- **Website**: [https://sg-edts.com](https://sg-edts.com)
- **Email**: [info@sg-edts.com](mailto:info@sg-edts.com)
- **Company**: SG-EDTS

---

**Status Legend**:
- ✅ Complete - Fully implemented with documentation
- 🔨 In Progress - Under development or documentation in progress
