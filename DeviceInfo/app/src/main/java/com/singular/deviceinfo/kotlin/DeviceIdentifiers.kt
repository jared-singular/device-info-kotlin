package com.singular.deviceinfo.kotlin

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.appset.AppSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeviceIdentifiers private constructor(context: Context) {
    private val appContext = context.applicationContext

    companion object {
        @Volatile
        private var instance: DeviceIdentifiers? = null

        fun getInstance(context: Context): DeviceIdentifiers {
            return instance ?: synchronized(this) {
                instance ?: DeviceIdentifiers(context.applicationContext).also { instance = it }
            }
        }
    }

    val androidId: String
        get() = try {
            Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
        } catch (e: Exception) {
            ""
        }

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
                    Log.e("DeviceIdentifiers", "Failed to retrieve App Set ID", exception)
                    continuation.resume("")
                }
        }
    }

    val adTrackingStatus: String
        get() = try {
            val manager = AdvertisingIdClient.getAdvertisingIdInfo(appContext)
            if (manager.isLimitAdTrackingEnabled) "2" else "3"
        } catch (e: Exception) {
            "0"
        }

    suspend fun getAllIdentifiers(): Map<String, String> {
        return mapOf(
            "adTrackingStatus" to adTrackingStatus,
            "appSetId" to getAppSetId(),
            "gaid" to getGaid(),
            "androidId" to androidId
        )
    }

    suspend fun printIdentifiers() {
        Log.d("DeviceIdentifiers", "Ad Tracking Status: $adTrackingStatus")
        Log.d("DeviceIdentifiers", "App Set ID: ${getAppSetId()}")
        Log.d("DeviceIdentifiers", "GAID: ${getGaid()}")
        Log.d("DeviceIdentifiers", "AndroidId: $androidId")
    }
}