package com.singular.deviceinfo.kotlin

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import com.google.android.gms.appset.AppSet
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeviceInfo private constructor(context: Context) {
    // Use application context to prevent memory leaks
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

    // App Information
    val appVersion: String
        get() = try {
            appContext.packageManager.getPackageInfo(appContext.packageName, 0).versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }

    val packageName: String
        get() = appContext.packageName


    // System Information
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

    val androidId: String
        get() = try {
            Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
        } catch (e: Exception) {
            ""
        }

    // Device Identifiers
    suspend fun getGaid(): String {
        return withContext(Dispatchers.IO) {
            try {
                AdvertisingIdClient.getAdvertisingIdInfo(appContext).id ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }

    suspend fun getAppSetId(): String {
        return suspendCoroutine { continuation ->
            val client = AppSet.getClient(appContext)
            client.appSetIdInfo
                .addOnSuccessListener { info ->
                    continuation.resume(info.id)
                }
                .addOnFailureListener { exception ->
                    Log.e("DeviceInfo", "Failed to retrieve App Set ID", exception)
                    continuation.resume("")
                }
        }
    }

    val adTrackingStatus: String
        get() = try {
            val manager = AdvertisingIdClient.getAdvertisingIdInfo(appContext)
            if (manager.isLimitAdTrackingEnabled) "2" else "3"
            /* "0": Error or Not Determined - Returned when there's an exception or tracking status cannot be determined
               "2": Denied/Limited - When limitAdTrackingEnabled is true, indicating the user has opted out of ad tracking
               "3": Authorized - When limitAdTrackingEnabled is false, indicating the user allows ad tracking */
        } catch (e: Exception) {
            "0"
        }

    suspend fun getAllDeviceInfo(): Map<String, String> {
        return mapOf(
            "packageName" to packageName,
            "appVersion" to appVersion,
            "adTrackingStatus" to adTrackingStatus,
            "appSetId" to getAppSetId(),
            "gaid" to getGaid(),
            "androidId" to androidId,
            "locale" to locale,
            "deviceMake" to deviceMake,
            "deviceModel" to deviceModel,
            "buildVersion" to buildVersion,
            "osVersion" to osVersion
        )
    }

    suspend fun printDeviceInfo() {
        Log.d("DeviceInfo", "Package Name: $packageName")
        Log.d("DeviceInfo", "App Version: $appVersion")
        Log.d("DeviceInfo", "Ad Tracking Status: $adTrackingStatus")
        Log.d("DeviceInfo", "App Set ID: ${getAppSetId()}")
        Log.d("DeviceInfo", "GAID: ${getGaid()}")
        Log.d("DeviceInfo", "AndroidId: $androidId")
        Log.d("DeviceInfo", "Locale: $locale")
        Log.d("DeviceInfo", "Device Make: $deviceMake")
        Log.d("DeviceInfo", "Device Model: $deviceModel")
        Log.d("DeviceInfo", "Build Version: $buildVersion")
        Log.d("DeviceInfo", "OS Version: $osVersion")
    }
}