package com.jeeny.autoaccept

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var switchAutoAccept: Switch
    private lateinit var tvStatus: TextView
    private lateinit var btnSettings: Button
    private lateinit var btnOverlay: Button
    private lateinit var btnAccessibility: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("jeeny_settings", MODE_PRIVATE)

        switchAutoAccept = findViewById(R.id.switchAutoAccept)
        tvStatus = findViewById(R.id.tvStatus)
        btnSettings = findViewById(R.id.btnSettings)
        btnOverlay = findViewById(R.id.btnOverlay)
        btnAccessibility = findViewById(R.id.btnAccessibility)

        switchAutoAccept.isChecked = prefs.getBoolean("auto_accept", false)
        updateStatus()

        switchAutoAccept.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isAccessibilityEnabled()) {
                switchAutoAccept.isChecked = false
                Toast.makeText(this, "يجب تفعيل خدمة الوصول أولاً", Toast.LENGTH_LONG).show()
                openAccessibilitySettings()
                return@setOnCheckedChangeListener
            }
            prefs.edit().putBoolean("auto_accept", isChecked).apply()
            updateStatus()
            val msg = if (isChecked) "تم تفعيل القبول التلقائي ✓" else "تم إيقاف القبول التلقائي"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        btnAccessibility.setOnClickListener { openAccessibilitySettings() }
        btnOverlay.setOnClickListener { openOverlaySettings() }
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updatePermissionsUI()
    }

    private fun updateStatus() {
        val isOn = prefs.getBoolean("auto_accept", false)
        if (isOn) {
            tvStatus.text = "القبول التلقائي: مفعّل ✓"
            tvStatus.setTextColor(getColor(R.color.green))
        } else {
            tvStatus.text = "القبول التلقائي: متوقف"
            tvStatus.setTextColor(getColor(R.color.red))
        }
    }

    private fun updatePermissionsUI() {
        val accessOk = isAccessibilityEnabled()
        val overlayOk = Settings.canDrawOverlays(this)
        btnAccessibility.text = if (accessOk) "✓ خدمة الوصول مفعّلة" else "تفعيل خدمة الوصول (مطلوب)"
        btnOverlay.text = if (overlayOk) "✓ الظهور فوق التطبيقات مفعّل" else "تفعيل الظهور فوق التطبيقات (مطلوب)"
    }

    private fun isAccessibilityEnabled(): Boolean {
        val service = "$packageName/${JeenyAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.split(":").any { it.equals(service, ignoreCase = true) }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun openOverlaySettings() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivity(intent)
    }
}
