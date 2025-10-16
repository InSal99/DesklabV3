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
    implementation 'com.github.InSal99:DesklabV3:$latestVersion'
}
```

Replace `latest-version` with the version number shown in the JitPack badge above.

---

## 📚 Component Catalog

### Interactive Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Button** | ✅ Complete | [View Docs](docs/Button.md) |
| **Checkbox** | ✅ Complete | [View Docs](docs/CheckBox.md) |
| **Radio Button** | ✅ Complete | [View Docs](docs/RadioButton.md) |
| **Input Field** | ✅ Complete | [View Docs](docs/InputField.md) |
| **Input Search** | ✅ Complete | [View Docs](docs/InputSearch.md) |
| **Sort Button** | ✅ Complete | [View Docs](docs/SortButton.md) |

### Navigation Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Bottom Navigation** | ✅ Complete | [View Docs](docs/BottomNavigation.md) |
| **Bottom Navigation Item** | ✅ Complete | [View Docs](docs/BottomNavigationItem.md) |
| **Tab** | ✅ Complete | [View Docs](docs/Tab.md) |
| **Tab Item** | ✅ Complete | [View Docs](docs/TabItem.md) |
| **Header** | ✅ Complete | [View Docs](docs/Header.md) |

### Display Components

| Component               | Status | Documentation |
|-------------------------|--------|---------------|
| **Infobox**             | ✅ Complete | [View Docs](docs/InfoBox.md) |
| **Toast**               | ✅ Complete | [View Docs](docs/Toast.md) |
| **Status Badge**        | ✅ Complete | [View Docs](docs/StatusBadge.md) |
| **Badge**               | ✅ Complete | [View Docs](docs/Badge.md) |
| **Detail Info Speaker** | ✅ Complete | [View Docs](docs/DetailInformationSpeaker.md) |
| **Footer**              | ✅ Complete | [View Docs](docs/Footer.md) |
| **Infobox Footer**      | ✅ Complete | [View Docs](docs/InfoBoxFooter.md) |

### Card Components

| Component                | Status | Documentation |
|--------------------------|--------|---------------|
| **Option Card**          | ✅ Complete | [View Docs](docs/OptionCard.md) |
| **Multi Detail Card**    | ✅ Complete | [View Docs](docs/CardMultiDetail.md) |
| **Detail Information B** | ✅ Complete | [View Docs](docs/CardDetailInfoB.md) |
| **Card: Left Slot**      | ✅ Complete | [View Docs](docs/CardLeftSlot.md) |
| **Event Card**           | ✅ Complete | [View Docs](docs/EventCard.md) |
| **Event Card: Badge**    | ✅ Complete | [View Docs](docs/EventCardBadge.md) |
| **Event Card: Status**   | ✅ Complete | [View Docs](docs/EventCardStatus.md) |
| **Event Notification Card** | ✅ Complete | [View Docs](docs/NotificationCard.md) |
| **My Event Card**        | ✅ Complete | [View Docs](docs/MyEventCard.md) |
| **Leave Card**           | ✅ Complete | [View Docs](docs/LeaveCard.md) |

### Selection Components

| Component | Status | Documentation                                |
|-----------|--------|----------------------------------------------|
| **Dropdown Filter** | ✅ Complete | [View Docs](docs/SelectionDropdownFilter.md) |
| **Chip** | ✅ Complete | [View Docs](docs/SelectionChip.md)           |

### Container Components

| Component | Status | Documentation |
|-----------|--------|---------------|
| **Bottom Tray** | ✅ Complete | [View Docs](docs/BottomTray.md) |
| **Flat: Detail Info A** | ✅ Complete | [View Docs](docs/DetailInformationA.md) |

### Modal Components

| Component                 | Status | Documentation |
|---------------------------|--------|---------------|
| **Modality Confirmation** | ✅ Complete | [View Docs](docs/ModalityConfirmation.md) |
| **Modality Loading**      | ✅ Complete | [View Docs](docs/ModalityLoading.md) |

### Utility Components

| Component                      | Status | Documentation |
|--------------------------------|--------|---------------|
| **Monthly Picker**             | ✅ Complete | [View Docs](docs/MonthlyPicker.md) |
| **Dropdown Filter Horizontal** | ✅ Complete | [View Docs](docs/DropdownFilterHorizontal.md) |

---

## 🚀 Quick Start

Here's a simple example of how to use a component from the library:

```kotlin
import com.desklab.components.button.Button
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
