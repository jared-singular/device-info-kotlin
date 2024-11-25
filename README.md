# device-info-kotlin
Device Info Sample App in Kotlin

## Overview

DeviceInfo is a Kotlin-based singleton class that provides a centralized way to collect crucial Android device data points. This information is essential for maintaining device graphs and processing out-of-app events through Singular's API.

## Key Features

- Singleton architecture for consistent device information access
- Comprehensive device data collection
- Ad tracking status monitoring
- System-level information gathering
- Coroutine support for asynchronous operations

## Core Components

**DeviceInfo Class**
```kotlin
class DeviceInfo private constructor(context: Context) {
    companion object {
        @Volatile
        private var instance: DeviceInfo? = null
        
        fun getInstance(context: Context): DeviceInfo {
            return instance ?: synchronized(this) {
                instance ?: DeviceInfo(context.applicationContext).also { 
                    instance = it 
                }
            }
        }
    }
}
```

## Data Points Collected

**App Information**
- `appVersion`: Current application version name
- `packageName`: Application package identifier
- `buildVersion`: System build version information

**Device Identifiers**
- `gaid`: Google Advertising ID
- `appSetId`: App Set ID for user tracking
- `androidId`: Android device identifier
- `deviceModel`: Physical device model information

**System Details**
- `osVersion`: Current Android version
- `locale`: Device's current locale setting
- `adTrackingStatus`: Ad tracking authorization status

## Usage

**Retrieving All Device Information**
```kotlin
val deviceInfo = DeviceInfo.getInstance(context)
lifecycleScope.launch {
    val allInfo = deviceInfo.getAllDeviceInfo()
}
```

**Debugging Output**
```kotlin
lifecycleScope.launch {
    deviceInfo.printDeviceInfo()
}
```

## Implementation Guide

1. Initialize the DeviceInfo singleton
2. Collect device information using getAllDeviceInfo()
3. Send the data to your server
4. Store the information in your device graph
5. Use the stored data points when sending event requests to Singular

## Best Practices

- Store device information server-side for consistent tracking
- Update device information periodically to maintain accuracy
- Handle ad tracking status changes appropriately
- Implement proper error handling for missing data points

## Integration with Singular

When sending Server-to-Server events:
1. Retrieve stored device information from your device graph
2. Include relevant device data points in your API requests
3. Maintain data consistency across different events

## Development and Debugging

The app provides two ways to verify collected data:
- Logcat output through printDeviceInfo()
- Visual representation in the DeviceInfo Fragment

## Screenshots ##
| About the App | DeviceInfo Dictionary |
|:---:|:---:|
| ![about-kotlin](https://github.com/user-attachments/assets/fcc900e3-7854-41a1-8c30-542861288101) | ![DeviceInfo-kotlin](https://github.com/user-attachments/assets/0bcaaf3f-3312-48d3-8eb5-7d4abc47b816) |

## Requirements

- Android API 21+
- Kotlin 1.5+
- Android Studio Arctic Fox+

## Dependencies

- Google Play Services Ads
- Google Play Services AppSet
- AndroidX Core KTX
- Kotlin Coroutines

## License
[See LICENSE](https://github.com/jared-singular/device-info-kotlin/blob/main/LICENSE)
