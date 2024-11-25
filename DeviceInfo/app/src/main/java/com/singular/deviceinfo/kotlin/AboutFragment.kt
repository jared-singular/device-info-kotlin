package com.singular.deviceinfo.kotlin

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

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create root vertical layout
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
            text = "About the App"
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

        // Version info
        val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.longVersionCode.toInt()

        val versionText = TextView(requireContext()).apply {
            text = "Version: ${versionName}"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        rootLayout.addView(versionText)

        val buildText = TextView(requireContext()).apply {
            text = "Build: ${versionCode}"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 32
                bottomMargin = 24
            }
        }
        rootLayout.addView(buildText)

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

        // Purpose section
        val purposeTitle = TextView(requireContext()).apply {
            text = "Purpose"
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 32
                bottomMargin = 16
            }
        }
        rootLayout.addView(purposeTitle)

        // Purpose text paragraphs
        val purposeTexts = arrayOf(
            "This app demonstrates how to obtain the required Android device data points needed for sending Singular Server-to-Server REST API requests. The DeviceInfo class is used to extract the data points and store in a Dictionary.",
            "This payload should be sent to your server and maintained in a device graph for the user. When an out-of-app event is sent from your server, you will then have the required data points to send an Event request to Singular.",
            "This app also displays the data in the Android Studio Logcat and the About View for development and debugging purposes.",
            "Click the 'Info' button to see the DeviceInfo data points."
        )

        purposeTexts.forEach { text ->
            TextView(requireContext()).apply {
                this.text = text
                this.textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 20
                    bottomMargin = 20
                }
            }.also { rootLayout.addView(it) }
        }

        // Wrap in ScrollView for scrollable content
        return ScrollView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(rootLayout)
        }
    }
}