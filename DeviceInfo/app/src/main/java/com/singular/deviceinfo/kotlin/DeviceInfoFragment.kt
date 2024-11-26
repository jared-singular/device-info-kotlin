package com.singular.deviceinfo.kotlin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DeviceInfoFragment : Fragment() {
    private lateinit var deviceInfo: DeviceInfo
    private lateinit var deviceIdentifiers: DeviceIdentifiers

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize DeviceInfo
        deviceInfo = DeviceInfo.getInstance(requireContext())
        // Initialize DeviceIdentifiers
        deviceIdentifiers = DeviceIdentifiers.getInstance(requireContext())

        // Create root layout (keep existing layout code...)
        val rootLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)
        }

        // Title
        val titleText = TextView(requireContext()).apply {
            text = "DeviceInfo Dictionary"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 64
                bottomMargin = 32
            }
        }
        rootLayout.addView(titleText)

        // Subtitle
        val subtitleText = TextView(requireContext()).apply {
            text = "Required Data Points"
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 32
                bottomMargin = 24
            }
        }
        rootLayout.addView(subtitleText)

        // Description
        val descriptionText = TextView(requireContext()).apply {
            text = "These device data points are required for all Singular Event REST API calls and must be captured client-side and stored on your server."
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 32
                bottomMargin = 24
            }
        }
        rootLayout.addView(descriptionText)

        // Divider
        val divider = View(requireContext()).apply {
            setBackgroundColor(Color.LTGRAY)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                3
            ).apply {
                topMargin = 32
                bottomMargin = 24
            }
        }
        rootLayout.addView(divider)

        // Create data layout
        val dataLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(20, 20, 20, 20)
        }

        // Launch coroutine to fetch async data
        lifecycleScope.launch {
            deviceInfo.printDeviceInfo()
            deviceIdentifiers.printIdentifiers()

            val gaid = deviceIdentifiers.getGaid()
            val appSetId = deviceIdentifiers.getAppSetId()
            val deviceInfoItems = listOf(
                "Package Name: ${deviceInfo.packageName}",
                "App Version: ${deviceInfo.appVersion}",
                "Ad Tracking: ${deviceIdentifiers.adTrackingStatus}",
                "GAID: $gaid",
                "App Set ID: $appSetId",
                "AndroidId: ${deviceIdentifiers.androidId}",
                "Locale: ${deviceInfo.locale}",
                "Device Make: ${deviceInfo.deviceMake}",
                "Device Model: ${deviceInfo.deviceModel}",
                "Build Version: ${deviceInfo.buildVersion}",
                "OS Version: ${deviceInfo.osVersion}"
            )

            // Update UI on main thread
            withContext(Dispatchers.Main) {
                deviceInfoItems.forEach { info ->
                    TextView(requireContext()).apply {
                        text = info
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            bottomMargin = 10
                        }
                    }.also { dataLayout.addView(it) }
                }
            }
        }

        rootLayout.addView(dataLayout)

        return ScrollView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(rootLayout)
        }
    }
}