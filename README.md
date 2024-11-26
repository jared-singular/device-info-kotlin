# Device Info Kotlin Library

A lightweight Kotlin library for collecting essential Android device data points required for maintaining device graphs and processing server-to-server events with Singular's API.

**TL;DR:**
- Collect device identifiers and system information using two main classes: `DeviceInfo` and `DeviceIdentifiers`
- Implements singleton pattern for consistent data access across your app
- Supports asynchronous operations through Kotlin Coroutines
- Provides comprehensive device data points required for Singular's REST API

## Core Components

### DeviceInfo Class
Handles collection of device and system-level information:

```kotlin
val deviceInfo = DeviceInfo.getInstance(context)
val systemInfo = deviceInfo.getAllDeviceInfo()
```

**Available Data Points:**
- `appVersion`: Application version name
- `packageName`: Application package identifier
- `osVersion`: Android OS version
- `locale`: Device locale settings
- `deviceModel`: Physical device model
- `deviceMake`: Device manufacturer
- `buildVersion`: System build information

### DeviceIdentifiers Class
Manages device identification and tracking-related data:

```kotlin
val deviceIdentifiers = DeviceIdentifiers.getInstance(context)
lifecycleScope.launch {
    val identifiers = deviceIdentifiers.getAllIdentifiers()
}
```

**Available Identifiers:**
- `androidId`: Android device identifier
- `gaid`: Google Advertising ID (requires async call)
- `appSetId`: App Set ID for user tracking
- `adTrackingStatus`: Current ad tracking authorization state

## Implementation Guide

**1. Initialize Components**
```kotlin
val deviceInfo = DeviceInfo.getInstance(context)
val deviceIdentifiers = DeviceIdentifiers.getInstance(context)
```

**2. Collect All Data Points**
```kotlin
lifecycleScope.launch {
    val deviceData = deviceInfo.getAllDeviceInfo()
    val identifiers = deviceIdentifiers.getAllIdentifiers()
    
    // Combine data for server upload
    val serverData = deviceData + identifiers
}
```

## Server Integration

### Device Graph Storage
Store collected data points in your device graph with these key considerations:
- Map multiple identifiers to a single user profile
- Update data points periodically to maintain accuracy
- Handle missing or null values gracefully

### Singular API Integration
When sending events to Singular's REST API:
1. Retrieve stored device information from your device graph
2. Include relevant device data in the API payload
3. Ensure consistent identifier usage across events

## Technical Requirements

**Android Environment:**
- Minimum API Level: 21+
- Kotlin Version: 1.5+
- Android Studio: Arctic Fox or newer

**Dependencies:**
- Google Play Services Ads
- Google Play Services AppSet
- AndroidX Core KTX
- Kotlin Coroutines

## Best Practices

- Cache device information to minimize API calls
- Implement proper error handling for missing identifiers
- Monitor ad tracking status changes
- Update device information periodically
- Use coroutines for asynchronous operations
- Implement proper error handling for all data collection methods

## Debugging

Debug collected data using built-in logging methods:

```kotlin
lifecycleScope.launch {
    deviceInfo.printDeviceInfo()
    deviceIdentifiers.printIdentifiers()
}
```

The app provides two ways to verify collected data:
- Logcat output through printDeviceInfo()
- Visual representation in the DeviceInfo Fragment

## Screenshots ##
| About the App | DeviceInfo Dictionary |
|:---:|:---:|
| ![about-kotlin](https://github.com/user-attachments/assets/fcc900e3-7854-41a1-8c30-542861288101) | ![DeviceInfo-kotlin](https://github.com/user-attachments/assets/0bcaaf3f-3312-48d3-8eb5-7d4abc47b816) |

## License
[See LICENSE](https://github.com/jared-singular/device-info-kotlin/blob/main/LICENSE)
