package com.megchan.poctestabfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.megchan.poctestabfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val preferences by lazy { LocalPreferencesStorage(this) }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSwitch()
        setupButtonListener()
    }

    private fun setupButtonListener() {
        binding.btnNextScreen.setOnClickListener {
            TestActivity.getStartIntent(this@MainActivity).run(::startActivity)
        }
    }

    private fun setupSwitch() {
        with(binding.switchFeatureEnabled) {
            isChecked = preferences.retrieveConsent()
            setOnCheckedChangeListener { _, isGranted ->
                preferences.savePreference(isGranted)
            }
        }
    }
}