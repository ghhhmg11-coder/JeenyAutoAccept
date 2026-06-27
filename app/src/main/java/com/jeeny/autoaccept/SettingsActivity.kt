package com.jeeny.autoaccept

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences("jeeny_settings", MODE_PRIVATE)

        val etMinDistance = findViewById<EditText>(R.id.etMinDistance)
        val etMaxDistance = findViewById<EditText>(R.id.etMaxDistance)
        val etMinFare = findViewById<EditText>(R.id.etMinFare)
        val etMaxFare = findViewById<EditText>(R.id.etMaxFare)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnBack = findViewById<Button>(R.id.btnBack)

        etMinDistance.setText(prefs.getFloat("min_distance", 0f).toString())
        etMaxDistance.setText(prefs.getFloat("max_distance", 10f).toString())
        etMinFare.setText(prefs.getFloat("min_fare", 0f).toString())
        etMaxFare.setText(prefs.getFloat("max_fare", 999f).toString())

        btnSave.setOnClickListener {
            val minDist = etMinDistance.text.toString().toFloatOrNull() ?: 0f
            val maxDist = etMaxDistance.text.toString().toFloatOrNull() ?: 10f
            val minFare = etMinFare.text.toString().toFloatOrNull() ?: 0f
            val maxFare = etMaxFare.text.toString().toFloatOrNull() ?: 999f

            prefs.edit()
                .putFloat("min_distance", minDist)
                .putFloat("max_distance", maxDist)
                .putFloat("min_fare", minFare)
                .putFloat("max_fare", maxFare)
                .apply()

            Toast.makeText(this, "تم حفظ الإعدادات ✓", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnBack.setOnClickListener { finish() }
    }
}
