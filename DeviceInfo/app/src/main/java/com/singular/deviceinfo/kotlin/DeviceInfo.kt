package com.singular.deviceinfo.kotlin

import android.content.Context
import android.os.Build
import android.util.Log
import java.util.Locale

class DeviceInfo private constructor(context: Context) {
    private val appContext = context.applicationContext

    companion object {
        @Volatile
        private var instance: DeviceInfo? = null

        fun getInstance(context: Context): DeviceInfo {
            return instance ?: synchronized(this) {
                instance ?: DeviceInfo(context.applicationContext).also { instance = it }
            }
        }
    }
    val appVersion: String
        get() = try {
            appContext.packageManager.getPackageInfo(appContext.packageName, 0).versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }

    val packageName: String
        get() = appContext.packageName

    val osVersion: String
        get() = Build.VERSION.RELEASE

    val locale: String
        get() = Locale.getDefault().toString()

    val deviceModel: String
        get() = Build.MODEL

    val deviceMake: String
        get() = Build.MANUFACTURER

    val buildVersion: String
        get() = "Build/${Build.ID}"

    fun getAllDeviceInfo(): Map<String, String> {
        return mapOf(
            "packageName" to packageName,
            "appVersion" to appVersion,
            "locale" to locale,
            "deviceMake" to deviceMake,
            "deviceModel" to deviceModel,
            "buildVersion" to buildVersion,
            "osVersion" to osVersion
        )
    }

    fun printDeviceInfo() {
        Log.d("DeviceInfo", "Package Name: $packageName")
        Log.d("DeviceInfo", "App Version: $appVersion")
        Log.d("DeviceInfo", "Locale: $locale")
        Log.d("DeviceInfo", "Device Make: $deviceMake")
        Log.d("DeviceInfo", "Device Model: $deviceModel")
        Log.d("DeviceInfo", "Build Version: $buildVersion")
        Log.d("DeviceInfo", "OS Version: $osVersion")
    }
}